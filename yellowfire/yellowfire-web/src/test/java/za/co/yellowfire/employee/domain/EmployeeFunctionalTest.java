package za.co.yellowfire.employee.domain;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.AbstractTestBase;
import za.co.yellowfire.common.domain.Address;
import za.co.yellowfire.common.domain.PersonName;
import za.co.yellowfire.common.domain.Title;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.domain.ChangeItem;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.notification.Notification;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.system.ApplicationManager;
import za.co.yellowfire.system.SetupManager;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@RunWith(Arquillian.class)
public class EmployeeFunctionalTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.TEST.getCategory());

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
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
                .addAsLibrary(new File("target/test-libs/liquibase-core.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-api.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-common.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-core.jar"))
                .addAsLibrary(new File("target/test-libs/picketlink-idm-spi.jar"))
                .addAsLibrary(new File("target/test-libs/commons-beanutils.jar"))
                .addAsLibrary(new File("target/test-libs/commons-logging.jar"))
                //.addAsLibrary(new File("target/test-libs/yellowfire-solarflare.jar"))
                .addAsManifestResource(new File("target/test-classes/META-INF/MANIFEST.MF"), "MANIFEST.MF")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource(new File("target/test-classes/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-mssql.xml"), "za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-mssql.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-h2.xml"), "za/co/yellowfire/setup/db_0_0_0/yellowfire-cde-0.0.0-h2.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_0_0/yellowfire-cde.xml"), "za/co/yellowfire/setup/db_0_0_0/yellowfire-cde.xml")
                .addAsResource(new File("target/classes/za/co/yellowfire/setup/db_0_1_0/yellowfire-cde.xml"), "za/co/yellowfire/setup/db_0_1_0/yellowfire-cde.xml")
                .addAsWebInfResource(new File("target/test-classes/web.xml"), "web.xml")
                .addAsWebInfResource(new File("target/test-classes/jboss-web.xml"), "jboss-web.xml");
    }

    @EJB
    private DomainManager domainManager;

    @Inject
    private SetupManager setupManager;

    @Test
    public void createEmployee() throws Exception {
        setupManager.executeInitial();
        setupManager.executeUpgrade();

        /*
         * Remove the employee record if it exists
         */
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Employee.FIELD_CODE, "1666");
        List<Employee> results = (List<Employee>) domainManager.query(Employee.QRY_EMPLOYEES_BY_CODE, params);
        for (Employee result : results) {
            LOGGER.info("Deleting employee : {}", result);
            domainManager.remove(result);
        }

        /*
         * Remove the profile record for user marka
         */
        params = new HashMap<String, Object>(1);
        params.put(Profile.FIELD_USER_NAME, "marka");
        Iterable<Profile> profiles = (List<Profile>) domainManager.query(Profile.QRY_USER_NAME, params);
        for (Profile profile : profiles) {
            LOGGER.info("Deleting profile : {}", profile);
            domainManager.remove(profile);
        }
        
        Employee employee = new Employee();

        /*
         * Employee.profile is not marked CASCADE PERSIST so the change item tracking is required
         */
        employee.track();
        
        employee.setCode("1666");
        employee.setJoined(new SimpleDateFormat("yyyy-MM-dd").parse("2008-11-01"));
                
        Profile profile =
                new Profile(
                        "marka",
                        "password1*",
                        "7405315048086",
                        "mp.ashworth@gmail.com",
                        new PersonName(
                                "Mark",
                                "Ashworth",
                                Title.Mr,
                                null),
                        new Address("line01", "line02", "line03", "1618"),
                        new Address("line11", "line12", "line13", "2007"));
        employee.setProfile(profile);

        if (employee.getId() == null) {
            domainManager.persist(employee.getProfile());
            domainManager.persist(employee);
        } else {
            if (employee.getProfile() != null) {
                List<ChangeItem> changeItems = employee.getProfile().changes();
                if (changeItems.size() > 0) {
                    employee.setProfile((Profile) domainManager.merge(Profile.class, employee.getProfile().getUserId(), changeItems));
                }
            }
        }

        /*
         * Test that all the properties were persisted correctly
         */
        params = new HashMap<String, Object>(1);
        params.put(Employee.FIELD_CODE, "1666");
        results = (List<Employee>) domainManager.query(Employee.QRY_EMPLOYEES_BY_CODE, params);
        
        Assert.assertNotNull(results == null);
        Assert.assertEquals("There should be 1 employee with code 1666", 1, results.size());
        
        Employee result = results.get(0);
        
        Assert.assertEquals("The employee.code should be 1666", "1666", result.getCode());
        Assert.assertEquals("The employee.joined should be 2008-11-01", new SimpleDateFormat("yyyy-MM-dd").parse("2008-11-01"), result.getJoined());
        Assert.assertNull("The employee.terminated should be null", result.getTerminated());
        Assert.assertNull("The employee.terminationReason should be null", result.getTerminationReason());

    }

    @Test
    public void updateEmployee() throws Exception {
        setupManager.executeInitial();
        setupManager.executeUpgrade();

        /*
         * Remove the employee record if it exists
         */
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Employee.FIELD_CODE, "1777");
        List<Employee> employees = (List<Employee>) domainManager.query(Employee.QRY_EMPLOYEES_BY_CODE, params);
        for (Employee result : employees) {
            LOGGER.info("Deleting employee : {}", result);
            domainManager.remove(result);
        }

        /*
         * Remove the profile record for users somea and natasjao
         */
        params = new HashMap<String, Object>(1);
        params.put(Profile.FIELD_USER_NAMES, Arrays.asList("somea", "natasjao"));
        List<Profile> profiles = (List<Profile>) domainManager.query(Profile.QRY_USER_NAMES, params);
        for (Profile profile : profiles) {
            LOGGER.info("Deleting profile : {}", profile);
            domainManager.remove(profile);
        }

        Employee employee = new Employee();

        /*
         * Persist employee for the first time
         */
        employee.setCode("1777");
        employee.setJoined(new SimpleDateFormat("yyyy-MM-dd").parse("2008-11-01"));

        Profile profile =
                new Profile(
                        "somea",
                        "password1*",
                        "7405315048086",
                        "mp.ashworth@gmail.com",
                        new PersonName(
                                "Mark",
                                "Ashworth",
                                Title.Mr,
                                null),
                        new Address("line01", "line02", "line03", "1618"),
                        new Address("line11", "line12", "line13", "2007"));
        employee.setProfile(profile);

        domainManager.persist(employee.getProfile());
        domainManager.persist(employee);

        
        /*
         * Retrieve employee
         */
        params = new HashMap<String, Object>(1);
        params.put(Employee.FIELD_CODE, "1777");
        employees = (List<Employee>) domainManager.query(Employee.QRY_EMPLOYEES_BY_CODE, params);
        Assert.assertNotNull("Employees results from query should not be null", employees);
        Assert.assertEquals("There should be one employee with the code 1777", 1, employees.size());

        LOGGER.info("Getting employee from employees list");
        employee = employees.get(0);
        LOGGER.info("Tracking changes");
        employee.track();
        LOGGER.info("Setting employee properties");
        employee.setJoined(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-01"));
        employee.getProfile().getName().setName("Natasja");
        employee.getProfile().getName().setSurname("Olivier");
        employee.getProfile().setIdNumber("7501100987081");
        employee.getProfile().setEmail("natsoli4@gmail.com");
        employee.getProfile().setUserName("natasjao");
        employee.getProfile().setPassword("password2*");

        if (employee.getProfile() != null) {
            List<ChangeItem> changeItems = employee.getProfile().changes();
            LOGGER.info("Profile change item {}", changeItems);
            Assert.assertEquals("There should be 6 profile changes", 6, changeItems.size());
            if (changeItems.size() > 0) {
                LOGGER.info("Merging profile...");
                employee.setProfile((Profile) domainManager.merge(Profile.class, employee.getProfile().getUserId(), changeItems));
            }
        }
        LOGGER.info("Merging employee...");
        domainManager.merge(employee);


        /*
         * Test that all the properties were persisted correctly
         */
        params = new HashMap<String, Object>(1);
        params.put(Employee.FIELD_CODE, "1777");
        employees = (List<Employee>) domainManager.query(Employee.QRY_EMPLOYEES_BY_CODE, params);

        Assert.assertNotNull(employees);
        Assert.assertEquals("There should be 1 employee with code 1777", 1, employees.size());

        Employee result = employees.get(0);

        Assert.assertEquals("The employee.code should be 1777", "1777", result.getCode());
        Assert.assertEquals("The employee.joined should be 2010-11-01", new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-01"), result.getJoined());
        Assert.assertNull("The employee.terminated should be null", result.getTerminated());
        Assert.assertNull("The employee.terminationReason should be null", result.getTerminationReason());
        Assert.assertEquals("The employee.profile.userName should be natasjao", "natasjao", result.getProfile().getUserName());

    }
}
