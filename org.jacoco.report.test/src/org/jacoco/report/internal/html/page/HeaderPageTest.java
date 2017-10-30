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
package org.jacoco.report.internal.html.page;

import static org.junit.Assert.assertEquals;


import org.jacoco.core.internal.analysis.ClassCoverageImpl;
import org.jacoco.core.internal.analysis.MethodCoverageImpl;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class HeaderPageTest extends PageTestBase {
    private ClassCoverageImpl node;

    private ClassPage page;

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();
        node = new ClassCoverageImpl("org/jacoco/example/Foo", 123, false);
        node.addMethod(new MethodCoverageImpl("a", "()V", null));
        node.addMethod(new MethodCoverageImpl("b", "()V", null));
        node.addMethod(new MethodCoverageImpl("c", "()V", null));
    }

    @Test
    public void testHeader() throws Exception {
        page = new ClassPage(node, null, null, rootFolder, context);
        page.render();
        final Document doc = support.parse(output.getFile("Foo.html"));
        int count = 1;
        assertEquals("Element", support.findStr(doc,
                "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Missed Instructions", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Cov.", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Missed Branches", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Cov.", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Missed", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Cxty", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Cov.", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Missed", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Lines", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Cov.", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Missed", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Methods", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        assertEquals("Cov.", support.findStr(doc,
            "/html/body/table[1]/thead/tr[1]/td[" + (count++) +  "]"));
        
    }

}
