"use client"

import Image from "next/image"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { useState } from "react"

export default function Home() {
  const router = useRouter()
  const [formData, setFormData] = useState({
    username: "",
    password: ""
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Aquí iría la lógica de autenticación
    console.log("Login attempt:", formData)
    
    // Redirigir al dashboard
    router.push("/dashboard")
  }

  return (
    <main className="flex min-h-screen">
      {/* Lado izquierdo - Logo */}
      <div className="w-1/2 bg-sky-200 flex flex-col items-center justify-center">
        <div className="w-72 h-72 mb-8">
          <Image 
            src="/logo.png" 
            alt="Pet Manager Logo" 
            width={400} 
            height={400}
            priority
            className="w-full h-full object-contain"
          />
        </div>
        <h1 className="text-4xl font-bold text-blue-900">PET MANAGER</h1>
      </div>

      {/* Lado derecho - Formulario de login */}
      <div className="w-1/2 flex items-center justify-center">
        <div className="w-full max-w-md px-8">
          <h2 className="text-2xl font-bold text-blue-900 mb-8">LOG IN TO YOUR ACCOUNT</h2>

          <form onSubmit={handleSubmit}>
            <div className="mb-4">
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

            <div className="mb-6">
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

            <div className="flex justify-center mb-6">
              <button
                type="submit"
                className="bg-yellow-300 hover:bg-yellow-400 text-blue-900 font-medium py-2 px-8 rounded-md"
              >
                Log in
              </button>
            </div>

            <div className="flex justify-between text-sky-400">
              <Link href="/forgot-password" className="hover:underline">
                Forgot the password?
              </Link>
              <Link href="/register" className="hover:underline">
                Create new account
              </Link>
            </div>
          </form>
        </div>
      </div>
    </main>
  )
}
