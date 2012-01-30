package za.co.yellowfire.training.domain;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.training.TrainingCourse;
import za.co.yellowfire.log.LogType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@RunWith(Arquillian.class)
public class CoursePersistenceGlassfishTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.TEST.getCategory());

    @PersistenceContext(unitName = "yellowfire")
    private EntityManager em;

    @Deployment(name = "weld-se-embedded-1.1")
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "test.war")
                .addPackage(LogType.class.getPackage())
                .addPackage(DomainEntity.class.getPackage())
                .addPackage(TrainingCourse.class.getPackage())
                .addAsManifestResource(
                        new ByteArrayAsset("<beans />".getBytes()),
                        ArchivePaths.create("beans.xml"))
                .addAsManifestResource("glassfish-persistence.xml", ArchivePaths.create("persistence.xml"));

//        MavenDependencyResolver builder = new MavenBuilderImpl();
//        builder.configureFrom("C:\\SDE\\modules\\apache-maven-3.0.3\\conf\\settings.xml");
//        builder.loadMetadataFromPom("pom.xml");
//        File[] files = builder.artifacts(
//                "commons-collections:commons-collections:3.2.1",
//                "commons-io:commons-io:2.1",
//                "org.apache.commons:commons-lang3:3.0.1",
//                "org.jboss.solder:solder-impl:3.1.0.Final",
//                "joda-time:joda-time:2.0",
//                "org.slf4j:slf4j-api:1.5.6",
//                "org.slf4j:slf4j-jdk14:1.5.6",
//                "org.slf4j:log4j-over-slf4j:1.5.6",
//                "org.slf4j:jcl-over-slf4j:1.5.6",
//                "org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-api:1.1.0-alpha-2",
//                "org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-api-maven:1.1.0-alpha-2",
//                "org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-impl-maven:1.1.0-alpha-2",
//                "org.liquibase:liquibase-core:2.0.3",
//                "org.eclipse.persistence:org.eclipse.persistence.moxy:2.3.1",
//                "org.hibernate:hibernate-validator:4.2.0.Final",
//                "org.picketlink.idm:picketlink-idm-api:1.5.0.Alpha02"
//        ).resolveAsFiles();
//
//        for (File file : files) {
//            war.addAsLibraries(file);
//        }

//        System.out.println("war = " + war.toString(true));
//
//        Map<ArchivePath, Node> contents = war.getContent();
//        for (ArchivePath archivePath : contents.keySet()) {
//            if (archivePath.get().contains("persistence.xml")) {
//                FileAsset fileAsset = (FileAsset) contents.get(archivePath).getAsset();
//            }
//        }
//        return war;
    }

    @Test
    public void testInsertUpdate() {
        List results = em.createQuery("SELECT c from TrainingCourse c").getResultList();
        for (Object result : results) {
            LOGGER.info("Course {}", result);
        }
    }
}