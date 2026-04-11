import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useAuthStore = create(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,

      login: (userData, tokenData) =>
        set({
          user: userData,
          token: tokenData,
          isAuthenticated: true,
        }),

      logout: () =>
        set({
          user: null,
          token: null,
          isAuthenticated: false,
        }),

      checkRole: (roles) => {
        const state = useAuthStore.getState();
        if (!state.user || !state.user.role) return false;
        
        // Sécuriser au cas où un vieil objet resterait dans le localStorage
        const userRole = typeof state.user.role === 'object' 
            ? (state.user.role.code || state.user.role.libelle || '') 
            : state.user.role;
            
        const cleanRole = userRole.toUpperCase();

        // Roles can be a single string or an array of strings
        if (Array.isArray(roles)) {
          return roles.includes(cleanRole);
        }
        return cleanRole === roles.toUpperCase();
      },
      
      updateUser: (userData) =>
        set((state) => ({
          user: { ...state.user, ...userData }
        }))
    }),
    {
      name: 'auth-storage', // name of the item in the storage (must be unique)
      partialize: (state) => ({ user: state.user, token: state.token, isAuthenticated: state.isAuthenticated }),
    }
  )
);
