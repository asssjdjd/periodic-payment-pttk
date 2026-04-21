package com.example.service.Impl;

import com.example.dto.request.UpdateSupplierRequest;
import com.example.dto.response.ProductResponse;
import com.example.dto.response.SupplierResponse;
import com.example.entity.Product;
import com.example.entity.Supplier;
import com.example.repository.ProductRepository;
import com.example.repository.SupplierRepository;
import com.example.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Override
    public SupplierResponse createSupplier(String name, String email, String phone, String taxCode) {
        Supplier createSupplier = Supplier.builder()
                .email(email)
                .name(name)
                .phone(phone)
                .taxCode(taxCode)
                .build();
        createSupplier.setCreatedAt(LocalDateTime.now());

        Supplier saveSupplier = supplierRepository.save(createSupplier);
        log.info("[Supplier Service] [SupplierServiceImpl] : Lưu thành công nhà cung cấp");

        return mapToResponse(saveSupplier);
    }

    @Override
    public List<SupplierResponse> getAllSuppliers() {
        // Thực tế nên lọc các bản ghi chưa bị xóa: findByDeletedAtIsNull()
        List<Supplier> suppliers = supplierRepository.findAll();

        return suppliers.stream()
//                .filter(s -> s.getDeletedAt() == null) // Bỏ qua các bản ghi đã xóa mềm
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public SupplierResponse updateSupplier(String id, UpdateSupplierRequest request) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với ID: " + id));

        // Cập nhật thông tin
        existingSupplier.setName(request.getName());
        existingSupplier.setEmail(request.getEmail());
        existingSupplier.setPhone(request.getPhone());
        existingSupplier.setTaxCode(request.getTaxCode());
        existingSupplier.setUpdatedAt(LocalDateTime.now()); // Cập nhật thời gian sửa

        if(request.getStatus().equals("ACTIVE")) {
            existingSupplier.setDeletedAt(null);
        }else {
            existingSupplier.setDeletedAt(LocalDateTime.now());
        }

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        log.info("[Supplier Service] [SupplierServiceImpl] : Cập nhật thành công nhà cung cấp ID: {}", id);

        return mapToResponse(updatedSupplier);
    }

    @Override
    public void deleteSupplier(String id) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với ID: " + id));

        // SOFT DELETE: Cập nhật trường deletedAt thay vì xóa hẳn khỏi Database
        existingSupplier.setDeletedAt(LocalDateTime.now());
        supplierRepository.save(existingSupplier);

        log.info("[Supplier Service] [SupplierServiceImpl] : Đã xóa mềm (Soft Delete) nhà cung cấp ID: {}", id);
    }

    // --- HÀM MAPPER DÙNG CHUNG ---
    // Tách riêng hàm này để code không bị lặp lại ở các hàm Create, Update, GetAll
    private SupplierResponse mapToResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .email(supplier.getEmail())
                .taxCode(supplier.getTaxCode())
                .phone(supplier.getPhone())
                .name(supplier.getName())
                .deletedAt(supplier.getDeletedAt())
                .build();
    }

    @Override
    public List<ProductResponse> getProductsBySupplierId(String supplierId) {
        // Kiểm tra xem NCC có tồn tại không
        supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp"));

        // Lấy danh sách sản phẩm (không trùng lặp)
        List<Product> products = productRepository.findDistinctProductsBySupplierId(supplierId);

        return products.stream()
                .map(p -> ProductResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .status(p.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
