package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Kiểm thử chức năng Giỏ hàng (TC03).
 */
public class CartTest extends BaseTest {

    private static final String EXPECTED_PRODUCT_NAME = "Sauce Labs Backpack";

    @Test
    @DisplayName("TC03 - Thêm sản phẩm vào giỏ hàng và kiểm tra trong giỏ hàng")
    void testAddProductToCart() {
        // 1. Đăng nhập vào hệ thống
        login(VALID_USERNAME, VALID_PASSWORD);
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        // 2. Thêm sản phẩm "Sauce Labs Backpack" vào giỏ hàng
        WebElement addToCartButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))
        );
        addToCartButton.click();

        // 3. Xác minh badge số lượng trên biểu tượng giỏ hàng hiển thị bằng 1
        WebElement cartBadge = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge"))
        );
        assertEquals(
                "1",
                cartBadge.getText(),
                "Số lượng sản phẩm hiển thị trên biểu tượng giỏ hàng phải bằng 1"
        );

        // 4. Mở giỏ hàng
        WebElement cartLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))
        );
        cartLink.click();

        // 5. Chờ trang giỏ hàng tải xong và lấy thông tin sản phẩm trong giỏ
        wait.until(ExpectedConditions.urlContains("cart.html"));
        WebElement productNameInCart = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name"))
        );
        WebElement cartQuantity = driver.findElement(By.className("cart_quantity"));

        // 6. Chụp ảnh màn hình minh chứng tại trang giỏ hàng
        takeScreenshot("test-03-cart.png");

        // 7. Xác minh đúng sản phẩm và số lượng trong giỏ hàng
        assertTrue(
                driver.getCurrentUrl().contains("cart.html"),
                "URL hiện tại phải là trang giỏ hàng (cart.html)"
        );
        assertEquals(
                EXPECTED_PRODUCT_NAME,
                productNameInCart.getText(),
                "Tên sản phẩm trong giỏ hàng phải khớp với sản phẩm đã chọn"
        );
        assertEquals(
                "1",
                cartQuantity.getText(),
                "Số lượng của sản phẩm trong giỏ hàng phải bằng 1"
        );
    }
}
