import React, { useState, useEffect } from 'react';
import { useAuthStore } from '../store/useAuthStore';
import { LogOut, User as UserIcon, Moon, Sun } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const Topbar = () => {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  
  const [darkMode, setDarkMode] = useState(() => {
    return localStorage.getItem('theme') === 'dark';
  });

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add('dark');
      localStorage.setItem('theme', 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('theme', 'light');
    }
  }, [darkMode]);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="h-16 border-b border-theme-border bg-theme-bg flex items-center justify-between px-6 shrink-0 transition-colors duration-200">
      <div className="text-lg font-medium text-theme-text">
        Bienvenue, <span className="text-theme-accent capitalize">{user?.nom || user?.prenom || 'Utilisateur'}</span>
      </div>
      
      <div className="flex items-center gap-4 relative">
        <button
          onClick={() => setDarkMode(!darkMode)}
          className="p-2 rounded-full bg-theme-card border border-theme-border text-theme-text hover:bg-theme-bg transition-colors"
          title={darkMode ? "Passer en mode clair" : "Passer en mode sombre"}
        >
          {darkMode ? <Sun size={20} /> : <Moon size={20} />}
        </button>

        <button 
          onClick={() => setMenuOpen(!menuOpen)}
          className="flex items-center gap-3 bg-transparent border-none cursor-pointer p-0"
        >
          <div className="flex flex-col text-right">
            <span className="text-sm font-medium text-theme-text">{user?.nom} {user?.prenom}</span>
            <span className="text-xs text-theme-textSec font-bold">
              {typeof user?.role === 'object' ? (user.role.code || user.role.libelle) : user?.role}
            </span>
          </div>
          <div className="w-10 h-10 rounded-full bg-theme-card flex items-center justify-center border border-theme-border text-theme-text transition-colors">
            <UserIcon size={20} />
          </div>
        </button>

        {menuOpen && (
          <div className="absolute right-0 top-12 w-48 bg-theme-card rounded-lg shadow-lg border border-theme-border py-2 z-50 transition-colors">
            <button
              onClick={() => { setMenuOpen(false); navigate('/profile'); }}
              className="flex w-full items-center gap-2 px-4 py-2 bg-transparent border-none cursor-pointer text-theme-text text-sm text-left hover:bg-theme-bg transition-colors"
            >
              <UserIcon size={16} /> Profil
            </button>
            <div className="h-px bg-theme-border my-1"></div>
            <button
              onClick={handleLogout}
              className="flex w-full items-center gap-2 px-4 py-2 bg-transparent border-none cursor-pointer text-red-500 text-sm text-left hover:bg-theme-bg transition-colors"
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
