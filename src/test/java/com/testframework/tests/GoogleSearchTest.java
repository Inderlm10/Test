package com.testframework.tests;

import com.testframework.core.BaseTest;
import com.testframework.pages.GoogleHomePage;
import com.testframework.utils.ScreenshotUtils;
import com.testframework.utils.TestDataManager;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Example test class demonstrating Google search functionality
 * Shows how to use the framework for real-world testing scenarios
 */
public class GoogleSearchTest extends BaseTest {
    
    private GoogleHomePage googleHomePage;
    private TestDataManager testDataManager;
    
    @BeforeClass
    public void setUp() {
        googleHomePage = new GoogleHomePage();
        testDataManager = TestDataManager.getInstance();
    }
    
    @Test(description = "Verify Google homepage loads correctly")
    public void testGoogleHomepageLoads() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logVerification("Verify page title contains Google");
        assertPageTitleContains("Google", "Page title should contain 'Google'");
        
        logVerification("Verify search box is displayed");
        assertElementDisplayed(By.name("q"), "Search box should be displayed");
        
        logVerification("Verify Google logo is displayed");
        assertTrue(googleHomePage.isGoogleLogoDisplayed(), "Google logo should be displayed");
        
        logVerification("Verify search button is enabled");
        assertTrue(googleHomePage.isSearchButtonEnabled(), "Search button should be enabled");
    }
    
    @Test(description = "Perform basic search functionality")
    public void testBasicSearch() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logStep("Enter search term 'Selenium WebDriver'");
        googleHomePage.enterSearchTerm("Selenium WebDriver");
        
        logStep("Click search button");
        googleHomePage.clickSearchButton();
        
        logVerification("Verify search results page is loaded");
        assertPageTitleContains("Selenium WebDriver", "Page title should contain search term");
        
        logVerification("Verify search results are displayed");
        assertElementDisplayed(By.id("rcnt"), "Search results should be displayed");
    }
    
    @Test(description = "Test search with different terms using data provider", dataProvider = "searchTerms")
    public void testSearchWithDifferentTerms(String searchTerm, String expectedResult) {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logData("Search term: " + searchTerm);
        logStep("Perform search with term: " + searchTerm);
        googleHomePage.search(searchTerm);
        
        logVerification("Verify search results contain expected term");
        assertPageTitleContains(expectedResult, "Page title should contain expected result");
    }
    
    @DataProvider(name = "searchTerms")
    public Object[][] getSearchTerms() {
        return new Object[][] {
            {"Java programming", "Java programming"},
            {"Test automation", "Test automation"},
            {"Selenium framework", "Selenium framework"},
            {"WebDriver tutorial", "WebDriver tutorial"}
        };
    }
    
    @Test(description = "Test search box functionality")
    public void testSearchBoxFunctionality() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logStep("Enter text in search box");
        String testText = "Test automation framework";
        googleHomePage.enterSearchTerm(testText);
        
        logVerification("Verify search box contains entered text");
        assertEquals(googleHomePage.getSearchBoxValue(), testText, "Search box should contain entered text");
        
        logStep("Clear search box");
        googleHomePage.clearSearchBox();
        
        logVerification("Verify search box is empty");
        assertEquals(googleHomePage.getSearchBoxValue(), "", "Search box should be empty after clearing");
    }
    
    @Test(description = "Test search suggestions")
    public void testSearchSuggestions() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logStep("Enter partial search term to trigger suggestions");
        googleHomePage.enterSearchTerm("selenium");
        
        logStep("Wait for search suggestions to appear");
        googleHomePage.waitForSearchSuggestions();
        
        logVerification("Verify search suggestions are displayed");
        assertTrue(googleHomePage.getSearchSuggestionsCount() > 0, "Search suggestions should be displayed");
        
        // Take screenshot of suggestions
        ScreenshotUtils.takeViewportScreenshot("search_suggestions");
    }
    
    @Test(description = "Test page elements and interactions")
    public void testPageElementsAndInteractions() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logStep("Hover over search box");
        googleHomePage.hoverOverSearchBox();
        
        logVerification("Verify search box placeholder text");
        String placeholder = googleHomePage.getSearchBoxPlaceholder();
        assertTrue(placeholder.contains("Google") || placeholder.contains("Search"), 
                  "Placeholder should contain 'Google' or 'Search'");
        
        logStep("Click Google apps button");
        googleHomePage.clickGoogleAppsButton();
        
        logVerification("Verify Google apps menu is displayed");
        assertElementDisplayed(By.cssSelector("div[role='dialog']"), "Google apps menu should be displayed");
        
        // Take screenshot of apps menu
        ScreenshotUtils.takeViewportScreenshot("google_apps_menu");
    }
    
    @Test(description = "Test page responsiveness")
    public void testPageResponsiveness() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logStep("Resize browser window to mobile size");
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));
        
        logVerification("Verify page elements are still accessible");
        assertTrue(googleHomePage.isSearchBoxDisplayed(), "Search box should be displayed on mobile");
        assertTrue(googleHomePage.isGoogleLogoDisplayed(), "Google logo should be displayed on mobile");
        
        logStep("Resize browser window back to desktop size");
        driver.manage().window().maximize();
        
        logVerification("Verify page elements are still accessible");
        assertTrue(googleHomePage.isSearchBoxDisplayed(), "Search box should be displayed on desktop");
        assertTrue(googleHomePage.isGoogleLogoDisplayed(), "Google logo should be displayed on desktop");
    }
    
    @Test(description = "Test search with special characters")
    public void testSearchWithSpecialCharacters() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logStep("Enter search term with special characters");
        String specialSearchTerm = "Java & Selenium @ 2024";
        googleHomePage.search(specialSearchTerm);
        
        logVerification("Verify search results page is loaded");
        assertPageTitleContains("Java", "Page title should contain search term");
        
        logVerification("Verify search results are displayed");
        assertElementDisplayed(By.id("rcnt"), "Search results should be displayed");
    }
    
    @Test(description = "Test search with empty term")
    public void testSearchWithEmptyTerm() {
        logStep("Navigate to Google homepage");
        googleHomePage.navigateToHomePage();
        
        logStep("Click search button without entering any term");
        googleHomePage.clickSearchButton();
        
        logVerification("Verify user stays on Google homepage");
        assertPageTitleContains("Google", "Should remain on Google homepage");
        
        logVerification("Verify search box is still displayed");
        assertTrue(googleHomePage.isSearchBoxDisplayed(), "Search box should still be displayed");
    }
} 