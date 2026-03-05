package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.*;
import com.minhthang.management.repository.PaymentRepository;
import com.minhthang.management.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final PaymentRepository paymentRepository;

    public ApiResponse<List<SalesOrder>> getAll() {
        return ApiResponse.success("Lấy danh sách đơn hàng thành công", salesOrderRepository.findAll());
    }

    public ApiResponse<List<SalesOrder>> getByStatus(OrderStatus status) {
        return ApiResponse.success("Lấy danh sách đơn hàng thành công", salesOrderRepository.findByStatus(status));
    }

    public ApiResponse<List<SalesOrder>> getByCustomerId(String customerId) {
        return ApiResponse.success("Lấy danh sách đơn hàng thành công", salesOrderRepository.findByCustomerId(customerId));
    }

    public ApiResponse<SalesOrder> getById(String id) {
        return salesOrderRepository.findById(id)
                .map(o -> ApiResponse.success("Lấy thông tin đơn hàng thành công", o))
                .orElse(ApiResponse.error("Không tìm thấy đơn hàng"));
    }

    @Transactional
    public ApiResponse<SalesOrder> create(SalesOrder salesOrder) {
        salesOrder.setOrderNumber("SO-" + System.currentTimeMillis());

        if (salesOrder.getItems() != null) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (var item : salesOrder.getItems()) {
                item.setSalesOrder(salesOrder);
                if (item.getQuantity() != null && item.getUnitPrice() != null) {
                    item.setTotalPrice(item.getQuantity().multiply(item.getUnitPrice()));
                    totalAmount = totalAmount.add(item.getTotalPrice());
                }
            }
            salesOrder.setTotalAmount(totalAmount);
        }

        SalesOrder saved = salesOrderRepository.save(salesOrder);
        return ApiResponse.success("Tạo đơn hàng thành công", saved);
    }

    @Transactional
    public ApiResponse<SalesOrder> update(String id, SalesOrder salesOrder) {
        return salesOrderRepository.findById(id).map(existing -> {
            existing.setQuotation(salesOrder.getQuotation());
            existing.setCustomer(salesOrder.getCustomer());
            existing.setSalesStaff(salesOrder.getSalesStaff());
            existing.setDeliveryDate(salesOrder.getDeliveryDate());
            existing.setDeliveryAddress(salesOrder.getDeliveryAddress());
            existing.setNotes(salesOrder.getNotes());

            existing.getItems().clear();
            if (salesOrder.getItems() != null) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (var item : salesOrder.getItems()) {
                    item.setSalesOrder(existing);
                    if (item.getQuantity() != null && item.getUnitPrice() != null) {
                        item.setTotalPrice(item.getQuantity().multiply(item.getUnitPrice()));
                        totalAmount = totalAmount.add(item.getTotalPrice());
                    }
                    existing.getItems().add(item);
                }
                existing.setTotalAmount(totalAmount);
            }

            return ApiResponse.success("Cập nhật đơn hàng thành công", salesOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy đơn hàng"));
    }

    @Transactional
    public ApiResponse<SalesOrder> changeStatus(String id, OrderStatus status) {
        return salesOrderRepository.findById(id).map(existing -> {
            existing.setStatus(status);
            return ApiResponse.success("Cập nhật trạng thái đơn hàng thành công", salesOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy đơn hàng"));
    }

    public ApiResponse<List<Payment>> getPayments(String orderId) {
        return ApiResponse.success("Lấy danh sách thanh toán thành công", paymentRepository.findBySalesOrderId(orderId));
    }

    @Transactional
    public ApiResponse<Payment> addPayment(String orderId, Payment payment) {
        return salesOrderRepository.findById(orderId).map(order -> {
            payment.setSalesOrder(order);
            Payment saved = paymentRepository.save(payment);

            List<Payment> allPayments = paymentRepository.findBySalesOrderId(orderId);
            BigDecimal totalPaid = allPayments.stream()
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalPaid.compareTo(order.getTotalAmount()) >= 0) {
                order.setPaymentStatus(PaymentStatus.PAID);
            } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
                order.setPaymentStatus(PaymentStatus.PARTIAL);
            } else {
                order.setPaymentStatus(PaymentStatus.UNPAID);
            }
            salesOrderRepository.save(order);

            return ApiResponse.success("Thêm thanh toán thành công", saved);
        }).orElse(ApiResponse.error("Không tìm thấy đơn hàng"));
    }
}
