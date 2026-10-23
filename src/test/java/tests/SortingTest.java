package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Kiểm thử chức năng Sắp xếp sản phẩm (TC04).
 */
public class SortingTest extends BaseTest {

    @Test
    @DisplayName("TC04 - Sắp xếp sản phẩm theo giá tăng dần (Price: low to high)")
    void testSortProductsByPriceLowToHigh() {
        // 1. Đăng nhập vào hệ thống
        login(VALID_USERNAME, VALID_PASSWORD);
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        // 2. Tìm dropdown sắp xếp và chọn tùy chọn "Price (low to high)"
        WebElement sortDropdownElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("product_sort_container"))
        );
        Select sortDropdown = new Select(sortDropdownElement);
        sortDropdown.selectByVisibleText("Price (low to high)");

        // 3. Lấy danh sách các phần tử hiển thị giá sản phẩm sau khi sắp xếp
        List<WebElement> priceElements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_price"))
        );

        // Chuyển đổi chuỗi giá (ví dụ "$7.99") sang kiểu số thực (Double)
        List<Double> actualPrices = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace("$", "").trim();
            actualPrices.add(Double.parseDouble(priceText));
        }

        // 4. Chụp ảnh màn hình minh chứng danh sách sản phẩm đã sắp xếp
        takeScreenshot("test-04-sorting.png");

        // 5. Tạo danh sách kỳ vọng đã được sắp xếp tăng dần để đối chiếu
        List<Double> expectedSortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedSortedPrices);

        // 6. Xác minh (Assertion) danh sách giá hợp lệ và được sắp xếp tăng dần
        assertFalse(
                actualPrices.isEmpty(),
                "Danh sách giá sản phẩm không được rỗng"
        );
        assertEquals(
                expectedSortedPrices,
                actualPrices,
                "Danh sách giá sản phẩm trên giao diện phải được sắp xếp theo thứ tự tăng dần"
        );

        for (int i = 0; i < actualPrices.size() - 1; i++) {
            assertTrue(
                    actualPrices.get(i) <= actualPrices.get(i + 1),
                    String.format(
                            "Giá tại vị trí %d (%.2f) phải nhỏ hơn hoặc bằng giá tại vị trí %d (%.2f)",
                            i, actualPrices.get(i), i + 1, actualPrices.get(i + 1)
                    )
            );
        }
    }
}
