-- ==============================================================================
-- 1. THÊM HỢP ĐỒNG VAY (CONTRACT)
-- ==============================================================================
-- Ràng buộc đã có: prepaidAmount <= productPrice (Trả trước <= Giá sản phẩm)
-- Dữ liệu giả định: Mua iPhone 15 giá 30tr, trả trước 10tr, vay 20tr.
INSERT INTO Contract (
    code, customerId, userId, signedDate, loanProductsId, loanContractId,
    productPrice, prepaidAmount, loanAmount, status, created_at, updated_at
) VALUES (
    'HD-20260317-001',   -- code: Mã hợp đồng duy nhất
    1,                   -- customerId: ID của Nguyễn Văn A (đã tạo ở seed trước)
    1,                   -- userId: ID của nhân viên tạo hợp đồng (System Admin)
    '2026-03-17',        -- signedDate: Ngày ký
    1,                   -- loanProductsId: Tham chiếu tới LoanOffer (Gói vay 6 tháng 0%)
    NULL,                -- loanContractId: Hợp đồng cha (NULL vì đây là hợp đồng mới)
    30000000.00,         -- productPrice: Giá sản phẩm (30 triệu)
    10000000.00,         -- prepaidAmount: Trả trước (10 triệu)
    20000000.00,         -- loanAmount: Số tiền vay (20 triệu)
    'ACTIVE',            -- status
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Lấy ID của Hợp đồng vừa tạo (Giả sử là 1) để dùng cho các bảng dưới

-- ==============================================================================
-- 2. THÊM TÀI SẢN THẾ CHẤP (COLLATERAL)
-- ==============================================================================
-- Vì vay mua điện thoại trả góp, tài sản thế chấp thường chính là chiếc điện thoại đó.
INSERT INTO Collateral (
    type, description, valuationValue, contractId, created_at, updated_at
) VALUES (
    'DEVICE',
    'Điện thoại iPhone 15 Pro Max 256GB Titanium, số IMEI: 351234567890123',
    25000000.00,         -- valuationValue: Giá trị định giá (thường thấp hơn giá bán)
    1,                   -- contractId: ID hợp đồng vừa tạo
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Xóa bảng cũ nếu bạn đã lỡ chạy
-- DELETE FROM LoanPaymentSchedule WHERE contractId = 1;

-- ==============================================================================
-- 3. THÊM LỊCH THANH TOÁN (LOAN_PAYMENT_SCHEDULE) - ĐỦ 6 KỲ
-- ==============================================================================
INSERT INTO LoanPaymentSchedule (
    contractId, scheduleId, termNo, dueDate,
    penaltyFee, overdueInterest, overduePrinciple,
    interestDue, principalDue, penaltyDue,
    status,
    penaltyFeePaid, overdueInterestPaid, overduePrinciplePaid,
    interestPaid, principlePaid,
    created_at, updated_at
) VALUES
-- Kỳ 1
(1, NULL, 1, '2026-04-17', 0, 0, 0, 0, 3333333.33, 0, 'PENDING', 0, 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Kỳ 2
(1, NULL, 2, '2026-05-17', 0, 0, 0, 0, 3333333.33, 0, 'PENDING', 0, 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Kỳ 3
(1, NULL, 3, '2026-06-17', 0, 0, 0, 0, 3333333.33, 0, 'PENDING', 0, 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Kỳ 4
(1, NULL, 4, '2026-07-17', 0, 0, 0, 0, 3333333.33, 0, 'PENDING', 0, 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Kỳ 5
(1, NULL, 5, '2026-08-17', 0, 0, 0, 0, 3333333.33, 0, 'PENDING', 0, 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Kỳ 6 (Tháng cuối cộng thêm 2 xu để tổng khớp đúng 20,000,000.00)
(1, NULL, 6, '2026-09-17', 0, 0, 0, 0, 3333333.35, 0, 'PENDING', 0, 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);