package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Customer;
import com.minhthang.management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public ApiResponse<List<Customer>> getAll() {
        return ApiResponse.success("Lấy danh sách khách hàng thành công", customerRepository.findAll());
    }

    public ApiResponse<List<Customer>> search(String q) {
        return ApiResponse.success("Tìm kiếm khách hàng thành công", customerRepository.search(q));
    }

    public ApiResponse<Customer> getById(String id) {
        return customerRepository.findById(id)
                .map(c -> ApiResponse.success("Lấy thông tin khách hàng thành công", c))
                .orElse(ApiResponse.error("Không tìm thấy khách hàng"));
    }

    @Transactional
    public ApiResponse<Customer> create(Customer customer) {
        Customer saved = customerRepository.save(customer);
        return ApiResponse.success("Tạo khách hàng thành công", saved);
    }

    @Transactional
    public ApiResponse<Customer> update(String id, Customer customer) {
        return customerRepository.findById(id).map(existing -> {
            existing.setName(customer.getName());
            existing.setCode(customer.getCode());
            existing.setContactPerson(customer.getContactPerson());
            existing.setPhone(customer.getPhone());
            existing.setEmail(customer.getEmail());
            existing.setAddress(customer.getAddress());
            existing.setCreditLimit(customer.getCreditLimit());
            existing.setCurrentBalance(customer.getCurrentBalance());
            existing.setSalesStaff(customer.getSalesStaff());
            return ApiResponse.success("Cập nhật khách hàng thành công", customerRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy khách hàng"));
    }

    @Transactional
    public ApiResponse<Customer> toggleActive(String id) {
        return customerRepository.findById(id).map(existing -> {
            existing.setActive(!existing.isActive());
            return ApiResponse.success(
                    existing.isActive() ? "Kích hoạt khách hàng thành công" : "Vô hiệu hóa khách hàng thành công",
                    customerRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy khách hàng"));
    }
}
