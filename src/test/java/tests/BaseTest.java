package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp cơ sở (BaseTest) dùng chung cho các lớp kiểm thử.
 * Đảm nhận việc khởi tạo WebDriver trước mỗi test case (@BeforeEach),
 * đóng trình duyệt sau khi test kết thúc (@AfterEach),
 * và cung cấp các phương thức hỗ trợ đăng nhập, chụp ảnh màn hình minh chứng.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final String BASE_URL = "https://www.saucedemo.com/";
    protected static final String VALID_USERNAME = "standard_user";
    protected static final String VALID_PASSWORD = "secret_sauce";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();

        // Tắt thông báo lưu mật khẩu và cảnh báo rò rỉ mật khẩu của Chrome để tránh che khuất giao diện
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--incognito");

        // Selenium Manager tự động tải và cấu hình ChromeDriver tương thích
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Khởi tạo Explicit Wait với thời gian chờ tối đa 10 giây
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        // Đảm bảo luôn đóng trình duyệt kể cả khi test case thất bại
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Phương thức hỗ trợ thực hiện thao tác đăng nhập vào hệ thống SauceDemo.
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     */
    protected void login(String username, String password) {
        driver.get(BASE_URL);

        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))
        );
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameInput.clear();
        usernameInput.sendKeys(username);

        passwordInput.clear();
        passwordInput.sendKeys(password);

        loginButton.click();
    }

    /**
     * Chụp ảnh màn hình trình duyệt thực tế và lưu vào thư mục screenshots/.
     *
     * @param fileName Tên file ảnh (ví dụ: "test-01-login-success.png")
     */
    protected void takeScreenshot(String fileName) {
        if (driver instanceof TakesScreenshot screenshotDriver) {
            try {
                // Chờ trang và các hình ảnh tải hoàn tất trước khi chụp màn hình
                if (driver instanceof JavascriptExecutor js) {
                    wait.until(d -> "complete".equals(js.executeScript("return document.readyState")));
                    wait.until(d -> (Boolean) js.executeScript(
                            "return Array.from(document.images).every(img => img.complete && img.naturalWidth > 0);"
                    ));
                }
                Path screenshotDir = Paths.get("screenshots");
                if (!Files.exists(screenshotDir)) {
                    Files.createDirectories(screenshotDir);
                }
                File sourceFile = screenshotDriver.getScreenshotAs(OutputType.FILE);
                Path targetPath = screenshotDir.resolve(fileName);
                Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.err.println("Không thể lưu ảnh chụp màn hình: " + e.getMessage());
            }
        }
    }
}
