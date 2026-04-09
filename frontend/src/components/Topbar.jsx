import React, { useState } from 'react';
import { useAuthStore } from '../store/useAuthStore';
import { LogOut, User as UserIcon } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const Topbar = () => {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="h-16 border-b border-slate-800 bg-slate-900 flex items-center justify-between px-6">
      <div className="text-xl font-medium text-slate-300">
        Bienvenue, <span className="text-blue-400 capitalize">{user?.nom || user?.prenom || 'Utilisateur'}</span>
      </div>
      
      <div className="relative">
        <button 
          onClick={() => setMenuOpen(!menuOpen)}
          className="flex items-center gap-3 focus:outline-none"
        >
          <div className="flex flex-col text-right hidden sm:flex">
            <span className="text-sm font-medium text-slate-200">{user?.nom} {user?.prenom}</span>
            <span className="text-xs text-blue-500 font-bold">{user?.role}</span>
          </div>
          <div className="w-10 h-10 rounded-full bg-slate-800 flex items-center justify-center border border-slate-700 text-slate-300">
            <UserIcon size={20} />
          </div>
        </button>

        {menuOpen && (
          <div className="absolute right-0 mt-2 w-48 bg-slate-800 rounded-xl shadow-lg border border-slate-700 py-1 z-50">
            <button
              onClick={() => { setMenuOpen(false); navigate('/profile'); }}
              className="flex w-full items-center gap-2 px-4 py-2 text-sm text-slate-300 hover:bg-slate-700 transition-colors"
            >
              <UserIcon size={16} /> Profil
            </button>
            <div className="h-px bg-slate-700 my-1"></div>
            <button
              onClick={handleLogout}
              className="flex w-full items-center gap-2 px-4 py-2 text-sm text-red-400 hover:bg-slate-700 transition-colors"
            >
              <LogOut size={16} /> Déconnexion
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Topbar;
