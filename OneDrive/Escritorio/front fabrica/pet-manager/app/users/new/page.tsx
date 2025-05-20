"use client"

import type React from "react"

import { useState } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"

export default function NewUser() {
  const router = useRouter()
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    role: "",
    permissions: "",
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Aquí iría la lógica para enviar los datos al backend
    console.log("Datos del nuevo usuario:", formData)

    // Redirigir a la lista de usuarios
    router.push("/users")
  }

  return (
    <main className="min-h-screen bg-sky-200 p-8">
      <div className="max-w-2xl mx-auto bg-white rounded-3xl p-8">
        <h1 className="text-2xl font-bold text-blue-900 mb-6">Crear Nuevo Usuario</h1>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="name" className="block text-blue-900 mb-2">
              Nombre
            </label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div className="mb-4">
            <label htmlFor="email" className="block text-blue-900 mb-2">
              Email
            </label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div className="mb-4">
            <label htmlFor="role" className="block text-blue-900 mb-2">
              Rol
            </label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            >
              <option value="">Seleccionar rol</option>
              <option value="admin">Administrador</option>
              <option value="user">Usuario</option>
              <option value="guest">Invitado</option>
            </select>
          </div>

          <div className="mb-6">
            <label htmlFor="permissions" className="block text-blue-900 mb-2">
              Permisos
            </label>
            <input
              type="text"
              id="permissions"
              name="permissions"
              value={formData.permissions}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div className="flex justify-between">
            <Link
              href="/users"
              className="bg-gray-300 hover:bg-gray-400 text-blue-900 font-medium py-2 px-6 rounded-md"
            >
              Cancelar
            </Link>
            <button
              type="submit"
              className="bg-yellow-300 hover:bg-yellow-400 text-blue-900 font-medium py-2 px-6 rounded-md"
            >
              Guardar
            </button>
          </div>
        </form>
      </div>
    </main>
  )
}
