"use client"

import { useState } from "react"
import Link from "next/link"
import { PawPrint } from "lucide-react"

// Tipo para los usuarios
type User = {
  id: string
  name: string
  email: string
  role: string
  permissions: string
}

export default function Users() {
  // Datos de ejemplo para la tabla
  const [users, setUsers] = useState<User[]>([
    { id: "1", name: "Name", email: "Email", role: "Role", permissions: "Permissions" },
    { id: "2", name: "Name", email: "Email", role: "Role", permissions: "Permissions" },
    { id: "3", name: "Name", email: "Email", role: "Role", permissions: "Permissions" },
    { id: "4", name: "Name", email: "Email", role: "Role", permissions: "Permissions" },
    { id: "5", name: "Name", email: "Email", role: "Role", permissions: "Permissions" },
    { id: "6", name: "Name", email: "Email", role: "Role", permissions: "Permissions" },
    { id: "7", name: "Name", email: "Email", role: "Role", permissions: "Permissions" },
  ])

  const handleDelete = (id: string) => {
    // Aquí iría la lógica para eliminar un usuario
    setUsers(users.filter((user) => user.id !== id))
  }

  return (
    <main className="min-h-screen bg-sky-200 p-8">
      <div className="max-w-6xl mx-auto">
        <div className="flex justify-between items-center mb-6">
          <div className="flex items-center">
            <PawPrint className="text-blue-900 mr-2" size={28} />
            <h1 className="text-2xl font-bold text-blue-900">USUARIOS DEL SISTEMA</h1>
          </div>
          <Link
            href="/users/new"
            className="bg-yellow-300 hover:bg-yellow-400 text-blue-900 font-medium py-2 px-4 rounded-md"
          >
            New user
          </Link>
        </div>

        <div className="bg-white bg-opacity-20 rounded-3xl p-4 border border-blue-900">
          <table className="w-full">
            <thead>
              <tr className="border-b border-blue-900">
                <th className="py-3 text-left text-blue-900 pl-4">Name</th>
                <th className="py-3 text-left text-blue-900">Email</th>
                <th className="py-3 text-left text-blue-900">Role</th>
                <th className="py-3 text-left text-blue-900">Permissions</th>
                <th className="py-3 text-center text-blue-900">Editar</th>
                <th className="py-3 text-center text-blue-900">Eliminar</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id} className="border-b border-blue-200">
                  <td className="py-3 pl-4">{user.name}</td>
                  <td className="py-3">{user.email}</td>
                  <td className="py-3">{user.role}</td>
                  <td className="py-3">{user.permissions}</td>
                  <td className="py-3 text-center">
                    <button className="text-blue-900 hover:underline">Editar</button>
                  </td>
                  <td className="py-3 text-center">
                    <button className="text-red-500 hover:underline" onClick={() => handleDelete(user.id)}>
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </main>
  )
}
