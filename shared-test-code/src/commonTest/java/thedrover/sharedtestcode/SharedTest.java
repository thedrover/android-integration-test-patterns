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
public class SharedTest {

  public static final String COM_EXAMPLE_UNITTEST_UNIT_TEST_RESOURCE_TXT = "com/example/unittest/unitTestResource.txt";
  public static final String COM_EXAMPLE_COMMON_COMMON_RESOURCE_TXT = "com/example/common/commonResource.txt";

  /**
   * Basic test is shared test class that is executed in both instrumentation and unit tests.
   */
  @Test
  public void testCommonSubtraction() {
    Assert.assertEquals(4, TestUtils.subtract(9, 5));
  }

  protected static void checkAsset(AssetManager assetManager) throws IOException {
    InputStream is = assetManager.open("asset.txt");
    Assert.assertNotNull("Resource not found", is);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    Assert.assertEquals("asset text", line);
    reader.close();
    is.close();
  }

  protected void checkFile(String s, String common) throws IOException {
    InputStream is = getClass().getClassLoader().getResourceAsStream(s);
    Assert.assertNotNull("Resource not found", is);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    Assert.assertEquals(common, line);
    reader.close();
    is.close();
  }
}
