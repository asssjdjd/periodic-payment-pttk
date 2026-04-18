package com.example.controller;

import com.example.dto.request.CreateSupplierRequest;
import com.example.dto.request.UpdateSupplierRequest;
import com.example.dto.response.ProductResponse;
import com.example.dto.response.SupplierResponse;
import com.example.dto.response.SuccessResponse;
import com.example.dto.response.ApiResponse;
import com.example.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    private static final Logger log = LoggerFactory.getLogger(SupplierController.class);

    @PostMapping("")
    public ApiResponse createSupplier(@RequestBody CreateSupplierRequest request) {
        log.info("[Supplier Service] : Create Supplier with email : {}; name : {}; phone : {}",request.getEmail(), request.getName(), request.getPhone());

        SupplierResponse response = supplierService.createSupplier(request.getName(),request.getEmail(),request.getPhone(),request.getTaxCode());

        return new SuccessResponse(response,"Tạo thành công nhà cung cấp mới");
    }

    // --- 2. XEM TOÀN BỘ ---
    @GetMapping("")
    public ApiResponse getAllSuppliers() {
        log.info("[Supplier Service] : Get all suppliers");
        List<SupplierResponse> response = supplierService.getAllSuppliers();
        return new SuccessResponse(response, "Lấy danh sách nhà cung cấp thành công");
    }

    // --- 3. CẬP NHẬT ---
    @PutMapping("/{id}")
    public ApiResponse updateSupplier(@PathVariable("id") String id, @RequestBody UpdateSupplierRequest request) {
        log.info("[Supplier Service] : Update Supplier ID: {}", id);
        SupplierResponse response = supplierService.updateSupplier(id, request);
        return new SuccessResponse(response, "Cập nhật nhà cung cấp thành công");
    }

    // --- 4. XÓA (Soft Delete) ---
    @DeleteMapping("/{id}")
    public ApiResponse deleteSupplier(@PathVariable("id") String id) {
        log.info("[Supplier Service] : Delete Supplier ID: {}", id);
        supplierService.deleteSupplier(id);
        return new SuccessResponse(null, "Xóa nhà cung cấp thành công");
    }

    // API 5: Get toàn bộ sản phẩm của một nhà cung cấp
    @GetMapping("/{id}/products")
    public ApiResponse getProductsBySupplier(@PathVariable("id") String supplierId) {
        log.info("[Supplier Service] : Get all products for Supplier ID: {}", supplierId);
        List<ProductResponse> response = supplierService.getProductsBySupplierId(supplierId);
        return new SuccessResponse(response, "Lấy danh sách sản phẩm của nhà cung cấp thành công");
    }
}
