"use client"

import Link from "next/link"
import { Search, Menu } from "lucide-react"
import { useState } from "react"
import { useSidebar } from "./sidebar-provider"

export function Header() {
  const [searchQuery, setSearchQuery] = useState("")
  const { toggleSidebar } = useSidebar()

  return (
    <header className="bg-white border-b border-gray-200 py-3 px-4 sticky top-0 z-30">
      <div className="flex items-center justify-between gap-4">
        {/* Logo y botón menú */}
        <div className="flex items-center gap-2">
          <button 
            onClick={toggleSidebar}
            className="lg:hidden p-2 hover:bg-gray-100 rounded-md"
          >
            <Menu size={24} className="text-gray-500" />
          </button>
          <Link href="/dashboard" className="text-xl font-bold text-blue-900">
            PET MANAGER
          </Link>
        </div>

        {/* Búsqueda */}
        <div className="flex-1 max-w-xl">
          <div className="relative">
            <input
              type="text"
              placeholder="Buscar en PetManager..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full py-2 px-4 pr-10 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-900/50"
            />
            <button className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500">
              <Search size={18} />
            </button>
          </div>
        </div>

        {/* Perfil */}
        <div className="flex items-center gap-2">
          <button className="p-2 hover:bg-gray-100 rounded-full">
            <div className="w-8 h-8 bg-blue-900 rounded-full flex items-center justify-center text-white font-medium">
              A
            </div>
          </button>
        </div>
      </div>
    </header>
  )
}
