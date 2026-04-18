# FRONTEND - DANH SÁCH FILE ĐÃ ĐƯỢC TẠO

## Cấu Trúc Thư Mục Frontend

```
periodic-payment-frontend/
├── src/
│   ├── api/
│   │   └── client.ts                 # ✅ API client (axios configuration)
│   ├── components/
│   │   ├── SearchCustomer.tsx        # ✅ Component tìm kiếm khách hàng
│   │   ├── ContractList.tsx          # ✅ Component danh sách hợp đồng
│   │   └── PaymentSchedule.tsx       # ✅ Component lịch thanh toán
│   ├── styles/
│   │   ├── SearchCustomer.css        # ✅ Style tìm kiếm
│   │   ├── ContractList.css          # ✅ Style danh sách hợp đồng
│   │   └── PaymentSchedule.css       # ✅ Style lịch thanh toán
│   ├── types/
│   │   └── index.ts                  # ✅ TypeScript type definitions
│   ├── App.tsx                       # ✅ Component chính (main app)
│   ├── App.css                       # ✅ Style chung app
│   ├── index.css                     # (original global styles)
│   └── main.tsx                      # (original entry point)
├── package.json                      # ✅ Updated với axios dependency
├── README.md                         # ✅ Updated documentation
├── vite.config.ts                    # (Vite configuration)
├── tsconfig.json                     # (TypeScript configuration)
└── index.html                        # (HTML entry point)
```

## Backend - FILE ĐÃ ĐƯỢC THÊM

```
periodic-payment/
└── src/main/java/com/example/periodic_payment/config/
    └── CorsConfig.java               # ✅ CORS configuration để cho phép frontend
```

## Chi Tiết Các File Được Tạo

### 1. API Client (`src/api/client.ts`)
- Sử dụng Axios để gọi backend API
- 5 phương thức chính:
  - `searchByName()` - Tìm khách hàng theo tên
  - `searchByCccd()` - Tìm khách hàng theo CCCD
  - `getActiveContracts()` - Lấy danh sách hợp đồng
  - `getPaymentSchedule()` - Lấy lịch thanh toán
  - `executePayment()` - Thực hiện thanh toán

### 2. Components

#### SearchCustomer.tsx
- Tìm kiếm khách hàng theo tên hoặc CCCD
- Hiển thị danh sách kết quả
- Cho phép chọn khách hàng

#### ContractList.tsx
- Hiển thị danh sách hợp đồng của khách hàng
- Thông tin chi tiết hợp đồng
- Cho phép chọn hợp đồng để xem lịch thanh toán

#### PaymentSchedule.tsx
- Hiển thị lịch thanh toán từng kỳ
- Chi tiết số tiền còn phải trả
- Form nhập số tiền thanh toán
- Xử lý thanh toán

### 3. Styles (CSS)
- Responsive design cho mobile/tablet/desktop
- Color scheme chuyên nghiệp
- Accessible UI elements

### 4. Types (TypeScript)
- Định nghĩa tất cả interfaces cho data:
  - Customer
  - Contract
  - LoanPaymentSchedule
  - LoanOffer
  - Collateral
  - User
  - ApiResponse
  - PaymentRequest

### 5. App.tsx (Main Component)
- State management cho toàn bộ app
- Navigation giữa các view
- Handler cho user interactions

### 6. CorsConfig.java (Backend)
- Cho phép frontend (localhost:5173) truy cập backend API
- Cấu hình CORS headers

## Feature Chính

✅ **Tìm Kiếm Khách Hàng**
- Tìm theo tên hoặc CCCD
- Hiển thị thông tin chi tiết

✅ **Xem Hợp Đồng**
- Danh sách hợp đồng hoạt động
- Thông tin sản phẩm vay
- Tỷ lệ lãi suất

✅ **Xem Lịch Thanh Toán**
- Chi tiết từng kỳ
- Số tiền còn phải trả
- Tiền lãi, gốc, phí phạt, lãi quá hạn

✅ **Thanh Toán**
- Form nhập số tiền
- Xác nhận thanh toán
- Cập nhật trạng thái ngay sau thanh toán

✅ **Giao Diện Responsive**
- Desktop: 1200px+
- Tablet: 768px - 1199px
- Mobile: < 768px

## Cài Đặt Nhanh

```bash
# Terminal 1: Chạy Backend
cd c:\Users\Long\Desktop\periodic-payment
.\mvnw spring-boot:run

# Terminal 2: Chạy Frontend
cd c:\Users\Long\Desktop\periodic-payment-frontend
npm install
npm run dev
```

Truy cập: `http://localhost:5173`

## Hướng Dẫn Sửa Đổi

### Thay đổi API URL
File: `src/api/client.ts`
```typescript
const API_BASE_URL = 'http://localhost:8080/api/v1'; // Thay đổi ở đây
```

### Thêm Component Mới
1. Tạo file `.tsx` trong `src/components/`
2. Tạo file `.css` trong `src/styles/`
3. Import vào `App.tsx`

### Cấu hình CORS
File: `src/main/java/com/example/periodic_payment/config/CorsConfig.java`
```java
.allowedOrigins("http://localhost:5173", "http://localhost:3000") // Thêm origins ở đây
```

## Kiểm Tra Lỗi

### Frontend Errors
- Mở DevTools: F12
- Xem Console tab
- Xem Network tab để check API calls

### Backend Errors
- Xem terminal backend
- Kiểm tra application.yaml
- Xem MySQL logs

## Production Build

```bash
# Frontend
cd periodic-payment-frontend
npm run build
# Output: dist/ folder

# Backend
cd periodic-payment
.\mvnw clean package
# Output: target/periodic-payment-0.0.1-SNAPSHOT.jar
```

## License

MIT - Tự do sử dụng và chỉnh sửa

---

**Ngày tạo:** 2026-03-25
**Phiên bản:** 1.0.0
