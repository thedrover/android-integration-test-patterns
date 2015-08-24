package thedrover.androidapiintegration;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

/**
 * {@link org.apache.http.client.HttpClient} has been deprecated in the Android SDK. This test pattern may provde a useful
 * approach to create tests and migrate to an alternative.
 *
 * Created by dougwright on 4/04/2015.
 */
public class HttpClientRequestor extends HttpRequestor {
  private static final String cHttp = "http";
  private static final String cHttps = "https";
  private static final int cHttpPort = 80;
  private static final int cHttpsPort = 443;

  // The timeout in milliseconds to wait until a connection is established.
  private static final int cTimeoutConnection = 60000;

  // The timeout in milliseconds to wait for data (socket timeout SO_TIMEOUT)
  private static final int cTimeoutSocket = 60000;


  private AbstractHttpClient mHttpClient;

  private ExecutorService mPostConnectionThreadPool;
  private ExecutorService mGetConnectionThreadPool;

  public HttpClientRequestor() {


    SchemeRegistry lSchemeRegistry = new SchemeRegistry();
    lSchemeRegistry.register(new Scheme(cHttp, PlainSocketFactory.getSocketFactory(), cHttpPort));
    lSchemeRegistry.register(new Scheme(cHttps, SSLSocketFactory.getSocketFactory(), cHttpsPort));

    HttpParams lParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(lParams, cTimeoutConnection);
    HttpConnectionParams.setSoTimeout(lParams, cTimeoutSocket);
    ClientConnectionManager lConnMgr = new ThreadSafeClientConnManager(lParams, lSchemeRegistry);
    mHttpClient = new DefaultHttpClient(lConnMgr, lParams);
    mHttpClient.setReuseStrategy(new NoConnectionReuseStrategy());

    mPostConnectionThreadPool = Executors.newFixedThreadPool(2);
    mGetConnectionThreadPool = Executors.newFixedThreadPool(2);
  }

  @Override
  public void makeRequest(String s_url) {
  // TODO add HTTP method
  makeGetRequest(s_url);


  }

  /**
   * Make a GET request to the Incoming API; the response will be automatically
   * decoded and broadcast using the event defined in the
   *
   *          Incoming API request
   */
  public void makeGetRequest(final String s_url) {
    mGetConnectionThreadPool.submit(new Runnable() {

      @Override
      public void run() {
        try {
          makeGetRequestSync(s_url);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }

  /**
  *
  * @throws IOException
  * @throws ClientProtocolException
  * @throws IllegalAccessException
  * @throws InstantiationException
  */
  private void makeGetRequestSync(String s_url) throws IOException, ClientProtocolException,
      InstantiationException, IllegalAccessException {

    //EventManager eventManager = (EventManager) ServiceBroker.getInstance().getService(EventManager.class);

    InputStream response = null;

    try {

      // Multiple requests and responses....how do we manage each....

      // thread pool of http connections.
      // can have multiple zotero "handler" threads. Each of these processes a
      // request. Response is put on outgoing queue. Outgoing queue can be
      // consumer by http responses or a local handler in code. !!!

      Header[] headers = { new BasicHeader("Accept-Encoding", "gzip"), };// {
      // new
      // BasicHeader("Zotero-API-Version",
      // "2"),
      // };

      HttpRequestBase httpRequest = new HttpGet(s_url);

      // httpRequest.

//      if (LogIncoming.SHOW_DEBUG) {
//        LogIncoming.i(TAG, "Request: " + httpRequest);
//
//      }

      response = makeRequest(headers, httpRequest);

      Object responsePayload = null;

//      Class<? extends Encodable> responseType = request.getResponseType();
//      if (JSONResponse.class.equals(responseType)) {
//        responsePayload = HttpRestClient.convertStreamToString(response);
//      } else {
//        responsePayload = EncodingUtils.deserialize(response, responseType);
//      }
//
//      if (LogIncoming.SHOW_DEBUG) {
//        LogIncoming.d(TAG, "Response: " + responsePayload);
//
//      }
//
//      eventManager.postEvent(request.getEventService(), request.getSuccessEventAction(), responsePayload);

    } catch (HttpResponseException e) {

//      if (LogIncoming.SHOW_DEBUG) {
//        LogIncoming.w(TAG, "http response error " + e.getStatusCode() + "  " + e.getMessage(), e);
//      }
//      ErrorResponse errResponse = new ErrorResponse(e.getStatusCode(), e.getMessage());
//
//      eventManager.postEvent(request.getEventService(), request.getErrorAction(), errResponse);
    } finally {
      if (response != null) {
        try {
          response.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }

  }

  /**
   * @param headers
   *          adds headers, may already have a gzip request.
   * @param httpRequest
   * @return
   * @throws IOException
   * @throws ClientProtocolException
   */
  private InputStream makeRequest(Header[] headers, HttpRequestBase httpRequest) throws IOException,
      ClientProtocolException, HttpResponseException {

    for (Header header : headers) {
      if (header != null) {
        httpRequest.addHeader(header);
      }
    }


    // The underlying HTTP connection is still held by the response
    // object
    // to allow the response content to be streamed directly from the
    // network socket.
    // In order to ensure correct deallocation of system resources
    // the user MUST either fully consume the response content or abort
    // request
    // execution by calling HttpGet#releaseConnection().
    // sLogger.info("Making request " + httpRequest);

    HttpResponse response = mHttpClient.execute(httpRequest);
    InputStream instream = null;
    int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode != HttpStatus.SC_OK) {

      instream = response.getEntity().getContent();
      StringBuilder msg = new StringBuilder();
      if (instream != null) {
        BufferedReader r = new BufferedReader(new InputStreamReader(instream));

        String line;
        while ((line = r.readLine()) != null) {
          msg.append(line);
        }
       System.out.println("Error response message: " + msg.toString());
      }


      throw new HttpResponseException(statusCode, msg.toString());
    } else {
      System.out.println("SUCCESS");
      // Success
      instream = response.getEntity().getContent();
      Header contentEncoding = response.getFirstHeader("Content-Encoding");
      if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
        instream = new GZIPInputStream(instream);
      }
      mResultHandler.onSuccess();
    }
    return instream;

  }


}
