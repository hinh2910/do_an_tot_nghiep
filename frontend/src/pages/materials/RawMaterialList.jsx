import { useState, useEffect } from 'react'
import DashboardLayout from '../../components/DashboardLayout'
import { PageHeader, SearchBar, Table, Td, Badge, ActionLink, ActionBtn, Icons, Loading, EmptyState } from '../../components/ui'
import { materialApi } from '../../services/api'

export default function RawMaterialList() {
  const [materials, setMaterials] = useState([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)

  const fetchMaterials = async (q = '') => {
    try {
      setLoading(true)
      const res = await materialApi.getAll(q)
      setMaterials(res.data.data || res.data || [])
    } catch {
      setMaterials([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchMaterials() }, [])

  const handleSearch = (val) => {
    setSearch(val)
    fetchMaterials(val)
  }

  return (
    <DashboardLayout title="Nguyên vật liệu">
      <PageHeader title="Nguyên vật liệu" desc="Quản lý danh sách nguyên vật liệu" actionTo="/materials/new" actionLabel="Thêm mới">
        <SearchBar value={search} onChange={handleSearch} placeholder="Tìm kiếm nguyên vật liệu..." />
      </PageHeader>

      {loading ? <Loading /> : materials.length === 0 ? <EmptyState message="Không có nguyên vật liệu nào" /> : (
        <Table headers={['Mã', 'Tên', 'Đơn vị', 'Trạng thái', 'Thao tác']}>
          {materials.map((m) => (
            <tr key={m.id} className="hover:bg-gray-50/50">
              <Td className="font-mono text-gray-600">{m.code || m.sku || '—'}</Td>
              <Td className="font-medium text-gray-900">{m.name}</Td>
              <Td>{m.unit || '—'}</Td>
              <Td>
                <Badge variant={m.active !== false ? 'green' : 'gray'}>
                  {m.active !== false ? 'Hoạt động' : 'Ngưng'}
                </Badge>
              </Td>
              <Td>
                <div className="flex gap-1">
                  <ActionLink to={`/materials/${m.id}`} icon={Icons.eye} title="Xem" />
                  <ActionLink to={`/materials/${m.id}/edit`} icon={Icons.edit} title="Sửa" />
                </div>
              </Td>
            </tr>
          ))}
        </Table>
      )}
    </DashboardLayout>
  )
}
