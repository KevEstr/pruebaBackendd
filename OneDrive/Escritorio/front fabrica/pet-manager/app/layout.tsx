import type React from "react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "../styles/globals.css"
import { SidebarProvider } from "@/components/sidebar-provider"
import { Header } from "@/components/header"
import { Sidebar } from "@/components/sidebar"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "Pet Manager",
  description: "Sistema de gestión para mascotas",
  generator: 'v0.dev'
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="es" className="h-full">
      <body className={`${inter.className} min-h-full bg-gray-50 antialiased`}>
        <SidebarProvider>
          <div className="min-h-screen">
            <Header />
            <div className="flex">
              <Sidebar />
              <main className="flex-1 p-4 lg:p-6">
                {children}
              </main>
            </div>
          </div>
        </SidebarProvider>
      </body>
    </html>
  )
}
