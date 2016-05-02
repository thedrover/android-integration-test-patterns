package com.example;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Example instrumentation test using UiThreadTestRule because there is no Activity.
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentationTest extends SharedTest {

  @Rule
  public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  @Test
  public void testAdd() {
    Assert.assertEquals(5, TestUtils.add(3, 2));
    Assert.assertEquals(7, TestUtils.subtract(10, 3));

  }

  /**
   * Load a resource from the application assets and verify the contents are as expected.
   */
  @Test
  public void testLoadAsset() throws IOException {
    // using the application context
    AssetManager assetManager = InstrumentationRegistry.getTargetContext().getAssets();
    checkAsset(assetManager);
  }

  /**
   * Load a test resource in the Android instrumentatio test directory tree and verify the contents are as expected.
   */
  @Test
  public void testAndroidTestResource() throws IOException {
    checkFile("com/example/androidtest/androidTestResource.txt", "Android test");
  }

  /**
   * Load a test resource in the common test directory tree and verify the contents are as expected.
   */
  @Test
  public void testCommonTestResource() throws IOException {
    checkFile(COM_EXAMPLE_COMMON_COMMON_RESOURCE_TXT, "common");
  }

}