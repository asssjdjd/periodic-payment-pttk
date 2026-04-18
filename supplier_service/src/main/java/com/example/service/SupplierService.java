package com.example.service;

import com.example.dto.request.UpdateSupplierRequest;
import com.example.dto.response.ProductResponse;
import com.example.dto.response.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse createSupplier(String name, String email, String phone, String taxCode);

    List<SupplierResponse> getAllSuppliers();

    SupplierResponse updateSupplier(String id, UpdateSupplierRequest request);

    void deleteSupplier(String id);

    List<ProductResponse> getProductsBySupplierId(String supplierId);
}
