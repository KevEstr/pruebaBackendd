import Link from "next/link"
import { Users, ShoppingBag, ShoppingCart, Tag, BarChart3 } from "lucide-react"

export default function Dashboard() {
  const stats = [
    { title: "Total Usuarios", value: "124", icon: <Users size={24} />, color: "bg-blue-100 text-blue-600" },
    {
      title: "Ventas Mensuales",
      value: "$12,543",
      icon: <ShoppingBag size={24} />,
      color: "bg-green-100 text-green-600",
    },
    {
      title: "Compras Mensuales",
      value: "$8,234",
      icon: <ShoppingCart size={24} />,
      color: "bg-purple-100 text-purple-600",
    },
    { title: "Productos", value: "532", icon: <Tag size={24} />, color: "bg-orange-100 text-orange-600" },
  ]

  const recentActivity = [
    { id: 1, action: "Nueva venta", user: "Juan Pérez", time: "Hace 5 minutos" },
    { id: 2, action: "Nuevo usuario", user: "María García", time: "Hace 1 hora" },
    { id: 3, action: "Compra registrada", user: "Admin", time: "Hace 3 horas" },
    { id: 4, action: "Producto actualizado", user: "Carlos López", time: "Hace 5 horas" },
    { id: 5, action: "Venta cancelada", user: "Ana Martínez", time: "Hace 1 día" },
  ]

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-blue-900">Dashboard</h1>
        <div className="flex space-x-2">
          <Link 
            href="/sales"
            className="bg-blue-900 text-white font-medium py-2 px-4 rounded-md flex items-center"
          >
            <BarChart3 size={18} className="mr-1" />
            Ver reportes
          </Link>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {stats.map((stat, index) => (
          <div key={index} className="bg-white rounded-lg shadow-md p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold text-blue-900">{stat.title}</h2>
              <div className={`p-2 rounded-full ${stat.color}`}>{stat.icon}</div>
            </div>
            <p className="text-3xl font-bold text-blue-900">{stat.value}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow-md">
          <div className="p-4 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-blue-900">Actividad Reciente</h2>
          </div>
          <div className="p-4">
            <ul className="divide-y divide-gray-100">
              {recentActivity.map((activity) => (
                <li key={activity.id} className="py-3">
                  <div className="flex justify-between">
                    <div>
                      <p className="text-blue-900 font-medium">{activity.action}</p>
                      <p className="text-sm text-gray-500">Por: {activity.user}</p>
                    </div>
                    <p className="text-sm text-gray-500">{activity.time}</p>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md">
          <div className="p-4 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-blue-900">Accesos Rápidos</h2>
          </div>
          <div className="p-4 grid grid-cols-2 gap-4">
            <Link
              href="/sales"
              className="bg-sky-100 hover:bg-sky-200 p-4 rounded-lg flex flex-col items-center justify-center text-center"
            >
              <ShoppingBag size={24} className="text-blue-900 mb-2" />
              <span className="text-blue-900 font-medium">Nueva Venta</span>
            </Link>
            <Link
              href="/users"
              className="bg-sky-100 hover:bg-sky-200 p-4 rounded-lg flex flex-col items-center justify-center text-center"
            >
              <Users size={24} className="text-blue-900 mb-2" />
              <span className="text-blue-900 font-medium">Nuevo Usuario</span>
            </Link>
            <Link
              href="/sales"
              className="bg-sky-100 hover:bg-sky-200 p-4 rounded-lg flex flex-col items-center justify-center text-center"
            >
              <ShoppingCart size={24} className="text-blue-900 mb-2" />
              <span className="text-blue-900 font-medium">Nueva Compra</span>
            </Link>
            <Link
              href="/sales"
              className="bg-sky-100 hover:bg-sky-200 p-4 rounded-lg flex flex-col items-center justify-center text-center"
            >
              <BarChart3 size={24} className="text-blue-900 mb-2" />
              <span className="text-blue-900 font-medium">Ver Reportes</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  )
}
