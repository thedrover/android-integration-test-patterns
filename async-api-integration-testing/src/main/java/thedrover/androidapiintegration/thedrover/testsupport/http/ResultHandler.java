package thedrover.androidapiintegration.thedrover.testsupport.http;

/**
 * Represents the class that processes the results of the asynchronous operation. This is a test-instrumented version, that might an event
 * listener/subscriber, or it might injected into the test setup in place of the real esult hand;er. This example is very general for demonstration purposes, and can
 * be adapted to the results of real operations. Test assertions are made by comparing an expected result with the actual result.
 */
public class ResultHandler {

  protected boolean mIsComplete = false;
  private int mResponseCode;
  private String mPayload;

  public void onResult(int responseCode, String payload) {
    mIsComplete = true;
    mResponseCode = responseCode;
    mPayload = payload;
  }


  public void onResult(Exception e) {
    mIsComplete = true;
    e.printStackTrace();

  }

  public String getPayload() {
    return mPayload;
  }


  /**
   * @return the HTTP response code
   */
  public int getResponseCode() {
    return mResponseCode;
  }

}
