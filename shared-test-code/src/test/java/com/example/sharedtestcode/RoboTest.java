package com.example.sharedtestcode;

import android.content.res.AssetManager;

import com.example.SharedTest;
import com.example.TestUtils;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

/**
 * Example unit test.
 */
@Config(manifest = "shared-test-code/src/main/AndroidManifest.xml", sdk=21)
@RunWith(RobolectricTestRunner.class)
public class RoboTest extends SharedTest {


  @Test
  public void testAdd() throws InterruptedException {
    Assert.assertEquals(5, TestUtils.add(3, 2));
    Assert.assertEquals(7, TestUtils.subtract(10, 3));

  }

  /**
   * Load a resource from the application assets and verify the contents are as expected.
   */
  @Test
  public void testLoadAsset() throws IOException {
    // using the application context
    AssetManager assetManager = RuntimeEnvironment.application.getAssets();
    Assert.assertNotNull(assetManager);
    checkAsset(assetManager);
  }

  /**
   * Load a test resource in the unit test directory tree and verify the contents are as expected.
   */
  @Test
  public void testLoadUnitTestResource() throws IOException {
    checkFile(SharedTest.COM_EXAMPLE_UNITTEST_UNIT_TEST_RESOURCE_TXT, "unit test");
  }

  /**
   * Load a test resource in the common test directory tree and verify the contents are as expected.
   */
  @Test
  public void testLoadCommonResource() throws IOException {
    checkFile(SharedTest.COM_EXAMPLE_COMMON_COMMON_RESOURCE_TXT, "common");
  }

}
