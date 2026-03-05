package com.minhthang.management.repository;

import com.minhthang.management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByActiveTrue();

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Customer> search(String q);

    List<Customer> findBySalesStaffId(String salesStaffId);

    long countByActiveTrue();
}
