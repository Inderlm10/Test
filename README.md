# Web Testing Framework

A comprehensive Java-based testing framework for automated web testing using Selenium WebDriver, TestNG, and modern testing practices.

## 🚀 Features

### Core Framework
- **WebDriver Management**: Automatic browser driver setup and lifecycle management
- **Page Object Model**: Robust page object implementation with common web interactions
- **Configuration Management**: Flexible configuration system supporting multiple environments
- **Test Data Management**: JSON-based test data loading and caching
- **Screenshot Utilities**: Advanced screenshot capabilities with highlighting and full-page capture
- **Logging**: Comprehensive logging with Logback integration

### Browser Support
- Chrome (default)
- Firefox
- Edge
- Safari
- Remote WebDriver (Selenium Grid)

### Testing Capabilities
- **Element Interactions**: Click, type, select, hover, drag & drop
- **Wait Strategies**: Explicit, implicit, and custom wait conditions
- **Assertions**: Built-in assertion methods for common validations
- **Data-Driven Testing**: TestNG data providers and JSON test data
- **Parallel Execution**: Configurable parallel test execution
- **Retry Logic**: Automatic retry for failed tests
- **Screenshot Capture**: Automatic screenshots on test failure

## 📁 Project Structure

```
Test/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── testframework/
│   │   │           ├── core/
│   │   │           │   ├── WebDriverManager.java
│   │   │           │   ├── BasePage.java
│   │   │           │   └── BaseTest.java
│   │   │           ├── config/
│   │   │           │   └── TestConfig.java
│   │   │           ├── pages/
│   │   │           │   └── GoogleHomePage.java
│   │   │           └── utils/
│   │   │               ├── TestDataManager.java
│   │   │               └── ScreenshotUtils.java
│   │   └── resources/
│   │       ├── config.properties
│   │       └── logback.xml
│   └── test/
│       └── java/
│           └── com/
│               └── testframework/
│                   └── tests/
│                       └── GoogleSearchTest.java
├── test-data/
│   └── search-data.json
├── pom.xml
├── testng.xml
└── README.md
```

## 🛠️ Setup and Installation

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser (for default testing)

### Installation Steps

1. **Clone or download the framework**
   ```bash
   git clone <repository-url>
   cd Test
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run tests**
   ```bash
   mvn test
   ```

## 📖 Usage Guide

### 1. Creating Page Objects

Extend `BasePage` class to create page objects:

```java
public class LoginPage extends BasePage {
    private static final By USERNAME_FIELD = By.id("username");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login");
    
    public void enterUsername(String username) {
        type(USERNAME_FIELD, username);
    }
    
    public void enterPassword(String password) {
        type(PASSWORD_FIELD, password);
    }
    
    public void clickLogin() {
        click(LOGIN_BUTTON);
    }
    
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
```

### 2. Writing Tests

Extend `BaseTest` class to create test classes:

```java
public class LoginTest extends BaseTest {
    private LoginPage loginPage;
    
    @BeforeClass
    public void setUp() {
        loginPage = new LoginPage();
    }
    
    @Test(description = "Test successful login")
    public void testSuccessfulLogin() {
        logStep("Navigate to login page");
        navigateTo("https://example.com/login");
        
        logStep("Perform login");
        loginPage.login("testuser", "password");
        
        logVerification("Verify successful login");
        assertPageTitleContains("Dashboard", "Should be on dashboard page");
    }
}
```

### 3. Configuration

#### Environment Configuration
Create environment-specific configuration files:

```properties
# config-dev.properties
test.baseUrl=https://dev.example.com
test.browser=chrome
test.headless=true

# config-prod.properties
test.baseUrl=https://prod.example.com
test.browser=firefox
test.headless=false
```

#### System Properties
Override configuration using system properties:

```bash
mvn test -Dtest.browser=firefox -Dtest.headless=true
```

### 4. Test Data Management

#### JSON Test Data
```json
{
  "users": [
    {
      "username": "testuser1",
      "password": "password123",
      "role": "admin"
    },
    {
      "username": "testuser2",
      "password": "password456",
      "role": "user"
    }
  ]
}
```

#### Loading Test Data
```java
TestDataManager dataManager = TestDataManager.getInstance();
JsonNode testData = dataManager.loadTestData("users.json");
String username = testData.get("users").get(0).get("username").asText();
```

### 5. Screenshots

#### Automatic Screenshots
Screenshots are automatically taken on test failure.

#### Manual Screenshots
```java
// Full page screenshot
ScreenshotUtils.takeFullPageScreenshot("test_name");

// Element screenshot
WebElement element = driver.findElement(By.id("button"));
ScreenshotUtils.takeElementScreenshot("test_name", element);

// Highlighted screenshot
ScreenshotUtils.takeHighlightedScreenshot("test_name", element);
```

## 🔧 Configuration Options

### Browser Configuration
- `test.browser`: Browser type (chrome, firefox, edge, safari)
- `test.headless`: Headless mode (true/false)
- `test.remote`: Remote execution (true/false)
- `test.gridUrl`: Selenium Grid URL

### Timeout Configuration
- `test.implicitWait`: Implicit wait timeout in seconds
- `test.pageLoadTimeout`: Page load timeout in seconds
- `test.scriptTimeout`: Script timeout in seconds

### Test Execution
- `test.retryCount`: Number of retries for failed tests
- `test.parallelThreads`: Number of parallel threads
- `test.videoRecording`: Enable video recording (true/false)

### Directory Configuration
- `test.screenshotDir`: Screenshot directory
- `test.reportDir`: Test report directory
- `test.testDataDir`: Test data directory
- `test.videoDir`: Video recording directory

## 📊 Test Execution

### Running All Tests
```bash
mvn test
```

### Running Specific Test Class
```bash
mvn test -Dtest=GoogleSearchTest
```

### Running Specific Test Method
```bash
mvn test -Dtest=GoogleSearchTest#testBasicSearch
```

### Running Tests with Different Browser
```bash
mvn test -Dtest.browser=firefox
```

### Running Tests in Headless Mode
```bash
mvn test -Dtest.headless=true
```

### Running Tests in Parallel
```bash
mvn test -Dtest.parallelThreads=4
```

## 📈 Reports and Logs

### Test Reports
- **TestNG Reports**: Located in `test-output/` directory
- **HTML Reports**: Detailed HTML reports with test results
- **XML Reports**: Machine-readable XML reports

### Logs
- **Application Logs**: `logs/test-framework.log`
- **Test Results**: `logs/test-results.log`
- **Console Output**: Real-time console logging

### Screenshots
- **Failure Screenshots**: Automatically captured on test failure
- **Manual Screenshots**: Captured using ScreenshotUtils
- **Location**: `screenshots/` directory

## 🧪 Example Tests

The framework includes comprehensive example tests demonstrating:

1. **Basic Page Navigation**: Navigate to pages and verify elements
2. **Form Interactions**: Fill forms, submit data, validate responses
3. **Search Functionality**: Test search features with various inputs
4. **Responsive Testing**: Test page behavior on different screen sizes
5. **Data-Driven Testing**: Use external test data for multiple scenarios
6. **Element Interactions**: Click, hover, drag & drop operations
7. **Screenshot Capture**: Manual and automatic screenshot functionality

## 🔍 Best Practices

### Page Object Model
- Keep page objects focused on single page functionality
- Use meaningful method names that describe user actions
- Encapsulate locators as private constants
- Implement page-specific wait conditions

### Test Design
- Write descriptive test method names
- Use data providers for similar test scenarios
- Implement proper setup and teardown methods
- Use logging to document test steps

### Configuration Management
- Use environment-specific configuration files
- Avoid hardcoding values in test code
- Use system properties for runtime overrides
- Keep sensitive data in secure configuration

### Test Data
- Use external JSON files for test data
- Implement data caching for performance
- Use environment-specific test data
- Generate random data for unique scenarios

## 🚀 Advanced Features

### Custom Listeners
Implement custom TestNG listeners for:
- Custom reporting
- Test execution monitoring
- Performance metrics
- Integration with external tools

### Parallel Execution
Configure parallel test execution:
```xml
<suite name="Test Suite" parallel="methods" thread-count="4">
```

### Retry Logic
Implement retry mechanisms for flaky tests:
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void testWithRetry() {
    // Test implementation
}
```

### Performance Testing
Add performance monitoring:
```java
long startTime = System.currentTimeMillis();
// Test actions
long endTime = System.currentTimeMillis();
assertTrue((endTime - startTime) < 5000, "Test should complete within 5 seconds");
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review example tests
- Consult the TestNG documentation

## 🔄 Version History

- **v1.0.0**: Initial release with core framework features
- Basic WebDriver management
- Page Object Model implementation
- Configuration management
- Screenshot utilities
- Example tests and documentation