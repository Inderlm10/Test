package com.testframework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.testframework.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Test data manager utility class
 * Handles loading and managing test data from various sources
 */
public class TestDataManager {
    private static final Logger logger = LoggerFactory.getLogger(TestDataManager.class);
    private static TestDataManager instance;
    private ObjectMapper objectMapper;
    private Map<String, Object> cachedData;
    
    private TestDataManager() {
        this.objectMapper = new ObjectMapper();
        this.cachedData = new HashMap<>();
    }
    
    public static TestDataManager getInstance() {
        if (instance == null) {
            instance = new TestDataManager();
        }
        return instance;
    }
    
    /**
     * Load test data from JSON file
     */
    public JsonNode loadTestData(String filename) {
        try {
            String testDataDir = TestConfig.getInstance().getTestDataDir();
            String filePath = testDataDir + "/" + filename;
            
            // Try to load from classpath first
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                JsonNode data = objectMapper.readTree(inputStream);
                logger.info("Loaded test data from classpath: {}", filePath);
                return data;
            }
            
            // Try to load from file system
            File file = new File(filePath);
            if (file.exists()) {
                JsonNode data = objectMapper.readTree(file);
                logger.info("Loaded test data from file system: {}", filePath);
                return data;
            }
            
            logger.warn("Test data file not found: {}", filePath);
            return null;
            
        } catch (IOException e) {
            logger.error("Failed to load test data from {}: {}", filename, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Load test data as list of objects
     */
    public <T> List<T> loadTestDataAsList(String filename, Class<T> clazz) {
        try {
            String testDataDir = TestConfig.getInstance().getTestDataDir();
            String filePath = testDataDir + "/" + filename;
            
            // Try to load from classpath first
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
                List<T> data = objectMapper.readValue(inputStream, listType);
                logger.info("Loaded test data list from classpath: {}", filePath);
                return data;
            }
            
            // Try to load from file system
            File file = new File(filePath);
            if (file.exists()) {
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
                List<T> data = objectMapper.readValue(file, listType);
                logger.info("Loaded test data list from file system: {}", filePath);
                return data;
            }
            
            logger.warn("Test data file not found: {}", filePath);
            return null;
            
        } catch (IOException e) {
            logger.error("Failed to load test data list from {}: {}", filename, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Load test data as single object
     */
    public <T> T loadTestDataAsObject(String filename, Class<T> clazz) {
        try {
            String testDataDir = TestConfig.getInstance().getTestDataDir();
            String filePath = testDataDir + "/" + filename;
            
            // Try to load from classpath first
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                T data = objectMapper.readValue(inputStream, clazz);
                logger.info("Loaded test data object from classpath: {}", filePath);
                return data;
            }
            
            // Try to load from file system
            File file = new File(filePath);
            if (file.exists()) {
                T data = objectMapper.readValue(file, clazz);
                logger.info("Loaded test data object from file system: {}", filePath);
                return data;
            }
            
            logger.warn("Test data file not found: {}", filePath);
            return null;
            
        } catch (IOException e) {
            logger.error("Failed to load test data object from {}: {}", filename, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Cache test data for later use
     */
    public void cacheData(String key, Object data) {
        cachedData.put(key, data);
        logger.debug("Cached test data with key: {}", key);
    }
    
    /**
     * Get cached test data
     */
    @SuppressWarnings("unchecked")
    public <T> T getCachedData(String key, Class<T> clazz) {
        Object data = cachedData.get(key);
        if (data != null && clazz.isInstance(data)) {
            return (T) data;
        }
        logger.warn("Cached data not found or wrong type for key: {}", key);
        return null;
    }
    
    /**
     * Clear cached data
     */
    public void clearCache() {
        cachedData.clear();
        logger.debug("Cleared test data cache");
    }
    
    /**
     * Generate random test data
     */
    public String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    /**
     * Generate random email
     */
    public String generateRandomEmail() {
        String username = generateRandomString(8);
        String domain = generateRandomString(6);
        return username + "@" + domain + ".com";
    }
    
    /**
     * Generate random phone number
     */
    public String generateRandomPhoneNumber() {
        return String.format("+1-%03d-%03d-%04d", 
            (int) (Math.random() * 900) + 100,
            (int) (Math.random() * 900) + 100,
            (int) (Math.random() * 9000) + 1000);
    }
    
    /**
     * Generate random number within range
     */
    public int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
    
    /**
     * Get test data by environment
     */
    public JsonNode getEnvironmentSpecificData(String baseFilename) {
        String environment = TestConfig.getInstance().getEnvironment();
        String filename = baseFilename.replace(".json", "-" + environment + ".json");
        
        JsonNode data = loadTestData(filename);
        if (data == null) {
            // Fallback to base filename
            data = loadTestData(baseFilename);
        }
        
        return data;
    }
    
    /**
     * Save test data to file
     */
    public void saveTestData(String filename, Object data) {
        try {
            String testDataDir = TestConfig.getInstance().getTestDataDir();
            File dir = new File(testDataDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String filePath = testDataDir + "/" + filename;
            objectMapper.writeValue(new File(filePath), data);
            logger.info("Saved test data to: {}", filePath);
            
        } catch (IOException e) {
            logger.error("Failed to save test data to {}: {}", filename, e.getMessage(), e);
        }
    }
} 