package com.testframework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for test framework
 * Handles loading and managing test configuration from various sources
 */
public class TestConfig {
    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);
    private static TestConfig instance;
    private Properties properties;
    private JsonNode configJson;
    
    private TestConfig() {
        loadConfiguration();
    }
    
    public static TestConfig getInstance() {
        if (instance == null) {
            instance = new TestConfig();
        }
        return instance;
    }
    
    /**
     * Load configuration from multiple sources
     */
    private void loadConfiguration() {
        properties = new Properties();
        
        // Load default properties
        loadPropertiesFromFile("config.properties");
        
        // Load environment-specific properties
        String environment = getEnvironment();
        if (environment != null && !environment.isEmpty()) {
            loadPropertiesFromFile("config-" + environment + ".properties");
        }
        
        // Load system properties (override file properties)
        properties.putAll(System.getProperties());
        
        // Load JSON configuration if available
        loadJsonConfiguration();
        
        logger.info("Configuration loaded successfully");
    }
    
    /**
     * Load properties from file
     */
    private void loadPropertiesFromFile(String filename) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (input != null) {
                properties.load(input);
                logger.info("Loaded properties from: {}", filename);
            } else {
                logger.debug("Properties file not found: {}", filename);
            }
        } catch (IOException e) {
            logger.warn("Failed to load properties from {}: {}", filename, e.getMessage());
        }
    }
    
    /**
     * Load JSON configuration
     */
    private void loadJsonConfiguration() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.json")) {
            if (input != null) {
                ObjectMapper mapper = new ObjectMapper();
                configJson = mapper.readTree(input);
                logger.info("Loaded JSON configuration");
            }
        } catch (IOException e) {
            logger.warn("Failed to load JSON configuration: {}", e.getMessage());
        }
    }
    
    /**
     * Get property value
     */
    public String getProperty(String key) {
        return getProperty(key, null);
    }
    
    /**
     * Get property value with default
     */
    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Getting property {} = {}", key, value);
        return value;
    }
    
    /**
     * Get integer property
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.warn("Invalid integer value for property {}: {}", key, value);
            }
        }
        return defaultValue;
    }
    
    /**
     * Get boolean property
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    /**
     * Get JSON configuration value
     */
    public String getJsonValue(String path) {
        if (configJson != null) {
            try {
                String[] keys = path.split("\\.");
                JsonNode node = configJson;
                for (String key : keys) {
                    node = node.get(key);
                    if (node == null) {
                        return null;
                    }
                }
                return node.asText();
            } catch (Exception e) {
                logger.warn("Failed to get JSON value for path {}: {}", path, e.getMessage());
            }
        }
        return null;
    }
    
    /**
     * Get environment name
     */
    public String getEnvironment() {
        return getProperty("test.environment", "default");
    }
    
    /**
     * Get base URL for testing
     */
    public String getBaseUrl() {
        return getProperty("test.baseUrl", "http://localhost:8080");
    }
    
    /**
     * Get browser name
     */
    public String getBrowser() {
        return getProperty("test.browser", "chrome");
    }
    
    /**
     * Get implicit wait timeout
     */
    public int getImplicitWait() {
        return getIntProperty("test.implicitWait", 30);
    }
    
    /**
     * Get page load timeout
     */
    public int getPageLoadTimeout() {
        return getIntProperty("test.pageLoadTimeout", 30);
    }
    
    /**
     * Get script timeout
     */
    public int getScriptTimeout() {
        return getIntProperty("test.scriptTimeout", 30);
    }
    
    /**
     * Check if headless mode is enabled
     */
    public boolean isHeadless() {
        return getBooleanProperty("test.headless", false);
    }
    
    /**
     * Get screenshot directory
     */
    public String getScreenshotDir() {
        return getProperty("test.screenshotDir", "screenshots");
    }
    
    /**
     * Get report directory
     */
    public String getReportDir() {
        return getProperty("test.reportDir", "test-output");
    }
    
    /**
     * Get test data directory
     */
    public String getTestDataDir() {
        return getProperty("test.testDataDir", "test-data");
    }
    
    /**
     * Get username for authentication
     */
    public String getUsername() {
        return getProperty("test.username", "");
    }
    
    /**
     * Get password for authentication
     */
    public String getPassword() {
        return getProperty("test.password", "");
    }
    
    /**
     * Get Selenium Grid URL
     */
    public String getGridUrl() {
        return getProperty("test.gridUrl", "");
    }
    
    /**
     * Check if remote execution is enabled
     */
    public boolean isRemoteExecution() {
        return getBooleanProperty("test.remote", false);
    }
    
    /**
     * Get retry count for failed tests
     */
    public int getRetryCount() {
        return getIntProperty("test.retryCount", 0);
    }
    
    /**
     * Get parallel thread count
     */
    public int getParallelThreads() {
        return getIntProperty("test.parallelThreads", 1);
    }
    
    /**
     * Check if video recording is enabled
     */
    public boolean isVideoRecordingEnabled() {
        return getBooleanProperty("test.videoRecording", false);
    }
    
    /**
     * Get video directory
     */
    public String getVideoDir() {
        return getProperty("test.videoDir", "videos");
    }
    
    /**
     * Print current configuration
     */
    public void printConfiguration() {
        logger.info("=== Test Configuration ===");
        logger.info("Environment: {}", getEnvironment());
        logger.info("Base URL: {}", getBaseUrl());
        logger.info("Browser: {}", getBrowser());
        logger.info("Headless: {}", isHeadless());
        logger.info("Remote Execution: {}", isRemoteExecution());
        logger.info("Grid URL: {}", getGridUrl());
        logger.info("Implicit Wait: {} seconds", getImplicitWait());
        logger.info("Page Load Timeout: {} seconds", getPageLoadTimeout());
        logger.info("Script Timeout: {} seconds", getScriptTimeout());
        logger.info("Retry Count: {}", getRetryCount());
        logger.info("Parallel Threads: {}", getParallelThreads());
        logger.info("Screenshot Directory: {}", getScreenshotDir());
        logger.info("Report Directory: {}", getReportDir());
        logger.info("Test Data Directory: {}", getTestDataDir());
        logger.info("Video Recording: {}", isVideoRecordingEnabled());
        logger.info("Video Directory: {}", getVideoDir());
        logger.info("========================");
    }
} 