package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Quotation;
import com.minhthang.management.entity.QuotationStatus;
import com.minhthang.management.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository quotationRepository;

    public ApiResponse<List<Quotation>> getAll() {
        return ApiResponse.success("Lấy danh sách báo giá thành công", quotationRepository.findAll());
    }

    public ApiResponse<List<Quotation>> getByStatus(QuotationStatus status) {
        return ApiResponse.success("Lấy danh sách báo giá thành công", quotationRepository.findByStatus(status));
    }

    public ApiResponse<List<Quotation>> getByCustomerId(String customerId) {
        return ApiResponse.success("Lấy danh sách báo giá thành công", quotationRepository.findByCustomerId(customerId));
    }

    public ApiResponse<Quotation> getById(String id) {
        return quotationRepository.findById(id)
                .map(q -> ApiResponse.success("Lấy thông tin báo giá thành công", q))
                .orElse(ApiResponse.error("Không tìm thấy báo giá"));
    }

    @Transactional
    public ApiResponse<Quotation> create(Quotation quotation) {
        quotation.setQuotationNumber("QT-" + System.currentTimeMillis());

        if (quotation.getItems() != null) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (var item : quotation.getItems()) {
                item.setQuotation(quotation);
                if (item.getQuantity() != null && item.getUnitPrice() != null) {
                    item.setTotalPrice(item.getQuantity().multiply(item.getUnitPrice()));
                    totalAmount = totalAmount.add(item.getTotalPrice());
                }
            }
            quotation.setTotalAmount(totalAmount);
        }

        Quotation saved = quotationRepository.save(quotation);
        return ApiResponse.success("Tạo báo giá thành công", saved);
    }

    @Transactional
    public ApiResponse<Quotation> update(String id, Quotation quotation) {
        return quotationRepository.findById(id).map(existing -> {
            existing.setCustomer(quotation.getCustomer());
            existing.setSalesStaff(quotation.getSalesStaff());
            existing.setValidUntil(quotation.getValidUntil());
            existing.setNotes(quotation.getNotes());

            existing.getItems().clear();
            if (quotation.getItems() != null) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (var item : quotation.getItems()) {
                    item.setQuotation(existing);
                    if (item.getQuantity() != null && item.getUnitPrice() != null) {
                        item.setTotalPrice(item.getQuantity().multiply(item.getUnitPrice()));
                        totalAmount = totalAmount.add(item.getTotalPrice());
                    }
                    existing.getItems().add(item);
                }
                existing.setTotalAmount(totalAmount);
            }

            return ApiResponse.success("Cập nhật báo giá thành công", quotationRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy báo giá"));
    }

    @Transactional
    public ApiResponse<Quotation> changeStatus(String id, QuotationStatus status) {
        return quotationRepository.findById(id).map(existing -> {
            existing.setStatus(status);
            return ApiResponse.success("Cập nhật trạng thái báo giá thành công", quotationRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy báo giá"));
    }
}
