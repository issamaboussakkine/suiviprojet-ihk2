import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { 
  Building2, 
  Users, 
  Briefcase, 
  Layers, 
  Link as LinkIcon, 
  CheckSquare, 
  FileText, 
  CreditCard, 
  PieChart, 
  LayoutDashboard,
  ChevronLeft,
  ChevronRight
} from 'lucide-react';
import { useAuthStore } from '../store/useAuthStore';
import clsx from 'clsx';

const Sidebar = () => {
  const [collapsed, setCollapsed] = useState(false);
  const { user } = useAuthStore();

  const role = user?.role;

  const NAV_ITEMS = [
    { name: 'Dashboard', path: '/dashboard', icon: LayoutDashboard, roles: ['ADMIN', 'SECRETAIRE', 'DIRECTEUR', 'CHEF_PROJET', 'COMPTABLE'] },
    { name: 'Organismes', path: '/organismes', icon: Building2, roles: ['ADMIN', 'SECRETAIRE'] },
    { name: 'Employés', path: '/employes', icon: Users, roles: ['ADMIN'] },
    { name: 'Projets', path: '/projets', icon: Briefcase, roles: ['ADMIN', 'SECRETAIRE', 'CHEF_PROJET'] },
    { name: 'Phases', path: '/phases', icon: Layers, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Affectations', path: '/affectations', icon: LinkIcon, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Livrables', path: '/livrables', icon: CheckSquare, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Documents', path: '/documents', icon: FileText, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Factures', path: '/factures', icon: CreditCard, roles: ['ADMIN', 'COMPTABLE'] },
    { name: 'Reporting', path: '/reporting', icon: PieChart, roles: ['ADMIN', 'DIRECTEUR'] },
  ];

  const filteredNav = NAV_ITEMS.filter(item => item.roles.includes(role) || item.roles.includes('ALL'));

  return (
    <div className={clsx(
      "h-full bg-slate-900 border-r border-slate-800 transition-all duration-300 relative flex flex-col",
      collapsed ? "w-20" : "w-64"
    )}>
      <div className="flex items-center justify-between p-4 border-b border-slate-800 h-16">
        {!collapsed && (
          <span className="text-xl font-bold text-slate-50 uppercase tracking-wider">
            IHK<span className="text-blue-500">App</span>
          </span>
        )}
        <button 
          onClick={() => setCollapsed(!collapsed)}
          className="mx-auto text-slate-400 hover:text-slate-50 transition-colors bg-slate-800 rounded p-1"
        >
          {collapsed ? <ChevronRight size={20} /> : <ChevronLeft size={20} />}
        </button>
      </div>

      <div className="flex-1 overflow-y-auto py-4 scrollbar-hide">
        <ul className="space-y-2 px-3">
          {filteredNav.map((item) => {
            const Icon = item.icon;
            return (
              <li key={item.path}>
                <NavLink
                  to={item.path}
                  className={({ isActive }) => clsx(
                    "flex items-center gap-3 px-3 py-3 rounded-xl transition-all duration-200",
                    isActive 
                      ? "bg-blue-600/10 text-blue-500 font-medium" 
                      : "text-slate-400 hover:bg-slate-800/50 hover:text-slate-100"
                  )}
                  title={collapsed ? item.name : undefined}
                >
                  <Icon size={20} className={collapsed ? "mx-auto" : ""} />
                  {!collapsed && <span>{item.name}</span>}
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
