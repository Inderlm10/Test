package com.testframework.pages;

import com.testframework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for Google Homepage
 * Demonstrates how to create page objects using the framework
 */
public class GoogleHomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(GoogleHomePage.class);
    
    // Page locators
    private static final By SEARCH_BOX = By.name("q");
    private static final By SEARCH_BUTTON = By.name("btnK");
    private static final By FEELING_LUCKY_BUTTON = By.name("btnI");
    private static final By GOOGLE_LOGO = By.cssSelector("img[alt='Google']");
    private static final By SEARCH_SUGGESTIONS = By.cssSelector("ul[role='listbox'] li");
    private static final By VOICE_SEARCH_BUTTON = By.cssSelector("button[aria-label='Search by voice']");
    private static final By GOOGLE_APPS_BUTTON = By.cssSelector("a[title='Google apps']");
    private static final By SIGN_IN_BUTTON = By.cssSelector("a[href*='accounts.google.com']");
    
    /**
     * Navigate to Google homepage
     */
    public void navigateToHomePage() {
        logger.info("Navigating to Google homepage");
        driver.get("https://www.google.com");
        waitForElementVisible(SEARCH_BOX);
    }
    
    /**
     * Enter search term
     */
    public void enterSearchTerm(String searchTerm) {
        logger.info("Entering search term: {}", searchTerm);
        type(SEARCH_BOX, searchTerm);
    }
    
    /**
     * Click search button
     */
    public void clickSearchButton() {
        logger.info("Clicking search button");
        click(SEARCH_BUTTON);
    }
    
    /**
     * Click "I'm Feeling Lucky" button
     */
    public void clickFeelingLuckyButton() {
        logger.info("Clicking 'I'm Feeling Lucky' button");
        click(FEELING_LUCKY_BUTTON);
    }
    
    /**
     * Perform search
     */
    public void search(String searchTerm) {
        enterSearchTerm(searchTerm);
        clickSearchButton();
    }
    
    /**
     * Get search box value
     */
    public String getSearchBoxValue() {
        return getAttribute(SEARCH_BOX, "value");
    }
    
    /**
     * Check if search box is displayed
     */
    public boolean isSearchBoxDisplayed() {
        return isElementDisplayed(SEARCH_BOX);
    }
    
    /**
     * Check if search button is enabled
     */
    public boolean isSearchButtonEnabled() {
        return isElementEnabled(SEARCH_BUTTON);
    }
    
    /**
     * Check if Google logo is displayed
     */
    public boolean isGoogleLogoDisplayed() {
        return isElementDisplayed(GOOGLE_LOGO);
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Wait for search suggestions to appear
     */
    public void waitForSearchSuggestions() {
        waitForElementVisible(SEARCH_SUGGESTIONS);
    }
    
    /**
     * Get search suggestions count
     */
    public int getSearchSuggestionsCount() {
        return driver.findElements(SEARCH_SUGGESTIONS).size();
    }
    
    /**
     * Click on search suggestion by index
     */
    public void clickSearchSuggestion(int index) {
        By suggestionLocator = By.cssSelector("ul[role='listbox'] li:nth-child(" + (index + 1) + ")");
        click(suggestionLocator);
    }
    
    /**
     * Click voice search button
     */
    public void clickVoiceSearchButton() {
        logger.info("Clicking voice search button");
        click(VOICE_SEARCH_BUTTON);
    }
    
    /**
     * Click Google apps button
     */
    public void clickGoogleAppsButton() {
        logger.info("Clicking Google apps button");
        click(GOOGLE_APPS_BUTTON);
    }
    
    /**
     * Click sign in button
     */
    public void clickSignInButton() {
        logger.info("Clicking sign in button");
        click(SIGN_IN_BUTTON);
    }
    
    /**
     * Clear search box
     */
    public void clearSearchBox() {
        logger.info("Clearing search box");
        WebElement searchBox = waitForElement(SEARCH_BOX);
        searchBox.clear();
    }
    
    /**
     * Hover over search box
     */
    public void hoverOverSearchBox() {
        hoverOver(SEARCH_BOX);
    }
    
    /**
     * Get search box placeholder text
     */
    public String getSearchBoxPlaceholder() {
        return getAttribute(SEARCH_BOX, "placeholder");
    }
    
    /**
     * Check if page is loaded
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(SEARCH_BOX) && isElementDisplayed(GOOGLE_LOGO);
    }
    
    /**
     * Wait for page to load
     */
    public void waitForPageLoad() {
        waitForElementVisible(SEARCH_BOX);
        waitForElementVisible(GOOGLE_LOGO);
    }
} 