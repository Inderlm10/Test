package com.testframework.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Core WebDriver management class for the testing framework
 * Handles browser initialization, configuration, and lifecycle management
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static WebDriver driver;
    private static final String DEFAULT_BROWSER = "chrome";
    private static final int DEFAULT_TIMEOUT = 30;
    
    /**
     * Initialize WebDriver with default configuration
     */
    public static WebDriver initializeDriver() {
        return initializeDriver(DEFAULT_BROWSER, DEFAULT_TIMEOUT);
    }
    
    /**
     * Initialize WebDriver with specified browser and timeout
     */
    public static WebDriver initializeDriver(String browser, int timeoutSeconds) {
        if (driver != null) {
            logger.info("WebDriver already initialized, returning existing instance");
            return driver;
        }
        
        logger.info("Initializing WebDriver for browser: {}", browser);
        
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = createChromeDriver();
                    break;
                case "firefox":
                    driver = createFirefoxDriver();
                    break;
                case "edge":
                    driver = createEdgeDriver();
                    break;
                case "safari":
                    driver = createSafariDriver();
                    break;
                default:
                    logger.warn("Unsupported browser: {}, falling back to Chrome", browser);
                    driver = createChromeDriver();
            }
            
            driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(timeoutSeconds, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(timeoutSeconds, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            
            logger.info("WebDriver initialized successfully");
            return driver;
            
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }
    
    /**
     * Initialize remote WebDriver for Selenium Grid
     */
    public static WebDriver initializeRemoteDriver(String gridUrl, String browser) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName(browser);
            
            driver = new RemoteWebDriver(new URL(gridUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            
            logger.info("Remote WebDriver initialized successfully");
            return driver;
            
        } catch (Exception e) {
            logger.error("Failed to initialize remote WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Remote WebDriver initialization failed", e);
        }
    }
    
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        return new ChromeDriver(options);
    }
    
    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        return new FirefoxDriver(options);
    }
    
    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        return new EdgeDriver(options);
    }
    
    private static WebDriver createSafariDriver() {
        SafariOptions options = new SafariOptions();
        return new SafariDriver(options);
    }
    
    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            logger.warn("WebDriver not initialized, creating new instance");
            return initializeDriver();
        }
        return driver;
    }
    
    /**
     * Quit and cleanup WebDriver
     */
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: {}", e.getMessage(), e);
            } finally {
                driver = null;
            }
        }
    }
    
    /**
     * Navigate to URL
     */
    public static void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        getDriver().get(url);
    }
    
    /**
     * Get current page title
     */
    public static String getPageTitle() {
        return getDriver().getTitle();
    }
    
    /**
     * Get current page URL
     */
    public static String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
} 