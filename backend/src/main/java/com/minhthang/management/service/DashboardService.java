package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.*;
import com.minhthang.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ApprovalRequestRepository approvalRequestRepository;
    private final CustomerRepository customerRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final ProductionOrderRepository productionOrderRepository;
    private final WorkOrderRepository workOrderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final StockTransactionRepository stockTransactionRepository;

    public ApiResponse<Map<String, Object>> getStats() {
        Map<String, Object> s = new LinkedHashMap<>();

        s.put("totalProducts", productRepository.count());
        s.put("totalSuppliers", supplierRepository.count());
        s.put("totalCustomers", customerRepository.count());
        s.put("pendingApprovals", approvalRequestRepository.countByStatus(ApprovalStatus.PENDING));

        List<SalesOrder> allOrders = salesOrderRepository.findAll();
        s.put("totalSalesOrders", allOrders.size());
        s.put("pendingSalesOrders", allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count());
        s.put("deliveredOrders", allOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count());

        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        s.put("totalRevenue", totalRevenue);

        s.put("totalProductionOrders", productionOrderRepository.count());
        s.put("inProgressProduction", productionOrderRepository.countByStatus(ProductionStatus.IN_PROGRESS));
        s.put("completedProduction", productionOrderRepository.countByStatus(ProductionStatus.COMPLETED));
        s.put("totalWorkOrders", workOrderRepository.count());

        List<RawMaterial> materials = rawMaterialRepository.findByActiveTrue();
        long lowStock = materials.stream()
                .filter(m -> m.getQuantity() != null && m.getMinQuantity() != null && m.getQuantity().compareTo(m.getMinQuantity()) <= 0)
                .count();
        s.put("lowStockItems", lowStock);

        return ApiResponse.success("OK", s);
    }

    public ApiResponse<Map<String, Object>> getMonitoring() {
        Map<String, Object> s = new LinkedHashMap<>();

        List<SalesOrder> allOrders = salesOrderRepository.findAll();
        List<RawMaterial> materials = rawMaterialRepository.findByActiveTrue();

        s.put("totalSalesOrders", allOrders.size());
        s.put("pendingApprovals", approvalRequestRepository.countByStatus(ApprovalStatus.PENDING));
        s.put("inProgressProduction", productionOrderRepository.countByStatus(ProductionStatus.IN_PROGRESS));

        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        s.put("totalRevenue", totalRevenue);

        List<RawMaterial> lowStockList = materials.stream()
                .filter(m -> m.getQuantity() != null && m.getMinQuantity() != null && m.getQuantity().compareTo(m.getMinQuantity()) <= 0)
                .toList();
        s.put("lowStockItems", lowStockList.size());
        s.put("lowStockMaterials", lowStockList);

        Map<String, Long> ordersByStatus = allOrders.stream()
                .collect(Collectors.groupingBy(o -> o.getStatus().name(), Collectors.counting()));
        s.put("ordersByStatus", ordersByStatus);

        List<ProductionOrder> allProd = productionOrderRepository.findAll();
        Map<String, Long> prodByStatus = allProd.stream()
                .collect(Collectors.groupingBy(o -> o.getStatus().name(), Collectors.counting()));
        s.put("productionByStatus", prodByStatus);

        List<SalesOrder> recentOrders = allOrders.stream()
                .sorted(Comparator.comparing(SalesOrder::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10).toList();
        s.put("recentOrders", recentOrders);

        return ApiResponse.success("OK", s);
    }

    public ApiResponse<Map<String, Object>> getSalesStaffDashboard(String email) {
        Map<String, Object> s = new LinkedHashMap<>();
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ApiResponse.error("User not found");

        var user = userOpt.get();
        s.put("totalCustomers", customerRepository.findBySalesStaffId(user.getId()).size());

        List<SalesOrder> myOrders = salesOrderRepository.findBySalesStaffId(user.getId());
        s.put("totalOrders", myOrders.size());
        s.put("pendingOrders", myOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count());

        BigDecimal myRevenue = myOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        s.put("myRevenue", myRevenue);

        s.put("recentOrders", myOrders.stream()
                .sorted(Comparator.comparing(SalesOrder::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5).toList());

        return ApiResponse.success("OK", s);
    }

    public ApiResponse<Map<String, Object>> getSalesManagerDashboard() {
        Map<String, Object> s = new LinkedHashMap<>();
        List<SalesOrder> allOrders = salesOrderRepository.findAll();

        s.put("totalOrders", allOrders.size());
        s.put("deliveredOrders", allOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count());
        s.put("unpaidOrders", allOrders.stream().filter(o -> o.getPaymentStatus() == PaymentStatus.UNPAID).count());
        s.put("totalCustomers", customerRepository.count());

        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        s.put("totalRevenue", totalRevenue);

        List<User> salesStaff = userRepository.findByRole(Role.SALES_STAFF);
        List<Map<String, Object>> perf = new ArrayList<>();
        for (User staff : salesStaff) {
            Map<String, Object> p = new LinkedHashMap<>();
            p.put("fullName", staff.getFullName());
            p.put("email", staff.getEmail());
            List<SalesOrder> staffOrders = allOrders.stream()
                    .filter(o -> o.getSalesStaff() != null && staff.getId().equals(o.getSalesStaff().getId()))
                    .toList();
            p.put("totalOrders", staffOrders.size());
            p.put("revenue", staffOrders.stream()
                    .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                    .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            perf.add(p);
        }
        s.put("staffPerformance", perf);

        return ApiResponse.success("OK", s);
    }

    public ApiResponse<Map<String, Object>> getProductionDashboard() {
        Map<String, Object> s = new LinkedHashMap<>();
        List<ProductionOrder> all = productionOrderRepository.findAll();

        s.put("totalOrders", all.size());
        s.put("inProgress", all.stream().filter(o -> o.getStatus() == ProductionStatus.IN_PROGRESS).count());
        s.put("completed", all.stream().filter(o -> o.getStatus() == ProductionStatus.COMPLETED).count());

        BigDecimal totalQty = BigDecimal.ZERO, totalCompleted = BigDecimal.ZERO, totalScrap = BigDecimal.ZERO;
        for (var po : all) {
            if (po.getQuantity() != null) totalQty = totalQty.add(po.getQuantity());
            if (po.getCompletedQuantity() != null) totalCompleted = totalCompleted.add(po.getCompletedQuantity());
            if (po.getScrapQuantity() != null) totalScrap = totalScrap.add(po.getScrapQuantity());
        }
        s.put("completionRate", totalQty.compareTo(BigDecimal.ZERO) > 0
                ? totalCompleted.multiply(BigDecimal.valueOf(100)).divide(totalQty, 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        s.put("scrapRate", totalQty.compareTo(BigDecimal.ZERO) > 0
                ? totalScrap.multiply(BigDecimal.valueOf(100)).divide(totalQty, 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        s.put("inProgressOrders", all.stream().filter(o -> o.getStatus() == ProductionStatus.IN_PROGRESS).toList());
        s.put("pendingWorkOrders", workOrderRepository.findByStatus(WorkOrderStatus.PENDING).size());

        return ApiResponse.success("OK", s);
    }

    public ApiResponse<Map<String, Object>> getWarehouseDashboard() {
        Map<String, Object> s = new LinkedHashMap<>();
        List<RawMaterial> materials = rawMaterialRepository.findByActiveTrue();

        s.put("totalMaterials", materials.size());

        List<RawMaterial> lowStock = materials.stream()
                .filter(m -> m.getQuantity() != null && m.getMinQuantity() != null && m.getQuantity().compareTo(m.getMinQuantity()) <= 0)
                .toList();
        s.put("lowStockCount", lowStock.size());
        s.put("lowStockMaterials", lowStock);

        BigDecimal totalValue = materials.stream()
                .filter(m -> m.getQuantity() != null && m.getPrice() != null)
                .map(m -> m.getQuantity().multiply(m.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        s.put("totalStockValue", totalValue);

        s.put("recentTransactions", stockTransactionRepository.findAll().stream()
                .sorted(Comparator.comparing(StockTransaction::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10).toList());

        return ApiResponse.success("OK", s);
    }
}
