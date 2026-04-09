import api from '../utils/axios.config';
import { createCrudService } from './crudService';

// Authentication Service
export const authService = {
  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials);
    return response.data;
  },
  getMe: async () => {
    const response = await api.get('/auth/me');
    return response.data;
  },
  changePassword: async (data) => {
    const response = await api.post('/auth/change-password', data);
    return response.data;
  }
};

// CRUD Services
export const organismeService = createCrudService('/organismes');
export const employeService = createCrudService('/employees');
export const projetService = createCrudService('/projets');
export const phaseService = createCrudService('/phases');
export const affectationService = createCrudService('/affectations');
export const livrableService = createCrudService('/livrables');

// Documents might need custom FormData upload
export const documentService = {
  ...createCrudService('/documents'),
  upload: async (file, metadata) => {
    const formData = new FormData();
    formData.append('file', file);
    if(metadata) {
       formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));
    }
    const response = await api.post('/documents/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  },
  download: async (id) => {
    const response = await api.get(`/documents/download/${id}`, { responseType: 'blob' });
    return response;
  }
};

export const factureService = createCrudService('/factures');

export const dashboardService = {
  getReporting: async () => {
    const response = await api.get('/reporting');
    return response.data;
  }
};
