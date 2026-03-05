package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.ApprovalRequest;
import com.minhthang.management.entity.ApprovalStatus;
import com.minhthang.management.repository.ApprovalRequestRepository;
import com.minhthang.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalRequestRepository approvalRequestRepository;
    private final UserRepository userRepository;

    public ApiResponse<List<ApprovalRequest>> getAll() {
        return ApiResponse.success("Lấy danh sách yêu cầu phê duyệt thành công",
                approvalRequestRepository.findAll());
    }

    public ApiResponse<List<ApprovalRequest>> getByStatus(ApprovalStatus status) {
        return ApiResponse.success("Lấy yêu cầu phê duyệt theo trạng thái thành công",
                approvalRequestRepository.findByStatus(status));
    }

    public ApiResponse<ApprovalRequest> getById(String id) {
        return approvalRequestRepository.findById(id)
                .map(r -> ApiResponse.success("Lấy yêu cầu phê duyệt thành công", r))
                .orElse(ApiResponse.error("Không tìm thấy yêu cầu phê duyệt"));
    }

    @Transactional
    public ApiResponse<ApprovalRequest> create(ApprovalRequest request) {
        ApprovalRequest saved = approvalRequestRepository.save(request);
        return ApiResponse.success("Tạo yêu cầu phê duyệt thành công", saved);
    }

    @Transactional
    public ApiResponse<ApprovalRequest> approve(String id, String userId, String notes) {
        return approvalRequestRepository.findById(id)
                .map(request -> {
                    if (request.getStatus() != ApprovalStatus.PENDING) {
                        return ApiResponse.<ApprovalRequest>error("Yêu cầu đã được xử lý");
                    }
                    return userRepository.findById(userId)
                            .map(user -> {
                                request.setStatus(ApprovalStatus.APPROVED);
                                request.setApprovedBy(user);
                                request.setNotes(notes);
                                return ApiResponse.success("Phê duyệt thành công",
                                        approvalRequestRepository.save(request));
                            })
                            .orElse(ApiResponse.error("Không tìm thấy người dùng"));
                })
                .orElse(ApiResponse.error("Không tìm thấy yêu cầu phê duyệt"));
    }

    @Transactional
    public ApiResponse<ApprovalRequest> reject(String id, String userId, String notes) {
        return approvalRequestRepository.findById(id)
                .map(request -> {
                    if (request.getStatus() != ApprovalStatus.PENDING) {
                        return ApiResponse.<ApprovalRequest>error("Yêu cầu đã được xử lý");
                    }
                    return userRepository.findById(userId)
                            .map(user -> {
                                request.setStatus(ApprovalStatus.REJECTED);
                                request.setApprovedBy(user);
                                request.setNotes(notes);
                                return ApiResponse.success("Từ chối yêu cầu thành công",
                                        approvalRequestRepository.save(request));
                            })
                            .orElse(ApiResponse.error("Không tìm thấy người dùng"));
                })
                .orElse(ApiResponse.error("Không tìm thấy yêu cầu phê duyệt"));
    }
}
