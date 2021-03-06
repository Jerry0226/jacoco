/*******************************************************************************
 * Copyright (c) 2009, 2016 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.agent.rt.internal.output;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.jacoco.core.runtime.AgentOptions;
import org.jacoco.core.runtime.IRemoteCommandVisitor;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;
import org.jacoco.core.runtime.RuntimeData;

/**
 * Handler for a single socket based remote connection.
 */
class TcpConnection implements IRemoteCommandVisitor {

	private AgentOptions options;
	
	private final RuntimeData data;

	private final Socket socket;

	private RemoteControlWriter writer;

	private RemoteControlReader reader;

	private boolean initialized;
	
	public TcpConnection(final Socket socket, final RuntimeData data) {
		this.socket = socket;
		this.data = data;
		this.initialized = false;
	}

	public TcpConnection(final AgentOptions options, final Socket socket, final RuntimeData data) {
		this.socket = socket;
		this.data = data;
		this.initialized = false;
		this.options = options;
	}

	public void init() throws IOException {
		this.writer = new RemoteControlWriter(socket.getOutputStream());
		this.reader = new RemoteControlReader(socket.getInputStream());
		this.reader.setRemoteCommandVisitor(this);
		this.initialized = true;
		if (options != null && options.getId(AgentOptions.DEFAULT_ID, 0) > 0) {
			writer.writeFirstHand(options.getId(AgentOptions.DEFAULT_ID, 0), options.getSessionId());
		}
		
	}

	/**
	 * Processes all requests for this session until the socket is closed.
	 * 
	 * @throws IOException
	 *             in case of problems whith the connection
	 */
	public void run() throws IOException {
		try {
			while (reader.read()) {
			}
			
			//只有链接中断的时候才会结束
			if(options != null && options.getId(AgentOptions.DEFAULT_ID, 0) > 0) {
			    System.out.println("End read, connection exception!!!");
			    throw new IOException("ThreadingTest client disconnection");
			}
		} catch (final SocketException e) {
			// If the local socket is closed while polling for commands the
			// SocketException is expected.
			if (!socket.isClosed()) {
				throw e;
			}
		} finally {
			close();
		}
	}
	
	
	

	/**
	 * Dumps the current execution data if the connection is already initialized
	 * and the underlying socket is still open.
	 * 
	 * @param reset
	 *            if <code>true</code> execution data is cleared afterwards
	 * @throws IOException
	 */
	public void writeExecutionData(final boolean reset) throws IOException {
		if (initialized && !socket.isClosed()) {
			visitDumpCommand(true, reset);
		}
	}

	/**
	 * Closes the underlying socket if not closed yet.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (!socket.isClosed()) {
			socket.close();
		}
	}

	// === IRemoteCommandVisitor ===

	public void visitDumpCommand(final boolean dump, final boolean reset)
			throws IOException {
		if (dump) {
//			if (options != null && options.getId(AgentOptions.DEFAULT_ID, 0) > 0) {
		    //每次发起数据的读取动作，都同时写出数据头,在ExecutionDataReader 中进行操作，排除对writer.writeHeader();的操作
//			writer.writeHeader();
//			}
			
			data.collect(writer, writer, reset);
		} else {
			if (reset) {
				data.reset();
			}
		}
		writer.sendCmdOk();
	}

}
