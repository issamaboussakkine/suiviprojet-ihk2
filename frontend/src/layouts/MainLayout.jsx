import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import Topbar from '../components/Topbar';

const MainLayout = () => {
  return (
    <div className="flex h-screen bg-theme-bg text-theme-text font-interface overflow-hidden transition-colors duration-200">
      <div className="w-[260px] shrink-0">
        <Sidebar />
      </div>
      <div className="flex-1 flex flex-col h-screen overflow-hidden">
        <Topbar />
        <main className="flex-1 overflow-x-hidden overflow-y-auto p-6 bg-theme-bg relative">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default MainLayout;
