package za.co.yellowfire.manager;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.search.Searchable;
import za.co.yellowfire.domain.search.SearchableProperty;
import za.co.yellowfire.domain.search.SearchablePropertyId;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local
@Singleton(name = "SearchManager")
public class SearchManagerBean implements SearchManager {
    private static final long serialVersionUID = 1L;

    private SolrServer solr;

    @PostConstruct
    public void init() {

//        CompassSession session = null;
//
//        try {
//            session = getCompass().openSession();
//
//            List<DomainObject> values = (List <DomainObject>) manager.query(Venue.QRY_VENUES, null);
//            for (DomainObject value : values) {
//                session.save(value);
//            }
//        } catch (Exception e) {
//            try { if (session != null) session.close(); } catch (Exception x) { /*Ignore*/ }
//
//            LOGGER.error("Unable to index existing data in the database", e);
//        }

        try {
            getSolr();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

//    private Compass getCompass() {
//        return org.compass.gps.device.jpa.embedded.eclipselink.EclipseLinkHelper.getCompass(JpaHelper.getEntityManager(em));
//    }

    public SolrServer getSolr() throws MalformedURLException {
        if (solr == null) {
            solr = new CommonsHttpSolrServer("http://localhost:8080/solr");
        }
        return solr;
    }

    public void onSearchableAdded(@Observes @SearchableAdded DomainObject object) {
//		LOGGER.info("onSearchableAdded : {}", object);
//        CompassSession session = null;
//        try {
//            session = getCompass().openSession();
//		    session.save(object);
//        } finally {
//            try {if (session != null) session.close();} catch (Exception e) { /*Ignore*/ }
//        }



        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        SolrInputDocument doc = new SolrInputDocument();

        Searchable searchable = object.getClass().getAnnotation(Searchable.class);
        if (searchable == null) {
            System.out.println("The domain entity is not searchable " + object);
            return;
        }

        Field[] fields =  object.getClass().getFields();
        for (Field field : fields) {
            SearchablePropertyId property = field.getAnnotation(SearchablePropertyId.class);
            if (property != null) {
                Object id = null;
                try {
                    id = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (id == null) {
                    System.out.println("The objects id is null " + object);
                    return;
                }

                doc.addField("id", searchable.name() + ":" + id.toString());
                break;
            }
        }

        for (Field field : fields) {
            SearchableProperty property = field.getAnnotation(SearchableProperty.class);
            if (property != null) {

                String name = property.name();
                if (name.equals("")) {
                    name = field.getName();
                }

                try {
                    doc.addField(name, field.get(object));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            getSolr().add(docs);
            getSolr().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}


    public void onSearchableRemoved(@Observes @SearchableRemoved DomainObject object) {
//		LOGGER.info("onSearchableRemoved : {}", object);

//        CompassSession session = null;
//        try {
//            session = getCompass().openSession();
//		    session.delete(object);
//        } finally {
//            try {if (session != null)session.close();} catch (Exception e) { /*Ignore*/ }
//        }
	}

    @Override
    public Object search(String value) {
//        System.out.println("3 = " + 3);
//        CompassSearchSession session = null;
//        try {
//            session = getCompass().openSearchSession();
//            System.out.println("4 = " + 4);
//            CompassHits hits = session.find(value);
//            return hits.detach();
//        } finally {
//            if (session != null) {
//                try {session.close();} catch (Exception e) { /*Ignore*/ }
//            }
//        }
        return null;
    }


}
