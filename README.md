# Hệ Thống Thanh Toán Khoản Vay Theo Kỳ Hạn - Frontend

Giao diện web React + TypeScript cho hệ thống quản lý thanh toán khoản vay theo kỳ hạn.

## Tính Năng

✅ **Tìm kiếm khách hàng** - Tìm theo tên hoặc số CCCD
✅ **Xem danh sách hợp đồng** - Hiển thị các hợp đồng vay hoạt động
✅ **Xem lịch thanh toán** - Chi tiết từng kỳ thanh toán
✅ **Thực hiện thanh toán** - Xử lý thanh toán trực tiếp từ giao diện
✅ **Giao diện responsive** - Hoạt động tốt trên mobile và desktop

## Công Nghệ

- **React 19** - Thư viện UI
- **TypeScript** - Hỗ trợ kiểu dữ liệu tĩnh
- **Vite** - Build tool hiệu năng cao
- **Axios** - HTTP client cho API calls
- **CSS3** - Styling responsive

## Cài Đặt

### Yêu cầu
- Node.js 16+ 
- npm hoặc yarn

### Bước 1: Cài đặt dependencies

```bash
cd periodic-payment-frontend
npm install
```

### Bước 2: Chạy dev server

```bash
npm run dev
```

Giao diện sẽ khả dụng tại: `http://localhost:5173`

### Bước 3: Build cho production

```bash
npm run build
```

## Cấu Trúc Dự Án

```
src/
├── api/
│   └── client.ts           # API client sử dụng axios
├── components/
│   ├── SearchCustomer.tsx  # Component tìm kiếm khách hàng
│   ├── ContractList.tsx    # Component danh sách hợp đồng
│   └── PaymentSchedule.tsx # Component lịch thanh toán
├── styles/
│   ├── SearchCustomer.css  # Style tìm kiếm
│   ├── ContractList.css    # Style hợp đồng
│   └── PaymentSchedule.css # Style lịch thanh toán
├── types/
│   └── index.ts            # TypeScript type definitions
├── App.tsx                 # Component chính
├── App.css                 # Style chung
└── main.tsx                # Entry point
```

## API Integration

Frontend kết nối với backend qua các endpoint sau:

### Tìm Kiếm Khách Hàng (Customer service - port 8082)
- **GET** `http://localhost:8082/api/v1/customers/search?name=...`
- **GET** `http://localhost:8082/api/v1/customers/search-by-cccd?cccd=...`

### Xem Hợp Đồng (Payment service - port 8080)
- **GET** `http://localhost:8080/api/v1/payments/{customerId}/contracts/active`

### Xem Lịch Thanh Toán
- **GET** `http://localhost:8080/api/v1/payments/{customerId}/schedule/{contractId}`

### Thực Hiện Thanh Toán
- **POST** `http://localhost:8080/api/v1/payments/{customerId}/contracts/payment`
  ```json
  {
    "scheduleId": "SCH002",
    "amount": 100000.0
  }
  ```

## Cấu Hình API

API backend mặc định:

- Customer API: `http://localhost:8082/api/v1`
- Payment API: `http://localhost:8080/api/v1`

Để thay đổi, chỉnh sửa file `src/api/client.ts`:

```typescript
const USER_SERVICE_URL = 'http://localhost:8082/api/v1';
const PAYMENT_SERVICE_URL = 'http://localhost:8080/api/v1';
```

## Chạy Backend

Backend cần chạy song song. Xem hướng dẫn trong thư mục `periodic-payment`:

```bash
cd ../periodic-payment
./mvnw spring-boot:run
```

Backend sẽ chạy tại `http://localhost:8080`

## Luồng Sử Dụng

1. **Bước 1:** Tìm kiếm khách hàng theo tên hoặc CCCD
2. **Bước 2:** Chọn khách hàng để xem danh sách hợp đồng
3. **Bước 3:** Chọn hợp đồng để xem chi tiết lịch thanh toán
4. **Bước 4:** Nhập số tiền và thực hiện thanh toán
5. **Bước 5:** Lịch thanh toán sẽ cập nhật sau thanh toán thành công

## Trạng Thái Kỳ Thanh Toán

- 🟢 **PAID** - Đã thanh toán đầy đủ
- 🟡 **PARTIALLY_PAID** - Thanh toán một phần
- 🔴 **PENDING** - Chưa thanh toán
- 🟣 **OVERDUE** - Quá hạn

## Responsive Design

Giao diện được tối ưu cho:
- Desktop (1200px+)
- Tablet (768px - 1199px)
- Mobile (< 768px)

## Troubleshooting

### Lỗi "Connection refused"
- Kiểm tra backend có chạy tại `http://localhost:8080` không
- Kiểm tra CORS configuration trong backend

### Không tìm thấy khách hàng
- Kiểm tra dữ liệu trong database backend
- Kiểm tra từ khóa tìm kiếm có chính xác không

### Lỗi thanh toán
- Kiểm tra số tiền nhập có hợp lệ không (> 0)
- Kiểm tra kỳ thanh toán chưa được thanh toán hoàn tất

## License

MIT

## Hỗ Trợ

Để báo cáo vấn đề hoặc yêu cầu tính năng, vui lòng liên hệ quản trị viên.
      tseslint.configs.stylisticTypeChecked,

      // Other configs...
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```

You can also install [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...
      // Enable lint rules for React
      reactX.configs['recommended-typescript'],
      // Enable lint rules for React DOM
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```
