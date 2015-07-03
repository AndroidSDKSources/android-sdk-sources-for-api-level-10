/*
 This Java source file was generated by test-to-java.xsl
 and is a derived work from the source document.
 The source document contained the following notice:



 Copyright (c) 2001-2003 World Wide Web Consortium,
 (Massachusetts Institute of Technology, Institut National de
 Recherche en Informatique et en Automatique, Keio University).  All
 Rights Reserved.  This program is distributed under the W3C's Software
 Intellectual Property License.  This program is distributed in the
 hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 PURPOSE.

 See W3C License http://www.w3.org/Consortium/Legal/ for more details.


 */

package tests.org.w3c.dom;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.DocumentType;
import org.w3c.dom.EntityReference;
import org.w3c.dom.ProcessingInstruction;

import javax.xml.parsers.DocumentBuilder;

/**
 * The method "isSupported(feature,version)" Tests whether the DOM
 * implementation implements a specific feature and that feature is supported by
 * this node.
 *
 * Call the isSupported method on the document element node with a combination
 * of features versions and versions as below. Valid feature names are case
 * insensitive and versions "2.0", "1.0" and if the version is not specified,
 * supporting any version of the feature should return true. Check if the value
 * returned value was true.
 *
 * @author IBM
 * @author Neil Delima
 * @see <a
 *      href="http://www.w3.org/TR/DOM-Level-2-Core/core#Level-2-Core-Node-supports">http://www.w3.org/TR/DOM-Level-2-Core/core#Level-2-Core-Node-supports</a>
 */
@TestTargetClass(Node.class)
public final class NodeIsSupported extends DOMTestCase {

    DOMDocumentBuilderFactory factory;

    DocumentBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        try {
            factory = new DOMDocumentBuilderFactory(DOMDocumentBuilderFactory
                    .getConfiguration1());
            builder = factory.getBuilder();
        } catch (Exception e) {
            fail("Unexpected exception" + e.getMessage());
        }
    }

    protected void tearDown() throws Exception {
        factory = null;
        builder = null;
        super.tearDown();
    }

    /**
     * Runs the test case.
     *
     * @throws Throwable
     *             Any uncaught exception causes test to fail
     */
    @TestTargetNew(
        level = TestLevel.PARTIAL,
        notes = "Doesn't verify that isSupported method can return false value.",
        method = "isSupported",
        args = {java.lang.String.class, java.lang.String.class}
    )
    public void testIsSupported1() throws Throwable {
        Document doc;
        Element element;
        String version = "";
        String version1 = "1.0";
        String version2 = "2.0";
        String featureCore;
        String featureXML;
        boolean success;
        List<String> featuresXML = new ArrayList<String>();
        featuresXML.add("XML");
        featuresXML.add("xmL");

        List<String> featuresCore = new ArrayList<String>();
        featuresCore.add("Core");
        featuresCore.add("CORE");

        doc = (Document) load("staffNS", builder);
        element = doc.getDocumentElement();
        for (int indexN10063 = 0; indexN10063 < featuresXML.size(); indexN10063++) {
            featureXML = (String) featuresXML.get(indexN10063);
            success = element.isSupported(featureXML, version);
            assertTrue("nodeissupported01_XML1", success);
            success = element.isSupported(featureXML, version1);
            assertTrue("nodeissupported01_XML2", success);
        }
        for (int indexN1007C = 0; indexN1007C < featuresCore.size(); indexN1007C++) {
            featureCore = (String) featuresCore.get(indexN1007C);
            success = element.isSupported(featureCore, version);
            assertTrue("nodeissupported01_Core1", success);
            success = element.isSupported(featureCore, version1);
            success = element.isSupported(featureCore, version2);
            assertTrue("nodeissupported01_Core3", success);
        }
    }
    @TestTargetNew(
        level = TestLevel.PARTIAL,
        notes = "Doesn't verify that isSupported method can return false value.",
        method = "isSupported",
        args = {java.lang.String.class, java.lang.String.class}
    )
    public void testIsSupported2() throws Throwable {
        Document doc;
        Attr attribute;
        String version = "";
        String version1 = "1.0";
        String version2 = "2.0";
        String featureCore;
        String featureXML;
        boolean success;
        List<String> featuresXML = new ArrayList<String>();
        featuresXML.add("XML");
        featuresXML.add("xmL");

        List<String> featuresCore = new ArrayList<String>();
        featuresCore.add("Core");
        featuresCore.add("CORE");

        doc = (Document) load("staffNS", builder);
        attribute = doc.createAttribute("TestAttr");
        for (int indexN10064 = 0; indexN10064 < featuresXML.size(); indexN10064++) {
            featureXML = (String) featuresXML.get(indexN10064);
            success = attribute.isSupported(featureXML, version);
            assertTrue("nodeissupported02_XML1", success);
            success = attribute.isSupported(featureXML, version1);
            assertTrue("nodeissupported02_XML2", success);
        }
        for (int indexN1007D = 0; indexN1007D < featuresCore.size(); indexN1007D++) {
            featureCore = (String) featuresCore.get(indexN1007D);
            success = attribute.isSupported(featureCore, version);
            assertTrue("nodeissupported02_Core1", success);
            success = attribute.isSupported(featureCore, version1);
            success = attribute.isSupported(featureCore, version2);
            assertTrue("nodeissupported02_Core3", success);
        }
    }
    @TestTargetNew(
        level = TestLevel.PARTIAL,
        notes = "Verifies that isSupported method returns false value if it's called with empty strings as parameters.",
        method = "isSupported",
        args = {java.lang.String.class, java.lang.String.class}
    )
    public void testIsSupported3() throws Throwable {
        Document doc;
        DocumentType docType;
        boolean success;
        doc = (Document) load("staffNS", builder);
        docType = doc.getDoctype();
        success = docType.isSupported("", "");
        assertFalse("nodeissupported03", success);
    }
    @TestTargetNew(
        level = TestLevel.PARTIAL,
        notes = "Verifies that isSupported method returns false value.",
        method = "isSupported",
        args = {java.lang.String.class, java.lang.String.class}
    )
    public void testIsSupported4() throws Throwable {
        Document doc;
        EntityReference entRef;
        boolean success;
        doc = (Document) load("staffNS", builder);
        entRef = doc.createEntityReference("ent1");
        assertNotNull("createdEntRefNotNull", entRef);
        success = entRef.isSupported("XML CORE", "");
        assertFalse("nodeissupported04", success);
    }
    @TestTargetNew(
        level = TestLevel.PARTIAL,
        notes = "Verifies that isSupported method returns false value.",
        method = "isSupported",
        args = {java.lang.String.class, java.lang.String.class}
    )
    public void testIsSupported5() throws Throwable {
        Document doc;
        ProcessingInstruction pi;
        boolean success;
        doc = (Document) load("staffNS", builder);
        pi = doc.createProcessingInstruction("PITarget", "PIData");
        success = pi.isSupported("-", "+");
        assertFalse("nodeissupported05", success);
    }
}
