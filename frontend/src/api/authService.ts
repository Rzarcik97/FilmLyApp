import type { LoginPayload, LoginResponse, RegistrationPayload, RegistrationResponse, UserProfile } from '../types/auth'
import apiClient from './apiClient'

export const authService = {
  register: async (payload: RegistrationPayload): Promise<RegistrationResponse> => {
    const response = await apiClient.post<RegistrationResponse>(
      '/auth/registration',
      payload
    );

    return response.data;
  },

  login: async (payload: LoginPayload): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>('auth/login', payload);
    return response.data;
  },

  getMyProfile: async (): Promise<UserProfile> => {
    const token = localStorage.getItem('token');
    const response = await apiClient.get<UserProfile>('/users/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    return response.data;
  }
}