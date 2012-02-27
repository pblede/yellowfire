package za.co.yellowfire.system;

import liquibase.exception.LiquibaseException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import za.co.yellowfire.AbstractTestBase;
import za.co.yellowfire.common.domain.PersonName;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.notification.Notification;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.employee.domain.Employee;
import za.co.yellowfire.manager.DomainManager;

import javax.inject.Inject;
import java.io.File;
import java.sql.SQLException;

/**
 *
 */
@RunWith(Arquillian.class)
public class ApplicationManagerTest {

    @Deployment
    public static WebArchive createDeployment() {
        System.out.println("createDeployment");
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                /*common.log*/
                .addPackage(LogType.class.getPackage())
                /*system*/
                .addPackage(ApplicationManager.class.getPackage())
                /*manager*/
                .addPackage(DomainManager.class.getPackage())
                /*domain*/
                .addPackage(DomainObject.class.getPackage())
                .addPackage(Notification.class.getPackage())
                /*common.domain*/
                .addPackage(PersonName.class.getPackage())
                /*employee.domain*/
                .addPackage(Employee.class.getPackage())
                /*profile*/
                .addPackage(Profile.class.getPackage())
                /*test*/
                .addPackage(AbstractTestBase.class.getPackage())
                .addPackage(DomainManager.class.getPackage())
                .addAsLibrary(new File("target/test-libs/liquibase-core.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-api.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-common.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-core.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-spi.jar"))
                .addAsLibrary(new File("target/test-libs/commons-beanutils.jar"))
                .addAsLibrary(new File("target/test-libs/commons-logging.jar"))
                .addAsManifestResource(new File("target/test-classes/META-INF/MANIFEST.MF"), "MANIFEST.MF")
                .addAsResource(new File("target/test-classes/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-mssql.xml"), "za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-mssql.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-h2.xml"), "za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-h2.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_0_0/yellowfire-cde.xml"), "za/co/yellowfire/setup/db_0_0_0/yellowfire-cde.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_1_0/yellowfire-cde.xml"), "za/co/yellowfire/setup/db_0_1_0/yellowfire-cde.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("target/test-classes/jboss-web.xml"), "jboss-web.xml");
        return war;
    }

    @Inject
    ApplicationManager manager;

    @Inject
    SetupManager setupManager;

    @Test
    public void implementationVersion() {
        Manifest manifest = manager.getManifest();
        Version version = manifest.getImplementationVersion();
        Assert.assertEquals("Major should be 0", 0, version.getMajor());
        Assert.assertEquals("Minor should be 1", 1, version.getMinor());
        Assert.assertEquals("Revision should be 0", 0, version.getRevision());
    }


    public void upgrade() throws SQLException, LiquibaseException {
        setupManager.executeInitial();
        setupManager.executeUpgrade();
    }
}
