package thedrover.androidapiintegration.thedrover.testsupport.http;

/**
 * Represents the result of the asynchronous HTTP request.
 */
public class HttpResult {

  private int mResponseCode = -1;
  private String mPayload;

  public void onResult(int responseCode, String payload) {
    mResponseCode = responseCode;
    mPayload = payload;
  }

  public void onResult(Exception e) {
   mPayload = e.getMessage();
  }

  public String getPayload() {
    return mPayload;
  }


  /**
   * @return the HTTP response code or -1 if the response is not complete.
   */
  public int getResponseCode() {
    return mResponseCode;
  }

}
