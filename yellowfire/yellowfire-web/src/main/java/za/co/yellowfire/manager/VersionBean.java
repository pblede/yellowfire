package za.co.yellowfire.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * Vendor and version information for A4J project
 *
 * @author asmirnov@exadel.com (latest modification by $Author: nbelaevski $)
 * @version $Revision: 17455 $ $Date: 2010-06-01 16:36:23 -0400 (Tue, 01 Jun 2010) $
 */
@ApplicationScoped
@Named("version")
public class VersionBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    /**
     * Class for incapsulate version info.
     *
     * @author asmirnov@exadel.com (latest modification by $Author: nbelaevski $)
     * @version $Revision: 17455 $ $Date: 2010-06-01 16:36:23 -0400 (Tue, 01 Jun 2010) $
     */
    public static class Version {

        private static final String UNKNOWN = "";

        private String implementationVendor = UNKNOWN;

        //TODO nick - default value for manifest file absense - review
        private String implementationVersion = "4.0.0-SNAPSHOT";

        private String implementationTitle = UNKNOWN;

        private String scmRevision = UNKNOWN;

        private String scmTimestamp = UNKNOWN;

        private String fullVersionString = UNKNOWN;

        private String resourceVersion = UNKNOWN;

        private boolean containsDataFromManifest = false;

        public Version() {
            initialize();
        }

        private String getAttributeValueOrDefault(Attributes attributes, String name) {
            String value = attributes.getValue(name);
            if (value == null) {
                value = UNKNOWN;
            }

            return value;
        }

        private void initialize() {
            Manifest manifest = null;
            try {
                manifest = readManifest();
            } catch (Exception e) {
                LOGGER.error(MessageFormat.format("Error reading project metadata: {0}", e.getMessage()), e);
            }

            if (manifest != null) {
                initializePropertiesFromManifest(manifest);
                initializeDerivativeProperties();
            }
        }

        private void initializePropertiesFromManifest(Manifest manifest) {
            containsDataFromManifest = true;

            Attributes attributes = manifest.getMainAttributes();
            implementationVendor = getAttributeValueOrDefault(attributes, "Implementation-Vendor");
            implementationVersion = getAttributeValueOrDefault(attributes, "Implementation-Version");
            implementationTitle = getAttributeValueOrDefault(attributes, "Implementation-Title");
            scmRevision = getAttributeValueOrDefault(attributes, "SCM-Revision");
            scmTimestamp = getAttributeValueOrDefault(attributes, "SCM-Timestamp");
        }

        private void initializeDerivativeProperties() {
            fullVersionString = MessageFormat.format("v.{0} SVN r.{1}", implementationVersion, scmRevision);
            resourceVersion = implementationVersion.replace('.', '_') + "_" + scmRevision;
        }

        private Manifest readManifest() {
            ProtectionDomain domain = VersionBean.class.getProtectionDomain();
            if (domain != null) {
                CodeSource codeSource = domain.getCodeSource();
                if (codeSource != null) {
                    URL url = codeSource.getLocation();
                    if (url != null) {
                        JarInputStream jis = null;
                        try {
                            URLConnection urlConnection = url.openConnection();
                            urlConnection.setUseCaches(false);

                            if (urlConnection instanceof JarURLConnection) {
                                JarURLConnection jarUrlConnection = (JarURLConnection) urlConnection;
                                return jarUrlConnection.getManifest();
                            } else {
                                jis = new JarInputStream(urlConnection.getInputStream());
                                return jis.getManifest();
                            }
                        } catch (IOException e) {
                            LOGGER.error(MessageFormat.format("Error reading META-INF/MANIFEST.MF file: {0}", e.getMessage()), e);
                        } finally {
                            if (jis != null) {
                                try {
                                    jis.close();
                                } catch (IOException e) {
                                    LOGGER.error(MessageFormat.format("Error closing stream: {0}", e.getMessage()), e);
                                }
                            }
                        }
                    }
                }
            }

            return null;
        }

        boolean containsDataFromManifest() {
            return containsDataFromManifest;
        }

        public String getRevision() {
            return scmRevision;
        }

        public String getVersion() {
            return fullVersionString;
        }

        public String getResourceVersion() {
            return resourceVersion;
        }

        public String getImplementationTitle() {
            return implementationTitle;
        }

        public String getImplementationVendor() {
            return implementationVendor;
        }

        public String getImplementationVersion() {
            return implementationVersion;
        }

        public String getScmRevision() {
            return scmRevision;
        }

        public String getScmTimestamp() {
            return scmTimestamp;
        }

        @Override
        public String toString() {
            return getVersion();
        }
    }

    public static final Version VERSION = new Version();

    public String getVendor() {
        return VERSION.getImplementationVendor();
    }

    public String getTitle() {
        return VERSION.getImplementationTitle();
    }

    public String getRevision() {
        return VERSION.getScmRevision();
    }

    public String getTimestamp() {
        return VERSION.getScmTimestamp();
    }

    public Version getVersion() {
        return VERSION;
    }

    @Override
    public String toString() {
        if (VERSION.containsDataFromManifest()) {
            return getTitle() + " by " + getVendor() + ", version " + VERSION.toString();
        } else {
            return VERSION.toString();
        }
    }
}
