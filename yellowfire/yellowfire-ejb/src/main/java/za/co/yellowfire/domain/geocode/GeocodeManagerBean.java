package za.co.yellowfire.domain.geocode;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local(GeocodeManager.class)
@Stateless(
  name = "GeocodeManager",
  mappedName = "bluefire/session/GeocodeManager",
  description = "Manages the geocode related queries")
public class GeocodeManagerBean implements GeocodeManager {
    @Override
    public GeocodeResponse findAddress(String search) throws Exception {
        return loadRemote(search);
    }

    private GeocodeResponse loadRemote(String search) throws GeocodeException {

        DefaultHttpClient client = new DefaultHttpClient();

        try {
            search = search.replace(' ', '+');
            //client.getCredentialsProvider().setCredentials(
            //        AuthScope.ANY,
            //        new NTCredentials("MarkA", "password6*", "L01MarkA", "jse.co.za"));

            //HttpHost proxy = new HttpHost("172.17.20.150", 8080);
            //client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            HttpContext localContext = new BasicHttpContext();

            HttpHost target = new HttpHost("maps.googleapis.com", 80, "http");
            HttpGet req = new HttpGet("/maps/api/geocode/xml?address="+search+"&sensor=true");

            HttpResponse rsp = client.execute(target, req, localContext);
            HttpEntity entity = rsp.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(rsp.getStatusLine());
            Header[] headers = rsp.getAllHeaders();
            for (Header header : headers) {
                System.out.println(header);
            }
            System.out.println("----------------------------------------");

            if (entity != null) {
                return load(new InputStreamReader(entity.getContent()));
            } else {
                throw new GeocodeException("Unable to load remote geocode xml because response is null");
            }
        } catch (Exception e) {
            throw new GeocodeException("Unable to find address", e);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    @SuppressWarnings("unused")
    private GeocodeResponse loadLocal() throws GeocodeException {

        try {
            URL url = GeocodeResponse.class.getResource("za/co/bluefire/domain/geocode/geocode-02.xml");
            if (url == null) {
                url = GeocodeResponse.class.getResource("/za/co/bluefire/domain/geocode/geocode-02.xml");
            }
            if (url == null) {
                throw new GeocodeException("Unable to load local geocode xml");
            }
            return load(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            throw new GeocodeException("Unable to unmarshal geocode response", e);
        }
    }
    
    private GeocodeResponse load(Reader reader) throws GeocodeException {
        try {
            Class[] classes = new Class[1];
            classes[0] = GeocodeResponse.class;
            JAXBContext context = JAXBContext.newInstance(classes);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (GeocodeResponse) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new GeocodeException("Unable to unmarshal geocode response", e);
        }
    }
}
