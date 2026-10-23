package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Kiểm thử chức năng Đăng nhập (TC01 & TC02).
 */
public class LoginTest extends BaseTest {

    @Test
    @DisplayName("TC01 - Đăng nhập thành công với tài khoản hợp lệ")
    void testLoginSuccess() {
        // 1. Mở trang đăng nhập và nhập thông tin hợp lệ
        login(VALID_USERNAME, VALID_PASSWORD);

        // 2. Chờ chuyển hướng sang trang danh sách sản phẩm và hiển thị tiêu đề
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        WebElement pageTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("title"))
        );

        // 3. Chụp ảnh màn hình minh chứng
        takeScreenshot("test-01-login-success.png");

        // 4. Xác minh (Assertion) kết quả thực tế
        String currentUrl = driver.getCurrentUrl();
        assertTrue(
                currentUrl.contains("inventory.html"),
                "URL sau khi đăng nhập phải chứa 'inventory.html', nhưng thực tế là: " + currentUrl
        );
        assertEquals(
                "Products",
                pageTitle.getText(),
                "Tiêu đề trang sau khi đăng nhập phải là 'Products'"
        );
    }

    @Test
    @DisplayName("TC02 - Đăng nhập thất bại khi nhập sai mật khẩu")
    void testLoginFailureWithWrongPassword() {
        // 1. Mở trang đăng nhập và nhập đúng username nhưng sai mật khẩu
        login(VALID_USERNAME, "wrong_password_123");

        // 2. Chờ thông báo lỗi hiển thị trên giao diện
        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
        );

        // 3. Chụp ảnh màn hình minh chứng
        takeScreenshot("test-02-login-failure.png");

        // 4. Xác minh (Assertion) thông báo lỗi hiển thị đúng nội dung và không chuyển trang
        assertTrue(
                errorMessage.isDisplayed(),
                "Thông báo lỗi phải được hiển thị khi nhập sai mật khẩu"
        );
        assertTrue(
                errorMessage.getText().contains("Epic sadface: Username and password do not match any user in this service"),
                "Nội dung thông báo lỗi không đúng như mong đợi. Thực tế: " + errorMessage.getText()
        );
        assertEquals(
                BASE_URL,
                driver.getCurrentUrl(),
                "Người dùng phải ở lại trang đăng nhập khi đăng nhập thất bại"
        );
    }
}
