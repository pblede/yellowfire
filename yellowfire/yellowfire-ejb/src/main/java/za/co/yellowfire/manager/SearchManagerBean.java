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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.DomainObject;
import za.co.yellowfire.domain.search.Searchable;
import za.co.yellowfire.domain.search.SearchableProperty;
import za.co.yellowfire.domain.search.SearchablePropertyId;
import za.co.yellowfire.domain.search.SearchablePropertyType;
import za.co.yellowfire.log.LogType;

import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local
@Singleton(name = "SearchManager")
public class SearchManagerBean implements SearchManager {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.SEARCH.getCategory());
    
    private String url = "http://localhost:8080/solr";
    private SolrServer solr;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SolrServer getSolr() throws MalformedURLException {
        if (solr == null) {
            solr = new CommonsHttpSolrServer(getUrl());
        }
        return solr;
    }

    public void onSearchableAdded(@Observes @SearchableAdded DomainObject object) {

        SolrInputDocument doc = new SolrInputDocument();

        Searchable searchable = object.getClass().getAnnotation(Searchable.class);
        if (searchable == null) {
            LOGGER.info("The domain entity is not searchable " + object);
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
                    LOGGER.error("Searchable property id on field {} is unaccessable", field);
                }

                if (id == null) {
                    LOGGER.warn("The objects id is null {} ",object);
                    return;
                }

                doc.addField("id", searchable.name() + ":" + id.toString());
                doc.addField("class", object.getClass().getName());
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

                /*
                If the property is dynamic then the property name
                needs to be appended with one of the Solr extentions
                to indicate that it is dynamic
                <dynamicField name="*_i"  type="int"    indexed="true"  stored="true"/>
                <dynamicField name="*_s"  type="string"  indexed="true"  stored="true"/>
                <dynamicField name="*_l"  type="long"   indexed="true"  stored="true"/>
                <dynamicField name="*_t"  type="text"    indexed="true"  stored="true"/>
                <dynamicField name="*_txt" type="text"    indexed="true"  stored="true" multiValued="true"/>
                <dynamicField name="*_b"  type="boolean" indexed="true"  stored="true"/>
                <dynamicField name="*_f"  type="float"  indexed="true"  stored="true"/>
                <dynamicField name="*_d"  type="double" indexed="true"  stored="true"/>
                <dynamicField name="*_coordinate"  type="tdouble" indexed="true"  stored="false"/>
                <dynamicField name="*_dt" type="date"    indexed="true"  stored="true"/>
                <dynamicField name="*_p"  type="location" indexed="true" stored="true"/>
                */
                if (property.dynamic()) {
                    SearchablePropertyType type = property.type();

                    if (type == SearchablePropertyType.INFER) {
                        Class clazz = field.getClass();
                        if (clazz.isAssignableFrom(Integer.class)) { type = SearchablePropertyType.INT; }
                        else if (clazz.isAssignableFrom(String.class)) { type = SearchablePropertyType.STRING; }
                        else if (clazz.isAssignableFrom(Long.class)) { type = SearchablePropertyType.LONG; }
                        else if (clazz.isAssignableFrom(Boolean.class)) { type = SearchablePropertyType.BOOLEAN; }
                        else if (clazz.isAssignableFrom(Float.class)) { type = SearchablePropertyType.FLOAT; }
                        else if (clazz.isAssignableFrom(Double.class)) { type = SearchablePropertyType.DOUBLE; }
                        else if (clazz.isAssignableFrom(Date.class)) { type = SearchablePropertyType.DATE; }
                        else if (clazz.isAssignableFrom(java.sql.Date.class)) { type = SearchablePropertyType.DATE; }
                        else { type = SearchablePropertyType.MULTIVALUED_TEXT; }
                    }

                    switch (type) {
                    case INT: name += "_i"; break;
                    case STRING: name += "_s"; break;
                    case LONG: name += "_l"; break;
                    case TEXT: name += "_t"; break;
                    case MULTIVALUED_TEXT: name += "_txt"; break;
                    case BOOLEAN: name += "_b"; break;
                    case FLOAT: name += "_f"; break;
                    case DOUBLE: name += "_d"; break;
                    case COORDINATE: name += "_coordinate"; break;
                    case DATE: name += "_dt"; break;
                    case LOCATION: name += "_p"; break;
                    }
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
