package za.co.yellowfire;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class AbstractTestBase {
    protected static final String manifest = "Manifest-Version: 1.0\r\n" +
            "Created-By: Apache Maven\r\n" +
            "Implementation-Vendor: Yellowfire\r\n" +
            "Implementation-Version: 0.1\r\n";

    protected static final String jboss_web = "<jboss-web xmlns=\"http://www.jboss.com/xml/ns/javaee\"\n" +
            "           xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "           xsi:schemaLocation=\"http://www.jboss.com/xml/ns/javaee http://www.jboss.org/j2ee/schema/jboss-web_6_0.xsd\"\n" +
            "           version=\"6.0\">\n" +
            "    <context-root>yellowfire</context-root>\n" +
            "    <resource-ref>\n" +
            "        <res-ref-name>yellowfire/app/ds</res-ref-name>\n" +
            "        <jndi-name>java:/yellowfire/test/ds</jndi-name>\n" +
            "    </resource-ref>\n" +
            "    <resource-ref>\n" +
            "        <res-ref-name>yellowfire/setup/ds</res-ref-name>\n" +
            "        <jndi-name>java:/yellowfire/test/ds</jndi-name>\n" +
            "    </resource-ref>\n" +
            "</jboss-web>";
}
