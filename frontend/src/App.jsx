import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import ForgotPassword from './pages/ForgotPassword'
import ResetPassword from './pages/ResetPassword'
import Dashboard from './pages/Dashboard'
import ProductList from './pages/products/ProductList'
import ProductForm from './pages/products/ProductForm'
import ProductDetail from './pages/products/ProductDetail'
import SupplierList from './pages/suppliers/SupplierList'
import SupplierForm from './pages/suppliers/SupplierForm'
import SupplierDetail from './pages/suppliers/SupplierDetail'
import WarehouseList from './pages/warehouses/WarehouseList'
import WarehouseForm from './pages/warehouses/WarehouseForm'
import WarehouseDetail from './pages/warehouses/WarehouseDetail'
import RawMaterialList from './pages/materials/RawMaterialList'
import RawMaterialForm from './pages/materials/RawMaterialForm'
import RawMaterialDetail from './pages/materials/RawMaterialDetail'
import BomList from './pages/bom/BomList'
import BomForm from './pages/bom/BomForm'
import BomDetail from './pages/bom/BomDetail'
import RoleList from './pages/roles/RoleList'
import RoleDetail from './pages/roles/RoleDetail'
import ApprovalList from './pages/approvals/ApprovalList'
import ApprovalDetail from './pages/approvals/ApprovalDetail'
import FinancialOverview from './pages/reports/FinancialOverview'
import RevenueReport from './pages/reports/RevenueReport'
import ReceivableReport from './pages/reports/ReceivableReport'
import CustomerList from './pages/customers/CustomerList'
import CustomerForm from './pages/customers/CustomerForm'
import CustomerDetail from './pages/customers/CustomerDetail'
import QuotationList from './pages/quotations/QuotationList'
import QuotationForm from './pages/quotations/QuotationForm'
import QuotationDetail from './pages/quotations/QuotationDetail'
import SalesOrderList from './pages/sales-orders/SalesOrderList'
import SalesOrderForm from './pages/sales-orders/SalesOrderForm'
import SalesOrderDetail from './pages/sales-orders/SalesOrderDetail'
import ProductionOrderList from './pages/production/ProductionOrderList'
import ProductionOrderForm from './pages/production/ProductionOrderForm'
import ProductionOrderDetail from './pages/production/ProductionOrderDetail'
import WorkOrderList from './pages/work-orders/WorkOrderList'
import WorkOrderForm from './pages/work-orders/WorkOrderForm'
import WorkOrderDetail from './pages/work-orders/WorkOrderDetail'
import InventoryList from './pages/inventory/InventoryList'
import InventoryDetail from './pages/inventory/InventoryDetail'
import StockIn from './pages/stock/StockIn'
import StockOut from './pages/stock/StockOut'
import StockCountList from './pages/stock-counts/StockCountList'
import StockCountForm from './pages/stock-counts/StockCountForm'
import StockCountDetail from './pages/stock-counts/StockCountDetail'
import UserProfile from './pages/profile/UserProfile'
import UserList from './pages/users/UserList'
import UserForm from './pages/users/UserForm'
import UserDetail from './pages/users/UserDetail'
import MonitoringDashboard from './pages/dashboards/MonitoringDashboard'
import SalesStaffDashboard from './pages/dashboards/SalesStaffDashboard'
import SalesManagerDashboard from './pages/dashboards/SalesManagerDashboard'
import ProductionDashboard from './pages/dashboards/ProductionDashboard'
import WarehouseDashboard from './pages/dashboards/WarehouseDashboard'
import OperationalDashboard from './pages/dashboards/OperationalDashboard'
import DeliveryTracking from './pages/sales-orders/DeliveryTracking'
import PaymentReminders from './pages/sales-orders/PaymentReminders'
import QuotationHistory from './pages/quotations/QuotationHistory'
import ProfitReport from './pages/reports/ProfitReport'
import CustomerCredit from './pages/reports/CustomerCredit'
import TransactionHistory from './pages/reports/TransactionHistory'
import OverdueInvoices from './pages/reports/OverdueInvoices'
import InventoryOverview from './pages/reports/InventoryOverview'
import ProductionPerformance from './pages/reports/ProductionPerformance'
import StockAdjustments from './pages/stock/StockAdjustments'

function P({ children }) {
  const token = localStorage.getItem('token') || sessionStorage.getItem('token')
  return token ? children : <Navigate to="/login" replace />
}

function Pub({ children }) {
  const token = localStorage.getItem('token') || sessionStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || sessionStorage.getItem('user') || 'null')
  return (token && user) ? <Navigate to="/dashboard" replace /> : children
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Pub><Login /></Pub>} />
        <Route path="/forgot-password" element={<Pub><ForgotPassword /></Pub>} />
        <Route path="/reset-password" element={<ResetPassword />} />

        <Route path="/dashboard" element={<P><Dashboard /></P>} />

        <Route path="/products" element={<P><ProductList /></P>} />
        <Route path="/products/new" element={<P><ProductForm /></P>} />
        <Route path="/products/:id" element={<P><ProductDetail /></P>} />
        <Route path="/products/:id/edit" element={<P><ProductForm /></P>} />

        <Route path="/suppliers" element={<P><SupplierList /></P>} />
        <Route path="/suppliers/new" element={<P><SupplierForm /></P>} />
        <Route path="/suppliers/:id" element={<P><SupplierDetail /></P>} />
        <Route path="/suppliers/:id/edit" element={<P><SupplierForm /></P>} />

        <Route path="/warehouses" element={<P><WarehouseList /></P>} />
        <Route path="/warehouses/new" element={<P><WarehouseForm /></P>} />
        <Route path="/warehouses/:id" element={<P><WarehouseDetail /></P>} />
        <Route path="/warehouses/:id/edit" element={<P><WarehouseForm /></P>} />

        <Route path="/materials" element={<P><RawMaterialList /></P>} />
        <Route path="/materials/new" element={<P><RawMaterialForm /></P>} />
        <Route path="/materials/:id" element={<P><RawMaterialDetail /></P>} />
        <Route path="/materials/:id/edit" element={<P><RawMaterialForm /></P>} />

        <Route path="/boms" element={<P><BomList /></P>} />
        <Route path="/boms/new" element={<P><BomForm /></P>} />
        <Route path="/boms/:id" element={<P><BomDetail /></P>} />
        <Route path="/boms/:id/edit" element={<P><BomForm /></P>} />

        <Route path="/roles" element={<P><RoleList /></P>} />
        <Route path="/roles/:role" element={<P><RoleDetail /></P>} />

        <Route path="/approvals" element={<P><ApprovalList /></P>} />
        <Route path="/approvals/:id" element={<P><ApprovalDetail /></P>} />

        <Route path="/customers" element={<P><CustomerList /></P>} />
        <Route path="/customers/new" element={<P><CustomerForm /></P>} />
        <Route path="/customers/:id" element={<P><CustomerDetail /></P>} />
        <Route path="/customers/:id/edit" element={<P><CustomerForm /></P>} />

        <Route path="/quotations" element={<P><QuotationList /></P>} />
        <Route path="/quotations/new" element={<P><QuotationForm /></P>} />
        <Route path="/quotations/:id" element={<P><QuotationDetail /></P>} />
        <Route path="/quotations/:id/edit" element={<P><QuotationForm /></P>} />

        <Route path="/sales-orders" element={<P><SalesOrderList /></P>} />
        <Route path="/sales-orders/new" element={<P><SalesOrderForm /></P>} />
        <Route path="/sales-orders/:id" element={<P><SalesOrderDetail /></P>} />

        <Route path="/production-orders" element={<P><ProductionOrderList /></P>} />
        <Route path="/production-orders/new" element={<P><ProductionOrderForm /></P>} />
        <Route path="/production-orders/:id" element={<P><ProductionOrderDetail /></P>} />

        <Route path="/work-orders" element={<P><WorkOrderList /></P>} />
        <Route path="/work-orders/new" element={<P><WorkOrderForm /></P>} />
        <Route path="/work-orders/:id" element={<P><WorkOrderDetail /></P>} />
        <Route path="/work-orders/:id/edit" element={<P><WorkOrderForm /></P>} />

        <Route path="/inventory" element={<P><InventoryList /></P>} />
        <Route path="/inventory/:id" element={<P><InventoryDetail /></P>} />

        <Route path="/stock/in" element={<P><StockIn /></P>} />
        <Route path="/stock/out" element={<P><StockOut /></P>} />
        <Route path="/stock/adjustments" element={<P><StockAdjustments /></P>} />

        <Route path="/stock-counts" element={<P><StockCountList /></P>} />
        <Route path="/stock-counts/new" element={<P><StockCountForm /></P>} />
        <Route path="/stock-counts/:id" element={<P><StockCountDetail /></P>} />

        <Route path="/profile" element={<P><UserProfile /></P>} />
        <Route path="/users" element={<P><UserList /></P>} />
        <Route path="/users/new" element={<P><UserForm /></P>} />
        <Route path="/users/:id" element={<P><UserDetail /></P>} />
        <Route path="/users/:id/edit" element={<P><UserForm /></P>} />

        <Route path="/reports/financial" element={<P><FinancialOverview /></P>} />
        <Route path="/reports/revenue" element={<P><RevenueReport /></P>} />
        <Route path="/reports/receivable" element={<P><ReceivableReport /></P>} />
        <Route path="/reports/profit" element={<P><ProfitReport /></P>} />
        <Route path="/reports/credit" element={<P><CustomerCredit /></P>} />
        <Route path="/reports/transactions" element={<P><TransactionHistory /></P>} />
        <Route path="/reports/overdue" element={<P><OverdueInvoices /></P>} />
        <Route path="/reports/inventory" element={<P><InventoryOverview /></P>} />
        <Route path="/reports/production-performance" element={<P><ProductionPerformance /></P>} />

        <Route path="/monitoring" element={<P><MonitoringDashboard /></P>} />
        <Route path="/dashboard/sales-staff" element={<P><SalesStaffDashboard /></P>} />
        <Route path="/dashboard/sales-manager" element={<P><SalesManagerDashboard /></P>} />
        <Route path="/dashboard/production" element={<P><ProductionDashboard /></P>} />
        <Route path="/dashboard/warehouse" element={<P><WarehouseDashboard /></P>} />
        <Route path="/dashboard/operational" element={<P><OperationalDashboard /></P>} />

        <Route path="/delivery-tracking" element={<P><DeliveryTracking /></P>} />
        <Route path="/payment-reminders" element={<P><PaymentReminders /></P>} />
        <Route path="/quotations/history/:customerId" element={<P><QuotationHistory /></P>} />

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
