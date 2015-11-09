package thedrover.sharedtestcode;

import android.content.res.AssetManager;

import junit.framework.Assert;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Tests may be placed in the common test code tree and run as unit tests or on device.
 */
public class SharedTests {

  @Test
  public void testCommonSubtraction() {
    Assert.assertEquals(4, TestUtils.subtract(9, 5));
  }



//  @Test
//  public void testLoadResourceFromCommon() throws IOException {
//    // using the application context
//    AssetManager assetManager = InstrumentationRegistry.getTargetContext().getAssets();
//    doX(assetManager);
//  }

  protected static void doX(AssetManager assetManager) throws IOException {
    InputStream is = assetManager.open("test.txt");
    Assert.assertNotNull("Resource not found", is);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    Assert.assertEquals("asset text", line);
    reader.close();
    is.close();
  }
}
