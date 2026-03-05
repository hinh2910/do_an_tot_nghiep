import { useState, useEffect } from 'react'
import DashboardLayout from '../../components/DashboardLayout'
import { PageHeader, SearchBar, Table, Td, Badge, ActionLink, ActionBtn, Icons, Loading, EmptyState, fmtCurrency } from '../../components/ui'
import { productApi } from '../../services/api'

export default function ProductList() {
  const [products, setProducts] = useState([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)

  const fetchProducts = async (q = '') => {
    try {
      setLoading(true)
      const res = await productApi.getAll(q)
      setProducts(res.data.data || [])
    } catch {
      setProducts([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchProducts() }, [])

  const handleSearch = (val) => {
    setSearch(val)
    fetchProducts(val)
  }

  const handleToggle = async (id) => {
    try {
      await productApi.toggleActive(id)
      fetchProducts(search)
    } catch { /* ignore */ }
  }

  return (
    <DashboardLayout title="Sản phẩm">
      <PageHeader title="Sản phẩm" desc="Quản lý danh sách sản phẩm" actionTo="/products/new" actionLabel="Thêm sản phẩm">
        <SearchBar value={search} onChange={handleSearch} placeholder="Tìm kiếm sản phẩm..." />
      </PageHeader>

      {loading ? <Loading /> : products.length === 0 ? <EmptyState message="Không có sản phẩm nào" /> : (
        <Table headers={['SKU', 'Tên', 'Danh mục', 'Giá', 'Trạng thái', 'Thao tác']}>
          {products.map((p) => (
            <tr key={p.id} className="hover:bg-gray-50/50">
              <Td className="font-mono text-gray-600">{p.sku}</Td>
              <Td className="font-medium text-gray-900">{p.name}</Td>
              <Td>{p.categoryName || p.category?.name || '—'}</Td>
              <Td>{fmtCurrency(p.price)}</Td>
              <Td>
                <Badge variant={p.active ? 'green' : 'gray'}>
                  {p.active ? 'Hoạt động' : 'Ngưng'}
                </Badge>
              </Td>
              <Td>
                <div className="flex gap-1">
                  <ActionLink to={`/products/${p.id}`} icon={Icons.eye} title="Xem" />
                  <ActionLink to={`/products/${p.id}/edit`} icon={Icons.edit} title="Sửa" />
                  <ActionBtn onClick={() => handleToggle(p.id)} icon={Icons.toggle} title={p.active ? 'Ngưng' : 'Kích hoạt'} />
                </div>
              </Td>
            </tr>
          ))}
        </Table>
      )}
    </DashboardLayout>
  )
}
