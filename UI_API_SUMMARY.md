# Tóm tắt giao diện, trường dữ liệu, nút bấm và hàm xử lý

## Tổng quan luồng giao diện (`App.tsx`)
- **Màn hình chính**:
  - Nút: **“Xem Thống Kê Dư Nợ”** → bật chế độ thống kê (`showDebtStatistics`).
  - Khối tìm kiếm khách hàng: `SearchCustomer`.
- **Màn hình danh sách hợp đồng** (khi đã chọn khách hàng):
  - Nút: **“← Quay Lại”** → quay lại tìm kiếm.
  - Hiển thị `ContractList`.
- **Màn hình lịch thanh toán** (khi đã chọn hợp đồng):
  - Nút: **“← Quay Lại”** → quay lại danh sách hợp đồng.
  - Hiển thị `PaymentSchedule`.
- **Màn hình thống kê dư nợ**:
  - Nút: **“← Quay Lại”** → quay lại màn hình chính.
  - Hiển thị `CustomerDebtStatistics`.

## 1) Tìm kiếm khách hàng (`SearchCustomer.tsx`)
### Trường dữ liệu (inputs)
- **Loại tìm kiếm**: dropdown `select` (`searchType`)
  - Giá trị: `name` (Tìm theo tên) hoặc `cccd` (Tìm theo CCCD)
- **Từ khóa**: input `text` (`searchValue`)
  - Placeholder thay đổi theo loại tìm kiếm

### Nút bấm
- **“Tìm Kiếm”** → chạy `handleSearch()`
- **“Chọn Khách Hàng”** (trên mỗi khách hàng) → gọi `onCustomerSelect(customer)`

### Hàm xử lý & gọi API
- `handleSearch()`
  - Kiểm tra input rỗng → báo lỗi
  - Gọi API:
    - `customerApi.searchByName(name)` **hoặc** `customerApi.searchByCccd(cccd)`
  - Nếu `response.code === 200`: cập nhật `customers`, nếu rỗng → báo “Không tìm thấy khách hàng”
  - Nếu lỗi: hiển thị `error`

### Dữ liệu hiển thị
- `Customer`: `fullName`, `phoneNumber`, `cccd`, `creditScore`, `status`

---

## 2) Danh sách hợp đồng (`ContractList.tsx`)
### Trường dữ liệu hiển thị
- **Mã hợp đồng**: `contract.code`
- **Trạng thái**: `contract.status`
- **Số tiền vay**: `contract.loanAmount`
- **Ngày ký hợp đồng**: `contract.signedDate`
- **Gói vay**: `contract.loanOffer?.name` hoặc số kỳ trả góp
- **Lãi suất**: `loanOffer?.interestRate` hoặc `paymentSchedules[0]?.interestDueRate`
- **Phí phạt**: `loanOffer?.penaltyRate` hoặc `paymentSchedules[0]?.penaltyFee`
- **Lãi suất quá hạn**: `loanOffer?.overdueInterestRate` hoặc `paymentSchedules[0]?.overdueInterestRate`
- **Kỳ hạn**: `loanOffer?.termMonths` hoặc số kỳ
- **Số kỳ còn lại**: số schedule chưa `PAID`

### Nút bấm
- **“Thanh Toán”** → gọi `onContractSelect(contract)`

### Hàm xử lý & gọi API
- `useEffect(..., [customerId])`
  - Gọi `customerApi.getActiveContracts(customerId)`
  - Nếu `response.code === 200` → set `contracts`
  - Nếu lỗi → set `error`

---

## 3) Lịch thanh toán & thanh toán (`PaymentSchedule.tsx`)
### Trường dữ liệu hiển thị
- **Danh sách kỳ thanh toán** (mỗi `LoanPaymentSchedule`):
  - Kỳ (`termNo`), hạn thanh toán (`dueDate`), trạng thái (`status`)
  - **Gốc còn**: `principalDue`
  - **Lãi còn**: `interestDue`
  - **Phí phạt**: `penaltyDue | penaltyFee`
  - **Lãi quá hạn**: `overdueInterest`
  - **Gốc quá hạn**: `overduePrinciple`
  - **Tổng còn phải trả**: tổng các khoản trên
  - **Đã trả**: `principlePaid`, `interestPaid`

### Trường nhập
- **Số tiền thanh toán**: input `number` (`paymentAmount`)
  - Có nút **“Max”** để điền số tiền tối đa còn phải trả

### Nút bấm
- **“Xem Tất Cả Lịch Thanh Toán”** / **“Ẩn Lịch Thanh Toán”** → bật/tắt `showAllSchedules`
- **“Thanh Toán”** → chạy `handlePayment()`

### Hàm xử lý & gọi API
- `useEffect(..., [customerId, contractId])`
  - Gọi `customerApi.getPaymentSchedule(customerId, contractId)`
  - Nếu OK → set `schedules` và chọn kỳ đầu tiên
- `handlePayment()`
  - Validate input và số tiền > 0
  - Tính `totalRemaining`
  - Kiểm tra số tiền không vượt `totalRemaining`
  - Gọi `customerApi.executePayment(customerId, { scheduleId, amount })`
  - Nếu OK → reload lịch bằng `customerApi.getPaymentSchedule(...)` và gọi `onPaymentComplete()`

---

## 4) Thống kê dư nợ khách hàng (`CustomerDebtStatistics.tsx`)
### Trường dữ liệu lọc
- **Tổng dư nợ từ** (`minDebt`) – input number
- **Tổng dư nợ đến** (`maxDebt`) – input number
- **Từ ngày** (`fromDate`) – input date
- **Đến ngày** (`toDate`) – input date

### Nút bấm
- **“Tìm kiếm”** → `handleFilter()`
- **“Xóa bộ lọc”** → `handleClearFilters()`
- **Mũi tên mở rộng hợp đồng** (click trên hợp đồng) → `toggleContractDetails(...)`

### Hàm xử lý & gọi API
- `useEffect(...)` khi mount
  - Gọi `statisticsApi.getOutstandingDebtDetail()`
  - Nếu OK → set `items` và `filteredItems`
- `handleFilter()`
  - Lọc theo `minDebt`, `maxDebt`, `fromDate`, `toDate`
- `handleClearFilters()`
  - Reset các bộ lọc

### Dữ liệu hiển thị
- **Tổng dư nợ**, **Gốc còn**, **Lãi còn**, **Phạt**, **Quá hạn** theo từng khách hàng
- Chi tiết theo từng hợp đồng và từng kỳ thanh toán (mở rộng)

---

## 5) Các hàm gọi API (`src/api/client.ts`)
### `customerApi`
- `searchByName(name: string)` → `GET /customers/search?name=`
- `searchByCccd(cccd: string)` → `GET /customers/search-by-cccd?cccd=`
- `getActiveContracts(customerId: string)` → `GET /payments/{customerId}/contracts/active`
- `getPaymentSchedule(customerId, contractId)` → `GET /payments/{customerId}/schedule/{contractId}`
  - Chuẩn hóa dữ liệu: `penaltyDue`, `overduePrinciple`, `overduePrinciplePaid`
- `executePayment(customerId, request)` → `POST /payments/{customerId}/contracts/payment`
  - Body: `{ scheduleId, amount }`

### `statisticsApi`
- `getOutstandingDebtDetail()` → `POST /statistics/customer/outstanding-debt/detail`
  - Body mặc định: `{ fromDate: null, endDate: null, minDebt: null, maxDebt: null }`

---

## 6) Các kiểu dữ liệu chính (`src/types/index.ts`)
- `Customer`, `Contract`, `LoanPaymentSchedule`, `PaymentRequest`
- `CustomerDebtStatistic`, `DebtStatisticContract`, `DebtStatisticSchedule`
- `ApiResponse<T>`

> File này tổng hợp nhanh các trường giao diện, các nút thao tác, và các hàm xử lý/gọi dữ liệu theo đúng code hiện tại.