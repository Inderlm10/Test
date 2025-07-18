package com.testframework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import com.testframework.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot utility class for capturing and managing screenshots
 */
public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    
    /**
     * Take full page screenshot
     */
    public static String takeFullPageScreenshot(String testName) {
        return takeFullPageScreenshot(testName, null);
    }
    
    /**
     * Take full page screenshot with custom filename
     */
    public static String takeFullPageScreenshot(String testName, String customName) {
        try {
            WebDriver driver = com.testframework.core.DriverManager.getDriver();
            if (driver instanceof TakesScreenshot) {
                AShot aShot = new AShot();
                aShot.shootingStrategy(ShootingStrategies.viewportPasting(1000));
                
                Screenshot screenshot = aShot.takeScreenshot(driver);
                
                String filename = generateScreenshotFilename(testName, customName, "fullpage");
                String filePath = saveScreenshot(screenshot, filename);
                
                logger.info("Full page screenshot saved: {}", filePath);
                return filePath;
            }
        } catch (Exception e) {
            logger.error("Failed to take full page screenshot: {}", e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Take viewport screenshot
     */
    public static String takeViewportScreenshot(String testName) {
        return takeViewportScreenshot(testName, null);
    }
    
    /**
     * Take viewport screenshot with custom filename
     */
    public static String takeViewportScreenshot(String testName, String customName) {
        try {
            WebDriver driver = com.testframework.core.DriverManager.getDriver();
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot ts = (TakesScreenshot) driver;
                File screenshot = ts.getScreenshotAs(OutputType.FILE);
                
                String filename = generateScreenshotFilename(testName, customName, "viewport");
                String filePath = saveScreenshotFile(screenshot, filename);
                
                logger.info("Viewport screenshot saved: {}", filePath);
                return filePath;
            }
        } catch (Exception e) {
            logger.error("Failed to take viewport screenshot: {}", e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Take element screenshot
     */
    public static String takeElementScreenshot(String testName, WebElement element) {
        return takeElementScreenshot(testName, element, null);
    }
    
    /**
     * Take element screenshot with custom filename
     */
    public static String takeElementScreenshot(String testName, WebElement element, String customName) {
        try {
            AShot aShot = new AShot();
            Screenshot screenshot = aShot.takeScreenshot(com.testframework.core.DriverManager.getDriver(), element);
            
            String filename = generateScreenshotFilename(testName, customName, "element");
            String filePath = saveScreenshot(screenshot, filename);
            
            logger.info("Element screenshot saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to take element screenshot: {}", e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Take screenshot by element locator
     */
    public static String takeElementScreenshot(String testName, By locator) {
        return takeElementScreenshot(testName, locator, null);
    }
    
    /**
     * Take screenshot by element locator with custom filename
     */
    public static String takeElementScreenshot(String testName, By locator, String customName) {
        try {
            WebDriver driver = com.testframework.core.DriverManager.getDriver();
            WebElement element = driver.findElement(locator);
            return takeElementScreenshot(testName, element, customName);
        } catch (Exception e) {
            logger.error("Failed to take element screenshot by locator: {}", e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Take screenshot with highlight
     */
    public static String takeHighlightedScreenshot(String testName, WebElement element) {
        return takeHighlightedScreenshot(testName, element, null);
    }
    
    /**
     * Take screenshot with highlight and custom filename
     */
    public static String takeHighlightedScreenshot(String testName, WebElement element, String customName) {
        try {
            WebDriver driver = com.testframework.core.DriverManager.getDriver();
            
            // Highlight element
            highlightElement(element, "red", 3);
            
            // Take screenshot
            AShot aShot = new AShot();
            Screenshot screenshot = aShot.takeScreenshot(driver, element);
            
            // Remove highlight
            removeHighlight(element);
            
            String filename = generateScreenshotFilename(testName, customName, "highlighted");
            String filePath = saveScreenshot(screenshot, filename);
            
            logger.info("Highlighted screenshot saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to take highlighted screenshot: {}", e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Highlight element
     */
    public static void highlightElement(WebElement element, String color, int duration) {
        try {
            WebDriver driver = com.testframework.core.DriverManager.getDriver();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            String originalStyle = element.getAttribute("style");
            String highlightStyle = "border: 3px solid " + color + "; background-color: yellow;";
            
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, highlightStyle);
            
            // Remove highlight after duration
            Thread.sleep(duration * 1000L);
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
            
        } catch (Exception e) {
            logger.error("Failed to highlight element: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Remove highlight from element
     */
    public static void removeHighlight(WebElement element) {
        try {
            WebDriver driver = com.testframework.core.DriverManager.getDriver();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', '');", element);
        } catch (Exception e) {
            logger.error("Failed to remove highlight: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Generate screenshot filename
     */
    private static String generateScreenshotFilename(String testName, String customName, String type) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String baseName = customName != null ? customName : testName;
        return String.format("%s_%s_%s.png", baseName, type, timestamp);
    }
    
    /**
     * Save screenshot to file
     */
    private static String saveScreenshot(Screenshot screenshot, String filename) throws IOException {
        String screenshotDir = TestConfig.getInstance().getScreenshotDir();
        Path dir = Paths.get(screenshotDir);
        
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        
        Path filePath = dir.resolve(filename);
        ImageIO.write(screenshot.getImage(), "PNG", filePath.toFile());
        
        return filePath.toString();
    }
    
    /**
     * Save screenshot file
     */
    private static String saveScreenshotFile(File screenshot, String filename) throws IOException {
        String screenshotDir = TestConfig.getInstance().getScreenshotDir();
        Path dir = Paths.get(screenshotDir);
        
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        
        Path filePath = dir.resolve(filename);
        Files.copy(screenshot.toPath(), filePath);
        
        return filePath.toString();
    }
    
    /**
     * Clean old screenshots
     */
    public static void cleanOldScreenshots(int daysToKeep) {
        try {
            String screenshotDir = TestConfig.getInstance().getScreenshotDir();
            Path dir = Paths.get(screenshotDir);
            
            if (!Files.exists(dir)) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L);
            
            Files.list(dir)
                .filter(path -> path.toString().endsWith(".png"))
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        logger.debug("Deleted old screenshot: {}", path);
                    } catch (IOException e) {
                        logger.warn("Failed to delete old screenshot {}: {}", path, e.getMessage());
                    }
                });
                
            logger.info("Cleaned old screenshots older than {} days", daysToKeep);
            
        } catch (IOException e) {
            logger.error("Failed to clean old screenshots: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Get screenshot count
     */
    public static long getScreenshotCount() {
        try {
            String screenshotDir = TestConfig.getInstance().getScreenshotDir();
            Path dir = Paths.get(screenshotDir);
            
            if (!Files.exists(dir)) {
                return 0;
            }
            
            return Files.list(dir)
                .filter(path -> path.toString().endsWith(".png"))
                .count();
                
        } catch (IOException e) {
            logger.error("Failed to get screenshot count: {}", e.getMessage(), e);
            return 0;
        }
    }
} 