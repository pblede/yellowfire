package za.co.yellowfire.manager;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
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
import java.util.Map;

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

        SolrInputDocument doc = new SolrInputDocument();

        Searchable searchable = object.getClass().getAnnotation(Searchable.class);
        if (searchable == null) {
            System.out.println("The domain entity is not searchable " + object);
            return;
        }

        Field[] fields =  object.getClass().getDeclaredFields();
        for (Field field : fields) {
            SearchablePropertyId property = field.getAnnotation(SearchablePropertyId.class);
            if (property != null) {
                Object id = null;
                try {
                    field.setAccessible(true);
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
                    field.setAccessible(true);
                    doc.addField(name, field.get(object));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


        try {
            if (!doc.isEmpty()) {
                SolrPingResponse ping = getSolr().ping();
                System.out.println("Solr ping status = " + ping.getStatus());

                UpdateResponse response = getSolr().add(doc);
                if (response.getResponse() != null) {
                    System.out.println("Solr response:");
                    for (Map.Entry<String,Object> entry : response.getResponse()) {
                        System.out.println(entry.getKey() + " = " + entry.getValue());
                    }
                }
                getSolr().commit();
            } else {
                System.out.println("Not adding domain object because no searchable properties found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}


    public void onSearchableRemoved(@Observes @SearchableRemoved DomainObject object) {

        String id = null;

        Searchable searchable = object.getClass().getAnnotation(Searchable.class);
        if (searchable == null) {
            System.out.println("The domain entity is not searchable " + object);
            return;
        }

        Field[] fields =  object.getClass().getDeclaredFields();
        for (Field field : fields) {
            SearchablePropertyId property = field.getAnnotation(SearchablePropertyId.class);
            if (property != null) {
                Object value = null;
                try {
                    field.setAccessible(true);
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (value == null) {
                    System.out.println("The objects id is null " + object);
                    return;
                }

                id = searchable.name() + ":" + value.toString();
                break;
            }
        }

        try {
            if (id != null) {
                getSolr().deleteById(id);
                getSolr().commit();
            } else {
                System.out.println("Not deleting domain object because no searchable id property found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    @Override
    public Object search(Class domainClass, String value) {

        String query = new StringBuffer(1024).append("q=").append(value).append("&fl=*,score").toString();

        try {
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.add(CommonParams.Q, value);
            params.add(CommonParams.FL, "*,score");
            QueryResponse response = getSolr().query(params, SolrRequest.METHOD.GET);
            if (response != null) {
                SolrDocumentList docs = response.getResults();
                if (docs != null && !docs.isEmpty()) {
                    for (SolrDocument doc : docs) {
                        Object id = doc.get("id");
                        System.out.println("id = " + id);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
