"use client"

import type React from "react"

import { useState } from "react"
import { PawPrint, Plus, Search, Filter, Calendar, Download } from "lucide-react"

// Tipo para las ventas
type Sale = {
  id: string
  date: string
  customer: string
  total: number
  status: "Completed" | "Pending" | "Cancelled"
}

export default function Sales() {
  // Datos de ejemplo para la tabla
  const [sales, setSales] = useState<Sale[]>([
    { id: "1", date: "2023-05-15", customer: "Juan Pérez", total: 120.5, status: "Completed" },
    { id: "2", date: "2023-05-14", customer: "María García", total: 85.75, status: "Completed" },
    { id: "3", date: "2023-05-13", customer: "Carlos López", total: 200.0, status: "Pending" },
    { id: "4", date: "2023-05-12", customer: "Ana Martínez", total: 150.25, status: "Completed" },
    { id: "5", date: "2023-05-11", customer: "Pedro Sánchez", total: 95.0, status: "Cancelled" },
    { id: "6", date: "2023-05-10", customer: "Laura Rodríguez", total: 180.3, status: "Completed" },
    { id: "7", date: "2023-05-09", customer: "Miguel Fernández", total: 220.45, status: "Pending" },
  ])

  const [searchTerm, setSearchTerm] = useState("")
  const [showNewSaleForm, setShowNewSaleForm] = useState(false)
  const [newSale, setNewSale] = useState({
    username: "",
    name: "",
    email: "",
    id: "",
    address: "",
    phone: "",
    role: "",
  })

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setNewSale((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("Nueva venta:", newSale)
    setShowNewSaleForm(false)
    // Aquí iría la lógica para guardar la nueva venta
  }

  const filteredSales = sales.filter(
    (sale) => sale.customer.toLowerCase().includes(searchTerm.toLowerCase()) || sale.id.includes(searchTerm),
  )

  const getStatusColor = (status: Sale["status"]) => {
    switch (status) {
      case "Completed":
        return "bg-green-100 text-green-800"
      case "Pending":
        return "bg-yellow-100 text-yellow-800"
      case "Cancelled":
        return "bg-red-100 text-red-800"
      default:
        return "bg-gray-100 text-gray-800"
    }
  }

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center">
          <PawPrint className="text-blue-900 mr-2" size={28} />
          <h1 className="text-2xl font-bold text-blue-900">LISTA DE VENTAS</h1>
        </div>
        <div className="flex space-x-2">
          <button
            onClick={() => setShowNewSaleForm(true)}
            className="bg-yellow-300 hover:bg-yellow-400 text-blue-900 font-medium py-2 px-4 rounded-md flex items-center"
          >
            <Plus size={18} className="mr-1" />
            Nueva venta
          </button>
          <button className="bg-blue-900 text-white font-medium py-2 px-4 rounded-md flex items-center">
            <Download size={18} className="mr-1" />
            Exportar
          </button>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md mb-6">
        <div className="p-4 border-b border-gray-200 flex justify-between items-center">
          <h2 className="text-lg font-semibold text-blue-900">Registro de Ventas</h2>
          <div className="flex items-center space-x-2">
            <div className="relative">
              <input
                type="text"
                placeholder="Buscar venta..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-9 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-900/50"
              />
              <Search size={16} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
            </div>
            <button className="p-2 border border-gray-300 rounded-md hover:bg-gray-100">
              <Calendar size={16} />
            </button>
            <button className="p-2 border border-gray-300 rounded-md hover:bg-gray-100">
              <Filter size={16} />
            </button>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="bg-sky-50">
                <th className="py-3 px-4 text-left text-blue-900 font-semibold">ID</th>
                <th className="py-3 px-4 text-left text-blue-900 font-semibold">Fecha</th>
                <th className="py-3 px-4 text-left text-blue-900 font-semibold">Cliente</th>
                <th className="py-3 px-4 text-left text-blue-900 font-semibold">Total</th>
                <th className="py-3 px-4 text-left text-blue-900 font-semibold">Estado</th>
                <th className="py-3 px-4 text-center text-blue-900 font-semibold">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filteredSales.map((sale) => (
                <tr key={sale.id} className="border-b border-gray-100 hover:bg-sky-50">
                  <td className="py-3 px-4">#{sale.id}</td>
                  <td className="py-3 px-4">{sale.date}</td>
                  <td className="py-3 px-4">{sale.customer}</td>
                  <td className="py-3 px-4">${sale.total.toFixed(2)}</td>
                  <td className="py-3 px-4">
                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(sale.status)}`}>
                      {sale.status}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center">
                    <button className="text-blue-900 hover:underline font-medium mr-2">Ver</button>
                    <button className="text-blue-900 hover:underline font-medium mr-2">Editar</button>
                    <button className="text-red-500 hover:underline font-medium">Eliminar</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="p-4 border-t border-gray-200 flex justify-between items-center">
          <div className="text-sm text-gray-500">
            Mostrando {filteredSales.length} de {sales.length} ventas
          </div>
          <div className="flex space-x-1">
            <button className="px-3 py-1 border border-gray-300 rounded-md hover:bg-gray-100">Anterior</button>
            <button className="px-3 py-1 bg-blue-900 text-white rounded-md">1</button>
            <button className="px-3 py-1 border border-gray-300 rounded-md hover:bg-gray-100">2</button>
            <button className="px-3 py-1 border border-gray-300 rounded-md hover:bg-gray-100">3</button>
            <button className="px-3 py-1 border border-gray-300 rounded-md hover:bg-gray-100">Siguiente</button>
          </div>
        </div>
      </div>

      {/* Modal para nueva venta */}
      {showNewSaleForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg w-full max-w-2xl">
            <div className="p-6">
              <h2 className="text-xl font-bold text-blue-900 mb-4">Nueva Venta</h2>
              <form onSubmit={handleSubmit}>
                <div className="grid grid-cols-2 gap-4 mb-4">
                  <div>
                    <label htmlFor="username" className="block text-blue-900 mb-1">
                      Username
                    </label>
                    <input
                      type="text"
                      id="username"
                      name="username"
                      value={newSale.username}
                      onChange={handleInputChange}
                      className="w-full p-2 bg-sky-100 rounded-md"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="name" className="block text-blue-900 mb-1">
                      Name
                    </label>
                    <input
                      type="text"
                      id="name"
                      name="name"
                      value={newSale.name}
                      onChange={handleInputChange}
                      className="w-full p-2 bg-sky-100 rounded-md"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="email" className="block text-blue-900 mb-1">
                      Email
                    </label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={newSale.email}
                      onChange={handleInputChange}
                      className="w-full p-2 bg-sky-100 rounded-md"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="id" className="block text-blue-900 mb-1">
                      ID
                    </label>
                    <input
                      type="text"
                      id="id"
                      name="id"
                      value={newSale.id}
                      onChange={handleInputChange}
                      className="w-full p-2 bg-sky-100 rounded-md"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="address" className="block text-blue-900 mb-1">
                      Address
                    </label>
                    <input
                      type="text"
                      id="address"
                      name="address"
                      value={newSale.address}
                      onChange={handleInputChange}
                      className="w-full p-2 bg-sky-100 rounded-md"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="phone" className="block text-blue-900 mb-1">
                      Phone
                    </label>
                    <input
                      type="tel"
                      id="phone"
                      name="phone"
                      value={newSale.phone}
                      onChange={handleInputChange}
                      className="w-full p-2 bg-sky-100 rounded-md"
                      required
                    />
                  </div>
                  <div>
                    <label htmlFor="role" className="block text-blue-900 mb-1">
                      Role
                    </label>
                    <select
                      id="role"
                      name="role"
                      value={newSale.role}
                      onChange={handleInputChange}
                      className="w-full p-2 bg-sky-100 rounded-md"
                      required
                    >
                      <option value="">Seleccionar rol</option>
                      <option value="admin">Administrador</option>
                      <option value="user">Usuario</option>
                      <option value="guest">Invitado</option>
                    </select>
                  </div>
                </div>
                <div className="flex justify-end space-x-2 mt-6">
                  <button
                    type="button"
                    onClick={() => setShowNewSaleForm(false)}
                    className="bg-gray-200 hover:bg-gray-300 text-blue-900 font-medium py-2 px-6 rounded-md"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="bg-yellow-300 hover:bg-yellow-400 text-blue-900 font-medium py-2 px-6 rounded-md"
                  >
                    Save
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
