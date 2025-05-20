"use client"

import type React from "react"

import Image from "next/image"
import { useRouter } from "next/navigation"
import { useState } from "react"

export default function Register() {
  const router = useRouter()
  const [formData, setFormData] = useState({
    username: "",
    name: "",
    password: "",
    confirmPassword: "",
    address: "",
    phone: "",
    email: "",
    id: "",
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Aquí iría la lógica para enviar los datos al backend
    console.log("Datos de registro:", formData)

    // Redirigir al usuario a la página de inicio de sesión
    router.push("/")
  }

  return (
    <main className="min-h-screen bg-sky-200 flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl p-8 w-full max-w-4xl relative">
        <div className="absolute top-6 right-8 flex flex-col items-center">
          <div className="w-24 h-24">
            <Image 
              src="/logo.png" 
              alt="Pet Manager Logo" 
              width={100} 
              height={100}
              priority
              className="w-full h-full object-contain"
            />
          </div>
        </div>

        <h2 className="text-2xl font-bold text-blue-900 mb-8">CREATE A NEW ACCOUNT</h2>

        <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-4">
          <div>
            <label htmlFor="username" className="block text-blue-900 mb-2">
              Username
            </label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div>
            <label htmlFor="name" className="block text-blue-900 mb-2">
              Name
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

          <div>
            <label htmlFor="password" className="block text-blue-900 mb-2">
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div>
            <label htmlFor="confirmPassword" className="block text-blue-900 mb-2">
              Confirm Password
            </label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div>
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

          <div>
            <label htmlFor="phone" className="block text-blue-900 mb-2">
              Phone
            </label>
            <input
              type="tel"
              id="phone"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div>
            <label htmlFor="id" className="block text-blue-900 mb-2">
              ID
            </label>
            <input
              type="text"
              id="id"
              name="id"
              value={formData.id}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div>
            <label htmlFor="address" className="block text-blue-900 mb-2">
              Address
            </label>
            <input
              type="text"
              id="address"
              name="address"
              value={formData.address}
              onChange={handleChange}
              className="w-full p-3 bg-sky-100 rounded-md"
              required
            />
          </div>

          <div className="col-span-2 flex justify-center mt-4">
            <button
              type="submit"
              className="bg-yellow-300 hover:bg-yellow-400 text-blue-900 font-medium py-2 px-8 rounded-md"
            >
              Register
            </button>
          </div>
        </form>
      </div>
    </main>
  )
}
