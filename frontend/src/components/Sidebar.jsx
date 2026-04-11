import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  Building2, Users, Briefcase, Layers, Link as LinkIcon, 
  CheckSquare, FileText, CreditCard, PieChart, LayoutDashboard
} from 'lucide-react';
import { useAuthStore } from '../store/useAuthStore';

const Sidebar = () => {
  const { user } = useAuthStore();
  
  // Extraction sûre du rôle (garantit que ce soit une string)
  const roleName = typeof user?.role === 'object' ? (user?.role?.code || user?.role?.libelle || '') : (user?.role || '');
  const normalizedRole = roleName.toUpperCase();

  const NAV_ITEMS = [
    { name: 'Dashboard', path: '/dashboard', icon: LayoutDashboard, roles: ['ADMIN', 'SECRETAIRE', 'DIRECTEUR', 'CHEF_PROJET', 'COMPTABLE', 'COLLABORATEUR'] },
    { name: 'Organismes', path: '/organismes', icon: Building2, roles: ['ADMIN', 'SECRETAIRE'] },
    { name: 'Employés', path: '/employes', icon: Users, roles: ['ADMIN'] },
    { name: 'Projets', path: '/projets', icon: Briefcase, roles: ['ADMIN', 'SECRETAIRE', 'DIRECTEUR', 'CHEF_PROJET'] },
    { name: 'Phases', path: '/phases', icon: Layers, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Affectations', path: '/affectations', icon: LinkIcon, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Livrables', path: '/livrables', icon: CheckSquare, roles: ['ADMIN', 'CHEF_PROJET', 'COLLABORATEUR'] },
    { name: 'Documents', path: '/documents', icon: FileText, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Factures', path: '/factures', icon: CreditCard, roles: ['ADMIN', 'COMPTABLE'] },
    { name: 'Reporting', path: '/reporting', icon: PieChart, roles: ['ADMIN', 'DIRECTEUR', 'COMPTABLE'] },
  ];

  // Si on n'a pas de rôle défini (ex: non connecté), on ne montre rien
  const filteredNav = normalizedRole ? NAV_ITEMS.filter(item => item.roles.includes(normalizedRole) || item.roles.includes('ALL')) : [];

  return (
    <div className="h-full w-full bg-theme-card border-r border-theme-border flex flex-col transition-colors duration-200">
      <div className="flex items-center justify-center px-4 border-b border-theme-border h-16 shrink-0 transition-colors duration-200">
        <span className="text-xl font-bold text-theme-text uppercase tracking-widest transition-colors duration-200">
          IHK<span className="text-theme-accent">App</span>
        </span>
      </div>

      <div className="flex-1 overflow-y-auto py-4 px-3">
        <ul className="list-none p-0 m-0 flex flex-col gap-2">
          {filteredNav.map((item) => {
            const Icon = item.icon;
            return (
              <li key={item.path}>
                <NavLink
                  to={item.path}
                  className={({ isActive }) => `
                    flex items-center gap-3 p-3 rounded-xl no-underline transition-all duration-200
                    ${isActive ? 'bg-theme-accent text-white font-bold' : 'bg-transparent text-theme-text font-normal hover:bg-theme-bg'}
                  `}
                >
                  <Icon size={20} />
                  <span>{item.name}</span>
                </NavLink>
              </li>
            );
          })}
        </ul>
      </div>
    </div>
  );
};

export default Sidebar;
