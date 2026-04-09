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
        // Roles can be a single string or an array of strings
        if (Array.isArray(roles)) {
          return roles.includes(state.user.role);
        }
        return state.user.role === roles;
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
