import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aopalliance.reflect.Class;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestCase {
    private static WebDriver driver;
    private static BatchInfo myTestBatch;
    private static EyesRunner testRunner;
    private static Configuration suiteConfig;
    Eyes eyes;

    @BeforeAll
    public static void beforeAll() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        driver = WebDriverManager.chromedriver().capabilities(chromeOptions).create();
        myTestBatch = new BatchInfo("Test Cases");
        testRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
        suiteConfig = new Configuration();
        suiteConfig.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        suiteConfig.setBatch(myTestBatch);
        suiteConfig.setStitchMode(StitchMode.CSS);
        suiteConfig.addBrowser(1000, 600, BrowserType.CHROME);
        suiteConfig.addBrowser(1600, 1200, BrowserType.FIREFOX);
        suiteConfig.addBrowser(1200, 800, BrowserType.SAFARI);
        suiteConfig.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.LANDSCAPE);
        suiteConfig.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        eyes = new Eyes(testRunner);
        eyes.setConfiguration(suiteConfig);
        eyes.open(driver, "My First Tests", testInfo.getTestMethod().get().getName(), new RectangleSize(1000, 600));
    }

    @Test
    public void lazyLoad() {
        driver.get("https://applitools.com");
        eyes.check(Target.window().lazyLoad());
    }

    @AfterEach
    public void afterEach() {
        eyes.closeAsync();
    }

    @AfterAll
    public static void afterAll() {
        driver.close();
        TestResultsSummary results = testRunner.getAllTestResults();
        System.out.println(results);
    }
}
