# Tài Liệu Kịch Bản Kiểm Thử Tự Động (Test Cases)

- **Website kiểm thử:** [https://www.saucedemo.com/](https://www.saucedemo.com/)
- **Công cụ kiểm thử:** Selenium WebDriver `4.27.0` + JUnit Jupiter `5.11.4` + Maven `3.9.16`
- **Trình duyệt thực thi:** Google Chrome (quản lý tự động qua Selenium Manager)

---

## 1. Bảng Tổng Hợp Kết Quả Kiểm Thử

| Test ID | Chức năng | Dữ liệu | Kết quả mong đợi | Kết quả thực tế | Trạng thái |
|---|---|---|---|---|---|
| **TC01** | Đăng nhập thành công (`LoginTest#testLoginSuccess`) | `username`: `standard_user`<br>`password`: `secret_sauce` | Đăng nhập thành công, chuyển hướng đến URL chứa `inventory.html` và tiêu đề trang hiển thị là `"Products"`. | Chuyển hướng đến `https://www.saucedemo.com/inventory.html`, tiêu đề trang hiển thị đúng `"Products"`. | **Pass** |
| **TC02** | Đăng nhập sai mật khẩu (`LoginTest#testLoginFailureWithWrongPassword`) | `username`: `standard_user`<br>`password`: `wrong_password_123` | Không đăng nhập được, giữ nguyên ở trang đăng nhập và hiển thị thông báo lỗi `"Epic sadface: Username and password do not match any user in this service"`. | Giữ nguyên tại `https://www.saucedemo.com/`, khung thông báo lỗi màu đỏ hiển thị đúng nội dung kỳ vọng. | **Pass** |
| **TC03** | Thêm sản phẩm vào giỏ hàng (`CartTest#testAddProductToCart`) | Tài khoản: `standard_user` / `secret_sauce`<br>Sản phẩm: `Sauce Labs Backpack` | Biểu tượng giỏ hàng hiển thị số lượng `1`. Khi mở giỏ hàng (`cart.html`), hiển thị đúng sản phẩm `"Sauce Labs Backpack"` với số lượng `1`. | Badge giỏ hàng hiển thị `"1"`. Trang `cart.html` hiển thị đúng sản phẩm `"Sauce Labs Backpack"` và số lượng `"1"`. | **Pass** |
| **TC04** | Sắp xếp sản phẩm theo giá tăng dần (`SortingTest#testSortProductsByPriceLowToHigh`) | Tài khoản: `standard_user` / `secret_sauce`<br>Tùy chọn lọc: `Price (low to high)` | Danh sách sản phẩm hiển thị trên trang được sắp xếp theo mức giá tăng dần từ thấp đến cao. | Danh sách 6 sản phẩm hiển thị theo đúng thứ tự giá tăng dần: `[7.99, 9.99, 15.99, 15.99, 29.99, 49.99]`. | **Pass** |
| **TC05** | Đăng xuất khỏi hệ thống (`LogoutTest#testLogoutSuccess`) | Tài khoản: `standard_user` / `secret_sauce` | Sau khi mở menu bên trái và nhấn `Logout`, hệ thống điều hướng về trang đăng nhập `https://www.saucedemo.com/`, hiển thị lại nút `Login` và ô nhập `Username`. | Quay về đúng URL `https://www.saucedemo.com/`, nút `Login` và ô nhập `Username` hiển thị đầy đủ. | **Pass** |

---

## 2. Chi Tiết Từng Test Case

### TC01 — Đăng nhập thành công
- **Lớp / Phương thức:** [`tests.LoginTest#testLoginSuccess`](../src/test/java/tests/LoginTest.java)
- **Tiền điều kiện:** Trình duyệt Chrome được khởi tạo mới, truy cập được `https://www.saucedemo.com/`.
- **Các bước thực hiện:**
  1. Mở trang `https://www.saucedemo.com/`.
  2. Nhập `standard_user` vào ô Username (`By.id("user-name")`).
  3. Nhập `secret_sauce` vào ô Password (`By.id("password")`).
  4. Nhấn nút `Login` (`By.id("login-button")`).
  5. Chờ URL chuyển sang `inventory.html` và phần tử tiêu đề (`By.className("title")`) hiển thị.
- **Điểm xác minh (Assertions):**
  - `assertTrue(currentUrl.contains("inventory.html"))`
  - `assertEquals("Products", pageTitle.getText())`
- **Ảnh minh chứng:** [`screenshots/test-01-login-success.png`](../screenshots/test-01-login-success.png)

### TC02 — Đăng nhập sai mật khẩu
- **Lớp / Phương thức:** [`tests.LoginTest#testLoginFailureWithWrongPassword`](../src/test/java/tests/LoginTest.java)
- **Tiền điều kiện:** Trình duyệt Chrome được khởi tạo mới, truy cập được `https://www.saucedemo.com/`.
- **Các bước thực hiện:**
  1. Mở trang `https://www.saucedemo.com/`.
  2. Nhập `standard_user` vào ô Username (`By.id("user-name")`).
  3. Nhập `wrong_password_123` vào ô Password (`By.id("password")`).
  4. Nhấn nút `Login` (`By.id("login-button")`).
  5. Chờ phần tử thông báo lỗi (`By.cssSelector("[data-test='error']")`) hiển thị.
- **Điểm xác minh (Assertions):**
  - `assertTrue(errorMessage.isDisplayed())`
  - `assertTrue(errorMessage.getText().contains("Epic sadface: Username and password do not match any user in this service"))`
  - `assertEquals("https://www.saucedemo.com/", driver.getCurrentUrl())`
- **Ảnh minh chứng:** [`screenshots/test-02-login-failure.png`](../screenshots/test-02-login-failure.png)

### TC03 — Thêm sản phẩm vào giỏ hàng
- **Lớp / Phương thức:** [`tests.CartTest#testAddProductToCart`](../src/test/java/tests/CartTest.java)
- **Tiền điều kiện:** Đăng nhập thành công với tài khoản `standard_user` / `secret_sauce`.
- **Các bước thực hiện:**
  1. Tại trang danh sách sản phẩm, nhấn nút `Add to cart` của sản phẩm `Sauce Labs Backpack` (`By.id("add-to-cart-sauce-labs-backpack")`).
  2. Kiểm tra số lượng hiển thị trên biểu tượng giỏ hàng (`By.className("shopping_cart_badge")`).
  3. Nhấn vào biểu tượng giỏ hàng (`By.className("shopping_cart_link")`) để mở trang giỏ hàng.
  4. Kiểm tra tên sản phẩm (`By.className("inventory_item_name")`) và số lượng (`By.className("cart_quantity")`) trong giỏ.
- **Điểm xác minh (Assertions):**
  - `assertEquals("1", cartBadge.getText())`
  - `assertTrue(driver.getCurrentUrl().contains("cart.html"))`
  - `assertEquals("Sauce Labs Backpack", productNameInCart.getText())`
  - `assertEquals("1", cartQuantity.getText())`
- **Ảnh minh chứng:** [`screenshots/test-03-cart.png`](../screenshots/test-03-cart.png)

### TC04 — Sắp xếp sản phẩm theo giá tăng dần
- **Lớp / Phương thức:** [`tests.SortingTest#testSortProductsByPriceLowToHigh`](../src/test/java/tests/SortingTest.java)
- **Tiền điều kiện:** Đăng nhập thành công với tài khoản `standard_user` / `secret_sauce`.
- **Các bước thực hiện:**
  1. Tìm dropdown sắp xếp (`By.className("product_sort_container")`).
  2. Chọn tùy chọn `"Price (low to high)"` bằng đối tượng `Select`.
  3. Lấy danh sách toàn bộ các phần tử giá sản phẩm (`By.className("inventory_item_price")`).
  4. Chuyển đổi chuỗi giá (bỏ ký tự `$`) sang danh sách số thực `List<Double>`.
  5. So sánh danh sách giá thực tế với danh sách đã sắp xếp tăng dần.
- **Điểm xác minh (Assertions):**
  - `assertFalse(actualPrices.isEmpty())`
  - `assertEquals(expectedSortedPrices, actualPrices)`
  - `assertTrue(actualPrices.get(i) <= actualPrices.get(i + 1))` với mọi vị trí liên tiếp.
- **Ảnh minh chứng:** [`screenshots/test-04-sorting.png`](../screenshots/test-04-sorting.png)

### TC05 — Đăng xuất
- **Lớp / Phương thức:** [`tests.LogoutTest#testLogoutSuccess`](../src/test/java/tests/LogoutTest.java)
- **Tiền điều kiện:** Đăng nhập thành công với tài khoản `standard_user` / `secret_sauce`.
- **Các bước thực hiện:**
  1. Nhấn nút mở menu bên trái (`By.id("react-burger-menu-btn")`).
  2. Chờ liên kết `Logout` (`By.id("logout_sidebar_link")`) trượt ra hoàn tất và có thể nhấn được (`elementToBeClickable`).
  3. Nhấn chọn `Logout`.
  4. Chờ trang đăng nhập hiển thị lại nút `Login` (`By.id("login-button")`) và ô nhập `Username` (`By.id("user-name")`).
- **Điểm xác minh (Assertions):**
  - `assertEquals("https://www.saucedemo.com/", driver.getCurrentUrl())`
  - `assertTrue(loginButton.isDisplayed())`
  - `assertTrue(usernameInput.isDisplayed())`
- **Ảnh minh chứng:** [`screenshots/test-05-logout.png`](../screenshots/test-05-logout.png)
