package com.example.service.Impl;

import com.example.dto.response.CustomerResponse;
import com.example.exception.ExceptionCode;
import com.example.exception.ResourceException;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import com.example.service.CustomerService;
import com.example.service.search.Impl.SearchByCccdImpl;
import com.example.service.search.Impl.SearchByNameImpl;
import com.example.service.search.SearchStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final String SEARCH_STRATEGY_BY_NAME = "NAME";
    private final String SEARCH_STRATEGY_BY_CCCD = "CCCD";

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true) // tăng hiệu quả đọc ghi
    public List<CustomerResponse> findAllCustomer(String name, String cccd) {
        log.info("[Customer Service] : Thực hiện tìm kiếm khách hàng với từ name : {}; cccd : {}", name, cccd);
        SearchStrategy searchStrategy = null;
        
        if(name != null) {
            searchStrategy = setStrategy(SEARCH_STRATEGY_BY_NAME);
        }else if(cccd != null) {
            searchStrategy = setStrategy(SEARCH_STRATEGY_BY_CCCD);
        }

        List<Customer> customers = searchStrategy.searchCustomer(name,cccd);

        if(customers.isEmpty()) {
            throw new ResourceException(400, "Vui lòng cung cấp tham số tìm kiếm");
        }

        return customers.stream()
                .map(CustomerResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public List<Customer> validate(String name, String password) {
       return customerRepository.findByFullNameAndPassword(name,password);
    }


    private SearchStrategy setStrategy(String strategy) {
        if(SEARCH_STRATEGY_BY_NAME.equals(strategy)) {
            return new SearchByNameImpl(customerRepository);
        }else if(SEARCH_STRATEGY_BY_CCCD.equals(strategy)) {
            return new SearchByCccdImpl( customerRepository);
        }else {
            throw new ResourceException(503,"Không có phương thức tìm kiếm phù hợp");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        log.info("[Customer Service] : Thực hiện lấy toàn bộ danh sách khách hàng");

        List<Customer> customers = customerRepository.findAll();

        // Đối với lấy toàn bộ, nếu rỗng thì trả về mảng rỗng [] thường tốt hơn là ném lỗi 404
        if (customers.isEmpty()) {
            log.warn("[Customer Service] : Database chưa có dữ liệu khách hàng nào");
            return Collections.emptyList();
        }

        return customers.stream()
                .map(CustomerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(String id) {
        log.info("[Customer Service] : Thực hiện tìm khách hàng theo id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Customer Service] : Không tìm thấy khách hàng với id: {}", id);
                    return new ResourceException(
                            ExceptionCode.USER_NOT_FOUND.getCode(),
                            ExceptionCode.USER_NOT_FOUND.getMessage()
                    );
                });

        return CustomerResponse.fromEntity(customer);
    }
}
