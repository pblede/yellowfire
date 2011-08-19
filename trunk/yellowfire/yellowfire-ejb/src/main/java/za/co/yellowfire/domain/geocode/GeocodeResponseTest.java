package za.co.yellowfire.domain.geocode;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class GeocodeResponseTest {

    public static void main(String[] args) throws Exception {
        Geocode();
        GeocodeManagerBean bean = new GeocodeManagerBean();
        GeocodeResponse response = bean.findAddress("");
    }

    public static void Geocode() {
        //http://maps.googleapis.com/maps/api/geocode/xml?address=9+Brendon+Street&sensor=true


        DefaultHttpClient client = new DefaultHttpClient();

        try {
            //client.getCredentialsProvider().setCredentials(
            //        AuthScope.ANY,
            //        new NTCredentials("MarkA", "password6*", "L01MarkA", "jse.co.za"));

            //HttpHost proxy = new HttpHost("172.17.20.150", 8080);
            //client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

            HttpContext localContext = new BasicHttpContext();

            HttpHost target = new HttpHost("maps.googleapis.com", 80, "http");
            HttpGet req = new HttpGet("/maps/api/geocode/xml?address=9+Brendon+Street&sensor=true");

            //System.out.println("executing request to " + target + " via " + proxy);
            HttpResponse rsp = client.execute(target, req, localContext);
            HttpEntity entity = rsp.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(rsp.getStatusLine());
            Header[] headers = rsp.getAllHeaders();
            for (int i = 0; i<headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }
        } catch (Exception e) {

            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();

        } finally {

            // Release the connection.
            client.getConnectionManager().shutdown();

        }
    }
}
