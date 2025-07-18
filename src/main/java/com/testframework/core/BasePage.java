package com.testframework.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Base page object class providing common web element interactions
 * All page objects should extend this class
 */
public abstract class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.actions = new Actions(driver);
    }
    
    /**
     * Wait for element to be visible and clickable
     */
    protected WebElement waitForElement(By locator) {
        logger.debug("Waiting for element: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementVisible(By locator) {
        logger.debug("Waiting for element to be visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be present in DOM
     */
    protected WebElement waitForElementPresent(By locator) {
        logger.debug("Waiting for element to be present: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Click on element with explicit wait
     */
    protected void click(By locator) {
        logger.debug("Clicking element: {}", locator);
        WebElement element = waitForElement(locator);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Element click intercepted, trying JavaScript click");
            clickWithJavaScript(element);
        }
    }
    
    /**
     * Click using JavaScript
     */
    protected void clickWithJavaScript(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
    
    /**
     * Type text into element
     */
    protected void type(By locator, String text) {
        logger.debug("Typing '{}' into element: {}", text, locator);
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Get text from element
     */
    protected String getText(By locator) {
        logger.debug("Getting text from element: {}", locator);
        WebElement element = waitForElementVisible(locator);
        return element.getText();
    }
    
    /**
     * Get attribute value from element
     */
    protected String getAttribute(By locator, String attribute) {
        logger.debug("Getting attribute '{}' from element: {}", attribute, locator);
        WebElement element = waitForElementPresent(locator);
        return element.getAttribute(attribute);
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            logger.debug("Element not displayed: {}", locator);
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        try {
            return waitForElement(locator).isEnabled();
        } catch (TimeoutException e) {
            logger.debug("Element not enabled: {}", locator);
            return false;
        }
    }
    
    /**
     * Wait for element to disappear
     */
    protected void waitForElementToDisappear(By locator) {
        logger.debug("Waiting for element to disappear: {}", locator);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for page title to contain text
     */
    protected void waitForPageTitle(String title) {
        logger.debug("Waiting for page title to contain: {}", title);
        wait.until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Wait for URL to contain text
     */
    protected void waitForUrlContains(String urlPart) {
        logger.debug("Waiting for URL to contain: {}", urlPart);
        wait.until(ExpectedConditions.urlContains(urlPart));
    }
    
    /**
     * Scroll to element
     */
    protected void scrollToElement(By locator) {
        logger.debug("Scrolling to element: {}", locator);
        WebElement element = waitForElementPresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Hover over element
     */
    protected void hoverOver(By locator) {
        logger.debug("Hovering over element: {}", locator);
        WebElement element = waitForElement(locator);
        actions.moveToElement(element).perform();
    }
    
    /**
     * Double click on element
     */
    protected void doubleClick(By locator) {
        logger.debug("Double clicking element: {}", locator);
        WebElement element = waitForElement(locator);
        actions.doubleClick(element).perform();
    }
    
    /**
     * Right click on element
     */
    protected void rightClick(By locator) {
        logger.debug("Right clicking element: {}", locator);
        WebElement element = waitForElement(locator);
        actions.contextClick(element).perform();
    }
    
    /**
     * Drag and drop element
     */
    protected void dragAndDrop(By source, By target) {
        logger.debug("Dragging element {} to {}", source, target);
        WebElement sourceElement = waitForElement(source);
        WebElement targetElement = waitForElement(target);
        actions.dragAndDrop(sourceElement, targetElement).perform();
    }
    
    /**
     * Select option from dropdown by visible text
     */
    protected void selectByVisibleText(By locator, String text) {
        logger.debug("Selecting '{}' from dropdown: {}", text, locator);
        WebElement element = waitForElement(locator);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        select.selectByVisibleText(text);
    }
    
    /**
     * Select option from dropdown by value
     */
    protected void selectByValue(By locator, String value) {
        logger.debug("Selecting value '{}' from dropdown: {}", value, locator);
        WebElement element = waitForElement(locator);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        select.selectByValue(value);
    }
    
    /**
     * Get all options from dropdown
     */
    protected List<WebElement> getDropdownOptions(By locator) {
        logger.debug("Getting dropdown options: {}", locator);
        WebElement element = waitForElement(locator);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        return select.getOptions();
    }
    
    /**
     * Switch to frame by index
     */
    protected void switchToFrame(int index) {
        logger.debug("Switching to frame at index: {}", index);
        driver.switchTo().frame(index);
    }
    
    /**
     * Switch to frame by name or id
     */
    protected void switchToFrame(String nameOrId) {
        logger.debug("Switching to frame: {}", nameOrId);
        driver.switchTo().frame(nameOrId);
    }
    
    /**
     * Switch to frame by element
     */
    protected void switchToFrame(By locator) {
        logger.debug("Switching to frame: {}", locator);
        WebElement frameElement = waitForElementPresent(locator);
        driver.switchTo().frame(frameElement);
    }
    
    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        logger.debug("Switching to default content");
        driver.switchTo().defaultContent();
    }
    
    /**
     * Switch to window by title
     */
    protected void switchToWindow(String title) {
        logger.debug("Switching to window with title: {}", title);
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains(title)) {
                break;
            }
        }
    }
    
    /**
     * Accept alert
     */
    protected void acceptAlert() {
        logger.debug("Accepting alert");
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }
    
    /**
     * Dismiss alert
     */
    protected void dismissAlert() {
        logger.debug("Dismissing alert");
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }
    
    /**
     * Get alert text
     */
    protected String getAlertText() {
        logger.debug("Getting alert text");
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }
    
    /**
     * Send keys to alert
     */
    protected void sendKeysToAlert(String text) {
        logger.debug("Sending keys to alert: {}", text);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().sendKeys(text);
    }
    
    /**
     * Refresh page
     */
    protected void refreshPage() {
        logger.debug("Refreshing page");
        driver.navigate().refresh();
    }
    
    /**
     * Go back to previous page
     */
    protected void goBack() {
        logger.debug("Going back to previous page");
        driver.navigate().back();
    }
    
    /**
     * Go forward to next page
     */
    protected void goForward() {
        logger.debug("Going forward to next page");
        driver.navigate().forward();
    }
    
    /**
     * Get page source
     */
    protected String getPageSource() {
        return driver.getPageSource();
    }
    
    /**
     * Execute JavaScript
     */
    protected Object executeJavaScript(String script, Object... args) {
        logger.debug("Executing JavaScript: {}", script);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }
} 