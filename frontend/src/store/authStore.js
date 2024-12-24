import { create } from 'zustand'

const useAuthStore = create((set) => ({
  user: null,
  role: null, // 'driver', 'manager', 'admin'
  isAuthenticated: false,
  
  login: (userData) => set({
    user: userData,
    role: userData.role,
    isAuthenticated: true,
  }),
  
  logout: () => set({
    user: null,
    role: null,
    isAuthenticated: false,
  }),
}))

export default useAuthStore