import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';

const RoleRoute = ({ roles }) => {
  const { checkRole } = useAuthStore();

  if (!checkRole(roles)) {
    // If not authorized, redirect to dashboard by default
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
};

export default RoleRoute;
