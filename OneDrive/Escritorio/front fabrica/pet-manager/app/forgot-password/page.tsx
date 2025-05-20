"use client"

import type React from "react"

import { useState } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"

export default function ForgotPassword() {
  const router = useRouter()
  const [email, setEmail] = useState("")
  const [submitted, setSubmitted] = useState(false)

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Aquí iría la lógica para enviar el correo de recuperación
    console.log("Solicitud de recuperación para:", email)
    setSubmitted(true)
  }

  return (
    <main className="min-h-screen bg-sky-200 flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl p-8 w-full max-w-md">
        <h2 className="text-2xl font-bold text-blue-900 mb-6">Recuperar Contraseña</h2>

        {!submitted ? (
          <form onSubmit={handleSubmit}>
            <div className="mb-6">
              <label htmlFor="email" className="block text-blue-900 mb-2">
                Email
              </label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full p-3 bg-sky-100 rounded-md"
                required
              />
              <p className="text-sm text-gray-600 mt-2">
                Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.
              </p>
            </div>

            <div className="flex justify-between">
              <Link href="/" className="text-blue-900 hover:underline">
                Volver al inicio de sesión
              </Link>
              <button
                type="submit"
                className="bg-yellow-300 hover:bg-yellow-400 text-blue-900 font-medium py-2 px-6 rounded-md"
              >
                Enviar
              </button>
            </div>
          </form>
        ) : (
          <div>
            <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
              Se ha enviado un correo electrónico a <strong>{email}</strong> con instrucciones para restablecer tu
              contraseña.
            </div>
            <div className="text-center">
              <Link href="/" className="text-blue-900 hover:underline">
                Volver al inicio de sesión
              </Link>
            </div>
          </div>
        )}
      </div>
    </main>
  )
}
