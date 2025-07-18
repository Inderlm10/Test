package com.testframework.core;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base test class providing test lifecycle management and utilities
 * All test classes should extend this class
 */
public abstract class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    
    @BeforeClass
    public void setUpClass() {
        logger.info("Setting up test class: {}", this.getClass().getSimpleName());
    }
    
    @AfterClass
    public void tearDownClass() {
        logger.info("Tearing down test class: {}", this.getClass().getSimpleName());
        // Quit driver after all tests in the class are complete
        DriverManager.quitDriver();
    }
    
    @BeforeMethod
    public void setUpMethod() {
        logger.info("Setting up test method");
        driver = DriverManager.initializeDriver();
    }
    
    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        logger.info("Tearing down test method: {}", result.getName());
        
        // Take screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getName());
        }
        
        // Don't quit driver after each test method to allow for test reuse
        // Driver will be quit in @AfterClass
    }
    
    /**
     * Take screenshot and save to file
     */
    protected void takeScreenshot(String testName) {
        try {
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot ts = (TakesScreenshot) driver;
                File screenshot = ts.getScreenshotAs(OutputType.FILE);
                
                // Create screenshots directory if it doesn't exist
                Path screenshotsDir = Paths.get("screenshots");
                if (!Files.exists(screenshotsDir)) {
                    Files.createDirectories(screenshotsDir);
                }
                
                // Generate filename with timestamp
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String filename = String.format("%s_%s.png", testName, timestamp);
                Path screenshotPath = screenshotsDir.resolve(filename);
                
                // Copy screenshot to destination
                Files.copy(screenshot.toPath(), screenshotPath);
                logger.info("Screenshot saved: {}", screenshotPath);
            }
        } catch (IOException e) {
            logger.error("Failed to take screenshot: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Navigate to URL
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        DriverManager.navigateTo(url);
    }
    
    /**
     * Get current page title
     */
    protected String getPageTitle() {
        return DriverManager.getPageTitle();
    }
    
    /**
     * Get current page URL
     */
    protected String getCurrentUrl() {
        return DriverManager.getCurrentUrl();
    }
    
    /**
     * Wait for specified time in seconds
     */
    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted");
        }
    }
    
    /**
     * Assert that element is displayed
     */
    protected void assertElementDisplayed(org.openqa.selenium.By locator, String message) {
        BasePage page = new BasePage() {};
        boolean isDisplayed = page.isElementDisplayed(locator);
        assert isDisplayed : message;
    }
    
    /**
     * Assert that element is not displayed
     */
    protected void assertElementNotDisplayed(org.openqa.selenium.By locator, String message) {
        BasePage page = new BasePage() {};
        boolean isDisplayed = page.isElementDisplayed(locator);
        assert !isDisplayed : message;
    }
    
    /**
     * Assert that text contains expected value
     */
    protected void assertTextContains(org.openqa.selenium.By locator, String expectedText, String message) {
        BasePage page = new BasePage() {};
        String actualText = page.getText(locator);
        assert actualText.contains(expectedText) : message + " Expected: " + expectedText + ", Actual: " + actualText;
    }
    
    /**
     * Assert that page title contains expected text
     */
    protected void assertPageTitleContains(String expectedTitle, String message) {
        String actualTitle = getPageTitle();
        assert actualTitle.contains(expectedTitle) : message + " Expected: " + expectedTitle + ", Actual: " + actualTitle;
    }
    
    /**
     * Assert that URL contains expected text
     */
    protected void assertUrlContains(String expectedUrl, String message) {
        String actualUrl = getCurrentUrl();
        assert actualUrl.contains(expectedUrl) : message + " Expected: " + expectedUrl + ", Actual: " + actualUrl;
    }
    
    /**
     * Assert that two strings are equal
     */
    protected void assertEquals(String actual, String expected, String message) {
        assert actual.equals(expected) : message + " Expected: " + expected + ", Actual: " + actual;
    }
    
    /**
     * Assert that two integers are equal
     */
    protected void assertEquals(int actual, int expected, String message) {
        assert actual == expected : message + " Expected: " + expected + ", Actual: " + actual;
    }
    
    /**
     * Assert that condition is true
     */
    protected void assertTrue(boolean condition, String message) {
        assert condition : message;
    }
    
    /**
     * Assert that condition is false
     */
    protected void assertFalse(boolean condition, String message) {
        assert !condition : message;
    }
    
    /**
     * Log test step
     */
    protected void logStep(String step) {
        logger.info("STEP: {}", step);
    }
    
    /**
     * Log test data
     */
    protected void logData(String data) {
        logger.info("DATA: {}", data);
    }
    
    /**
     * Log verification
     */
    protected void logVerification(String verification) {
        logger.info("VERIFY: {}", verification);
    }
} 