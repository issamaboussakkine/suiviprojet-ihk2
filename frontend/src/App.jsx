import React, { Suspense, lazy } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import PrivateRoute from './guards/PrivateRoute';
import RoleRoute from './guards/RoleRoute';

const Login = lazy(() => import('./pages/Login'));
const Dashboard = lazy(() => import('./pages/Dashboard'));
const Organismes = lazy(() => import('./pages/Organismes'));
const Employes = lazy(() => import('./pages/Employes'));
const Projets = lazy(() => import('./pages/Projets'));
const Phases = lazy(() => import('./pages/Phases'));
const Affectations = lazy(() => import('./pages/Affectations'));
const Livrables = lazy(() => import('./pages/Livrables'));
const Documents = lazy(() => import('./pages/Documents'));
const Factures = lazy(() => import('./pages/Factures'));

function App() {
  return (
    <BrowserRouter>
      <Suspense fallback={<div className="min-h-screen bg-slate-900 flex items-center justify-center"><div className="w-8 h-8 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div></div>}>
        <Routes>
          <Route path="/login" element={<Login />} />
          
          {/* Routes privées avec Sidebar / Topbar */}
          <Route element={<PrivateRoute />}>
            <Route element={<MainLayout />}>
              <Route path="/" element={<Navigate to="/dashboard" replace />} />
              <Route path="/dashboard" element={<Dashboard />} />
              
              <Route element={<RoleRoute roles={['ADMIN', 'SECRETAIRE']} />}>
                 <Route path="/organismes" element={<Organismes />} />
              </Route>
              
              <Route element={<RoleRoute roles={['ADMIN']} />}>
                 <Route path="/employes" element={<Employes />} />
              </Route>
              
              <Route element={<RoleRoute roles={['ADMIN', 'SECRETAIRE', 'CHEF_PROJET', 'DIRECTEUR']} />}>
                 <Route path="/projets" element={<Projets />} />
              </Route>
              
              <Route element={<RoleRoute roles={['ADMIN', 'CHEF_PROJET', 'COLLABORATEUR']} />}>
                 <Route path="/phases" element={<Phases />} />
                 <Route path="/affectations" element={<Affectations />} />
                 <Route path="/documents" element={<Documents />} />
              </Route>

              <Route element={<RoleRoute roles={['ADMIN', 'CHEF_PROJET', 'COLLABORATEUR']} />}>
                 <Route path="/livrables" element={<Livrables />} />
              </Route>

              <Route element={<RoleRoute roles={['ADMIN', 'COMPTABLE']} />}>
                 <Route path="/factures" element={<Factures />} />
              </Route>

              <Route element={<RoleRoute roles={['ADMIN', 'DIRECTEUR']} />}>
                 <Route path="/reporting" element={<Dashboard />} />
              </Route>
            </Route>
          </Route>
          
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
}

export default App;