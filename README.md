# Sàn Bách Hóa VTC

## 1. Giới thiệu

Đồ án môn học: Trang Bán Sàn Thương Mại Điện Tử VTV

## 2. Thành viên

- Tô Duy Vượng - 20110053
- Nguyễn Quốc Trung - 20110588

[//]: # (- Hà Nhật Vềnh - 20110599)

## 3. Mô tả đồ án

[//]: # (- Đồ án sử dụng công nghệ Spring Boot, Security, Spring Data JPA, Hibernate, MySQL, Restful API, ReactJS, Redux, Axios,)

[//]: # (  Bootstrap, HTML, CSS, JavaScript...)

[//]: # (- Đồ án sử dụng mô hình Client - Server.)

[//]: # (  _~~- Đồ án sử dụng mô hình 3 lớp: Controller, Service, Repository.~~_)


## 4. Các chức năng

- Đăng nhập, đăng ký, đăng xuất.


- Quản lý thông tài khoản:
    - Xem thông tin tài khoản.
    - Cập nhật thông tin tài khoản.
    - Đổi mật khẩu.
    - Lấy lại mật khẩu.
    - Xác nhận tài khoản.
    - Gửi mã xác nhận tài khoản.


- Quản lý địa chỉ:
    - Thêm địa chỉ.
    - Xem chi tiết địa chỉ.
    - Cập nhật thông tin chi tiết địa chỉ.
    - Chọn trạng thái địa chỉ.
    - Xóa địa chỉ.
    - Xem danh sách địa chỉ.


- Quản lý cửa hàng:
    - Thêm cửa hàng.
    - Xem chi tiết cửa hàng.
    - Cập nhật thông tin chi tiết cửa hàng.
    - Chọn trạng thái cửa hàng.


- Quản lý danh mục từ quyền manager:
    - Thêm danh mục.
    - Cập nhật thông tin danh mục.
    - Xóa danh mục.


- Quản lý thương hiệu sản phẩm từ quyền admin:
    - Thêm thương hiệu bằng quyền admin.
    - Xem danh sách thương hiệu.
    - Xem chi tiết thương hiệu.
    - Cập nhật thông tin thương hiệu.
    - Cập nhật trạng thái thương hiệu.


- Quản lý danh mục từ quyền vendor:
    - Thêm danh mục bằng quyền vendor.
    - Xem danh sách danh mục của vendor.
    - Xem danh sách danh mục parent của admin.
    - Xem chi tiết danh mục.
    - Cập nhật thông tin danh mục.
    - Chọn trạng thái danh mục.


- Quản lý thuộc tính từ quyền vendor:
    - Thêm thuộc tính bằng quyền vendor.
    - Xem danh sách thuộc tính của vendor.
    - Xem chi tiết thuộc tính.
    - Cập nhật thông tin thuộc tính.
    - Khóa, mở khóa thuộc tính.
    - Xóa thuộc tính.



- Quản lý sản phẩm từ quyền vendor:
    - Thêm sản phẩm bằng quyền vendor.
    - Xem danh sách sản phẩm của vendor.
    - Xem chi tiết sản phẩm.
    - Cập nhật thông tin sản phẩm.
    - Chọn trạng thái sản phẩm.
    - Xem danh sách sản phẩm đã xóa.
    - Khôi phục sản phẩm đã xóa.
    - Xem danh sách sản phẩm theo danh mục.
    - Xem danh sách sản phẩm bán chạy.
    - Xem danh sách sản phẩm mới nhất.
    - Tim kiếm sản phẩm theo tên.
    - Xem danh sách sản phẩm theo giá.


- Xem sản phẩm:
    - Xem chi tiết sản phẩm.
    - Xem danh sách sản phẩm theo danh mục.
    - Xem danh sách sản phẩm bán chạy.
    - Xem danh sách sản phẩm mới nhất.
    - Tim kiếm sản phẩm theo tên.
    - Xem danh sách sản phẩm theo giá.
    - Tính số yêu thích của sản phẩm.


- Yêu thích sản phẩm:
    - Thêm sản phẩm vào danh sách yêu thích.
    - Chi tiết sản phẩm yêu thích.
    - Xem danh sách sản phẩm yêu thích.
    - Xóa sản phẩm khỏi danh sách yêu thích.


- Theo dõi cửa hàng:
    - Thêm cửa hàng vào danh sách theo dõi.
    - Xem danh sách cửa hàng theo dõi.
    - Xóa cửa hàng khỏi danh sách theo dõi.
    - Tính số theo dõi của cửa hàng.


- Xem cửa hàng:
    - Xem chi tiết cửa hàng.
    - Xem danh sách sản phẩm của cửa hàng.
    - Xem danh sách sản phẩm bán chạy của cửa hàng.
    - Xem danh sách sản phẩm mới nhất của cửa hàng.
    - Tim kiếm sản phẩm theo tên của cửa hàng.
    - Xem danh sách sản phẩm theo giá của cửa hàng.


- Quản lý giỏ hàng:
    - Thêm sản phẩm vào giỏ hàng.
    - Xem danh sách sản phẩm trong giỏ hàng theo cửa hàng.
    - Xem danh sách sản phẩm trong giỏ hàng theo mã giỏ hàng.
    - Cập nhật số lượng sản phẩm trong giỏ hàng.
    - Xóa sản phẩm khỏi giỏ hàng.
    - Xóa sản phẩm khỏi giỏ hàng theo cửa hàng.


- Quản lý mã giảm giá từ quyền admin:
    - Thêm mã giảm giá bằng quyền admin.
    - Xem danh sách mã giảm giá theo trạng thái và loại mã giảm giá.
    - Xem chi tiết mã giảm giá.
    - Cập nhật thông tin mã giảm giá.
    - Chọn trạng thái mã giảm giá.


- Quản lý mã giảm giá từ quyền vendor:
    - Thêm mã giảm giá bằng quyền vendor.
    - Xem danh sách mã giảm giá theo trạng thái, loại mã giảm giá và cửa hàng.
    - Xem chi tiết mã giảm giá.
    - Cập nhật thông tin mã giảm giá.
    - Chọn trạng thái mã giảm giá.


- Xem mã giảm giá từ quyền khách:
    - Xem danh sách mã giảm giá theo trạng thái và loại mã giảm giá.
    - Xem chi tiết mã giảm giá.
    - Xem danh sách mã giảm giá theo cửa hàng.
    - Xem danh sách tất cả mã giảm giá.


- Quản lý mã giảm giá từ quyền customer:
    - Lưu mã giảm giá.
    - Xem danh sách mã giảm giá đã lưu.


- Quản lý đơn hàng của customer:
    - Tạo đơn hàng tạm thời với mã giỏ hàng.
    - Cập nhật đơn hàng tạm thời với:
        - Thông tin địa chỉ nhận hàng.
        - Thông tin mã giảm giá.
        - Thông tin phương thức thanh toán.
        - Thông tin phương thức vận chuyển.
        - Thông tin ghi chú.
    - Đặt hàng với đơn hàng tạm thời.
    - Xem danh sách đơn hàng đã đặt.
    - Xem danh sách đơn theo trạng thái.
    - Xem chi tiết đơn hàng đã đặt.
    - Hủy đơn hàng đã đặt.


- Quản lý đơn hàng của vender:
    - Xem danh sách đơn hàng theo trạng thái.
    - Xem chi tiết đơn hàng đã đặt.
    - Cập nhật trạng thái đơn hàng.
    - Xem danh sách đơn hàng theo thời gian.
    - Xem danh sách đơn hàng theo thời gian và trạng thái.


- Quản lý đánh giá của customer:
    - Thêm đánh giá.
    - Xóa đánh giá.


- Quản lý đánh giá:
    - Xem danh sách đánh giá theo sản phẩm.


- Quản lý bình luận:
    - Thêm bình luận.
    - Xóa bình luận.
    - Tính số bình luận của sản phẩm.
    - Tính điểm đánh giá của sản phẩm.


- Thống kê doanh thu từ quyền vender:
    - Xem doanh thu theo thời gian.



- Phân trang sản phẩm:
    - Xem danh sách sản phẩm theo danh mục.
    - Xem danh sách sản phẩm theo cửa hàng.
    - Xem danh sách sản phẩm bán chạy theo cửa hàng.
    - Xem danh sách sản phẩm mới nhất theo cửa hàng.
    - Tim kiếm sản phẩm theo tên và lọc sản phẩm theo giá.
    - Tim kiếm sản phẩm theo tên và lọc sản phẩm.
    - Xem danh sách sản phẩm theo giá theo cửa hàng.
    - Xem danh sách sản phẩm theo giá.
    - Lọc sản phẩm theo giá.
    - //Xem danh sách sản phẩm theo thương hiệu.



- Quản lý khách hàng:
    - Xem danh sách khách hàng theo trạng thái.
    - Xem danh sách khách hàng theo trạng thái và sắp xếp theo tên, thời gian.
    - Tìm kiếm khách hàng theo tên, trạng thái.



- //Xem chi tiết khách hàng.
- //Xóa khách hàng.
  -// Xem danh sách khách hàng theo thời gian.
- //Xem danh sách khách hàng theo thời gian và trạng thái.
-
- //Xem danh sách khách hàng theo tên.
- //Xem danh sách khách hàng theo tên và trạng thái.
- //Xem danh sách khách hàng theo tên và thời gian.
- //Xem danh sách khách hàng theo tên, thời gian và trạng thái.
- //Xem danh sách khách hàng theo tên và thời gian.
- // danh sách khách hàng theo tên, thời gian và trạng thái.



- Quản lý nhân viên từ quyền admin:
    - Thêm nhân viên.
    - Xem danh sách nhân viên.
    - Tìm kiếm nhân viên theo tên.
    - Xóa nhân viên.


- Quản lý sản phẩm từ quyền nhân viên:

[//]: # (    - Khóa sản phẩm.)

[//]: # (    - Mở khóa sản phẩm.)

[//]: # (    - Lấy danh sách sản phẩm bị khóa.)

[//]: # (    - Tìm kiếm sản phẩm bị khóa theo tên sản phẩm.)



- Quản lý location province:
    - Lấy danh sách tỉnh thành.

- Quản lý location district:
    - Lấy danh sách quận huyện theo tỉnh thành.

- Quản lý location ward:
    - Lấy danh sách phường xã theo quận huyện.
    - Lấy thông tin chi tiết location theo phường xã.



- Quản lý quyền nhân viên từ quyền manger:
    - Thêm quyền nhân viên.
    - Cập nhật quyền nhân viên.
    - Xem danh sách quyền nhân viên theo quyền.
    - Xem danh sách quyền nhân viên theo trạng thái.
    - Xem danh sách quyền nhân viên theo tài khoản và trạng thái.
    - Xóa quyền nhân viên.


[//]: # (có thên thêmn nhà cung cấp vận chuyển)



- Trò chuyện với customer:
  - Tạo cuộc trò chuyện với customer.
  - Xem danh sách cuộc trò chuyện.
  - Xem chi tiết cuộc trò chuyện.
  - Gửi tin nhắn.

- Quản lý nhà cung cấp vận chuyển từ quyền manager:
    - Thêm nhà cung cấp vận chuyển.
    - Cập nhật khu vực làm việc của nhà cung cấp vận chuyển.


- Xem thông tin nhà cung cấp vận chuyển từ quyền customer:
    - Xem danh sách nhà cung cấp vận chuyển đầy đủ thông tin.
    - Xem danh sách nhà cung cấp vận chuyển rút gọn thông tin.
    - Xem chi tiết nhà cung cấp vận chuyển.


- Quản lý nhà vận chuyển từ quyền provider:
  - Thêm nhân viên vận chuyển.
  - Cập nhật khu vực làm việc của nhân viên vận chuyển.
  - Cập nhật trạng thái nhân viên vận chuyển.
  - Xem danh sách nhân viên vận chuyển theo trạng thái.
  - Xem danh sách nhân viên vận chuyển theo trạng thái và loại công việc.


- Tìm kiếm sản phẩm(nếu có token thì sẻ lưu lịch sử tìm kiếm)
  - Tìm kiếm sản phẩm theo tên và lọc sản phẩm theo mới nhất, bán chạy, giá tăng dần, giá giảm dần, ngẫu nhiên.
  - Tìm kiếm sản phẩm theo tên, khoảng giá và lọc sản phẩm theo mới nhất, bán chạy, giá tăng dần, giá giảm dần, ngẫu nhiên.
  - Tìm kiếm sản phẩm theo tên, trong cửa hàng và lọc sản phẩm theo mới nhất, bán chạy, giá tăng dần, giá giảm dần, ngẫu nhiên.
  - Tìm kiếm sản phẩm theo tên, trong cửa hàng, khoảng giá và lọc sản phẩm theo mới nhất, bán chạy, giá tăng dần, giá giảm dần, ngẫu nhiên.


- Gợi ý sản phẩm cửa Customer:
  - Theo lịch sử tìm kiếm.

[//]: # (  - Gợi ý sản phẩm theo sản phẩm đã xem.)
[//]: # (  - Gợi ý sản phẩm theo sản phẩm yêu thích.)
[//]: # (  - Gợi ý sản phẩm theo sản phẩm đã mua.)


- Gợi ý sản phẩm cửa Guest:
  - Theo ngẫu nhiên.
  - Theo tên sản phẩm trên toàn hệ thống và cửa hàng. (inShop = true -> cửa hàng, inShop = false -> toàn hệ thống)
  - Theo mã danh mục con, cha. (truyền category id server tự phân loại)


- Quản lý thông báo:
  - Xem danh sách thông báo.
  - Đọc thông báo.
  - Xóa thông báo.

- Quản lý vận chuyển:
  - Tính thông tin vận chuyển(dựa vào wardCode và shippingProvider).
  - Lấy danh sách đơn vị vẩn chuyển và tính phí vận chuyển.(dựa vào wardCode)

- Xem danh mục sản phẩm:
  - Xem danh sách danh mục con theo danh mục cha.
  - Xem danh sách danh mục cha.































































