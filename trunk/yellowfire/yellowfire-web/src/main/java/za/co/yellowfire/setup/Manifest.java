package za.co.yellowfire.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

/**
 * Class for incapsulate version info.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: nbelaevski $)
 * @version $Revision: 17455 $ $Date: 2010-06-01 16:36:23 -0400 (Tue, 01 Jun 2010) $
 *
 * Changes the version default and the version class from String to Version
 * @author Mark P Ashworth
 * @version 0.1
 */
public class Manifest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    private static final String UNKNOWN = "";
    private static final Version UNKNOWN_VERSION = new Version(0, 0, 0);

    private String implementationVendor = UNKNOWN;

    private Version databaseVersion = UNKNOWN_VERSION;

    private Version implementationVersion = UNKNOWN_VERSION;

    private String implementationTitle = UNKNOWN;

    private String scmRevision = UNKNOWN;

    private String scmTimestamp = UNKNOWN;

    private String fullVersionString = UNKNOWN;

    private String resourceVersion = UNKNOWN;

    private boolean containsDataFromManifest = false;

    public Manifest() {
        initialize();
    }

    private String getAttributeValueOrDefault(Attributes attributes, String name) {
        String value = attributes.getValue(name);
        if (value == null) {
            value = UNKNOWN;
        }

        return value;
    }

    private Version getVersionValueOrDefault(Attributes attributes, String name) {
        String value = getAttributeValueOrDefault(attributes, name);
        LOGGER.info("Version retrieved from manifest is {}", value == null ? "null" : value);
        if (value == null) {
            return UNKNOWN_VERSION;
        } else if (value.equals(UNKNOWN)) {
            return UNKNOWN_VERSION;
        }

        String[] parts = value.split("\\.");
        int[] info = new int[] {0, 0, 0};
        for (int i = 0; i < parts.length && i < info.length; i++) {
            try {
                info[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse version at index {} for version {}", i, value);
            }
        }
        return new Version(info);
    }
    
    private void initialize() {
        java.util.jar.Manifest manifest = null;
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

    private void initializePropertiesFromManifest(java.util.jar.Manifest manifest) {
        containsDataFromManifest = true;

        Attributes attributes = manifest.getMainAttributes();
        implementationVendor = getAttributeValueOrDefault(attributes, "Implementation-Vendor");
        implementationVersion = getVersionValueOrDefault(attributes, "Implementation-Version");
        implementationTitle = getAttributeValueOrDefault(attributes, "Implementation-Title");
        scmRevision = getAttributeValueOrDefault(attributes, "SCM-Revision");
        scmTimestamp = getAttributeValueOrDefault(attributes, "SCM-Timestamp");
    }

    private void initializeDerivativeProperties() {
        fullVersionString = MessageFormat.format("v.{0} SVN r.{1}", implementationVersion.toString(), scmRevision);
        resourceVersion = implementationVersion.toString().replace('.', '_') + "_" + scmRevision;
    }

    private java.util.jar.Manifest readManifest() {
        System.out.println("LOGGER = " + Manifest.class.getPackage().getImplementationVersion());

        ProtectionDomain domain = Manifest.class.getProtectionDomain();
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
                            java.util.jar.Manifest manifest = jis.getManifest();
                            if (manifest != null) {
                                return manifest;
                            }
                            
                            String location = url.toExternalForm();
                            int index = location.indexOf("WEB-INF/classes");
                            if (index > -1) {
                                location = location.substring(0, index + "WEB-INF/classes".length() + 1);
                                location = location + "META-INF/MANIFEST.MF";
                                InputStream is = null;
                                try {
                                    is = new URL(location).openStream();
                                    manifest = new java.util.jar.Manifest();
                                    manifest.read(is);
                                    return manifest;
                                } catch (Exception e) {
                                    LOGGER.error(MessageFormat.format("Error reading WEB-INF/classes/META-INF/MANIFEST.MF file: {0}", e.getMessage()), e);
                                } finally {
                                    try {
                                        if (is != null)
                                            is.close();
                                    } catch (Exception e) {
                                        LOGGER.error(MessageFormat.format("Error closing stream: {0}", e.getMessage()), e);
                                    }
                                }
                                
                            }
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

    public Version getImplementationVersion() {
        return implementationVersion;
    }

    public void setDatabaseVersion(Version version) {
        this.databaseVersion = version;
    }

    public Version getDatabaseVersion() {
        return databaseVersion;
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