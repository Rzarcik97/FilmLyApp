import type { LoginPayload, LoginResponse, RegistrationPayload, RegistrationResponse } from '../types/auth'
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
  }
}