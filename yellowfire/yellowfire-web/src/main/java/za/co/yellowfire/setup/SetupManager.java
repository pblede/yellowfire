package za.co.yellowfire.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;

/**
 * @author Mark P Ashworth
 */
@Stateless
public class SetupManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.SETUP.getCategory());

    @Resource(mappedName = "yellowfire/ds")
    private DataSource dataSource;

    protected boolean hasVersion(Connection connection) throws SQLException {
        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTables(null, null, "version", null);
            return (rs != null && rs.next());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.warn("Unable to close result after trying to determine the version");
                }
            }
        }
    }
    
    protected Version retrieveVersion(Connection connection) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT version from cde.version");
            if (rs != null && rs.next()) {
                return new Version(rs.getString("version"));
            }
            return new Version();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.warn("Unable to close result after trying to retrieve the version");
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException x) {
                    LOGGER.warn("Unable to close statement after trying to retrieve the version");
                }
            }
        }
    }

    public Manifest getManifest() throws SQLException {
        Connection connection = null;
        
        try {
            Manifest manifest = new Manifest();
            connection = dataSource.getConnection();
            LOGGER.info("Determining if the database has version information");
            if (hasVersion(connection)) {
                LOGGER.info("Reading manifest version and database version");
                manifest.setDatabaseVersion(retrieveVersion(connection));
            } else {
                manifest.setDatabaseVersion(null);
            }
            return manifest;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException x) {
                    LOGGER.warn("Unable to close connection after trying to retrieve the database version information");
                }
            }
        }
    }

    public boolean isSetupRequired(Manifest manifest) throws SQLException {

        if (manifest == null) throw new IllegalArgumentException("The manifest may not be null");

        LOGGER.info("Checking is implementation version {} is greater than database version {}", manifest.getImplementationVersion(), manifest.getDatabaseVersion());
        if (manifest.getImplementationVersion().compareTo(manifest.getDatabaseVersion()) > 0) {
            LOGGER.info("Implementation is greater than database");
            return true;
        }
        return false;
    }
}
