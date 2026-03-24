-- 1. Thêm Admin User
INSERT INTO User (name, email, phoneNumber, userName, password)
VALUES ('System Admin', 'admin@loan.com', '0987654321', 'admin', 'hashed_password_here');

-- 2. Thêm Khách hàng (Customer & IndividualCustomer)
INSERT INTO Customer (fullName, phoneNumber, creditScore, status)
VALUES ('Nguyen Van A', '0123456789', 800, 'ACTIVE');

-- Lấy ID của khách hàng vừa tạo (Giả sử auto-increment ra ID = 1)
INSERT INTO IndividualCustomer (id, cccd, dob)
VALUES (1, '001090123456', '1990-01-01');

-- 3. Thêm Đối tác (Partner & Supplier)
INSERT INTO Partner (name, taxCode, phoneNumber, address, email)
VALUES ('Cong ty Dien may X', '0312345678', '19001560', 'Ha Noi', 'contact@dienmayx.com');

-- Lấy ID của Partner vừa tạo (ID = 1)
INSERT INTO Supplier (id, contractDate)
VALUES (1, '2026-01-01');

-- 4. Thêm Sản phẩm (Product) thuộc về Supplier trên
INSERT INTO Product (name, importPrice, sellingPrice, status, supplierId)
VALUES ('iPhone 15 Pro Max', 25000000.00, 30000000.00, 'ACTIVE', 1);

-- 5. Thêm Gói vay (LoanOffer) cho Sản phẩm trên (ID = 1)
INSERT INTO LoanOffer (productId, name, interestRate, penaltyRate, overdueInterestRate, overduePrincipleRate, maxAmount, termMonths)
VALUES (1, 'Goi vay tra gop 6 thang 0%', 0.0000, 0.0500, 0.0800, 0.0800, 20000000.00, 6);