package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Kiểm thử chức năng Đăng xuất (TC05).
 */
public class LogoutTest extends BaseTest {

    @Test
    @DisplayName("TC05 - Đăng xuất thành công khỏi hệ thống")
    void testLogoutSuccess() {
        // 1. Đăng nhập thành công vào hệ thống
        login(VALID_USERNAME, VALID_PASSWORD);
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        // 2. Mở menu điều hướng bên trái (Burger Menu)
        WebElement menuButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn"))
        );
        menuButton.click();

        // 3. Chờ liên kết Logout hiển thị đầy đủ (sau hiệu ứng trượt menu) và nhấn chọn
        WebElement logoutLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))
        );
        logoutLink.click();

        // 4. Chờ quay về trang đăng nhập (nút Login và ô nhập username hiển thị lại)
        WebElement loginButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("login-button"))
        );
        WebElement usernameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))
        );

        // 5. Chụp ảnh màn hình minh chứng đã quay về trang đăng nhập
        takeScreenshot("test-05-logout.png");

        // 6. Xác minh (Assertion) người dùng đã đăng xuất và quay lại trang đăng nhập
        assertEquals(
                BASE_URL,
                driver.getCurrentUrl(),
                "URL sau khi đăng xuất phải quay về trang đăng nhập"
        );
        assertTrue(
                loginButton.isDisplayed(),
                "Nút Login phải được hiển thị trên trang đăng nhập sau khi đăng xuất"
        );
        assertTrue(
                usernameInput.isDisplayed(),
                "Ô nhập Username phải được hiển thị trên trang đăng nhập sau khi đăng xuất"
        );
    }
}
