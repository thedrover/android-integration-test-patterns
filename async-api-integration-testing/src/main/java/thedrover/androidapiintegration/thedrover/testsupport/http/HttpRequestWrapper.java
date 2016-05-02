package thedrover.androidapiintegration.thedrover.testsupport.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple wrapper that provides a consistent interface for using HttpURLConnection,
 * OK HTTP, or HTTPClient to make requests.
 */
public abstract class HttpRequestWrapper {

  protected HttpResult mResultHandler;
  protected ExecutorService mGetConnectionThreadPool;

  public HttpRequestWrapper() {
    mGetConnectionThreadPool = Executors.newFixedThreadPool(2);
  }

  /**
   * Make asynchronous HTTP request.
   *  @param url
   *
   */
  protected abstract void makeRequest(String url);

  /**
   *
   * @param url
   * @param resultHandler
   */
  public void makeRequestOnThread(final String url, HttpResult resultHandler) {
    mResultHandler = resultHandler;
    mGetConnectionThreadPool.submit(new Runnable() {

      @Override
      public void run() {
        makeRequest(url);
      }
    });
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
