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
  },

  changeEmail: async (newEmail: string, currentPassword: string) => {
    const response = await apiClient.patch('/users/me/change-email', null, {
      params: {
        newEmail,
        currentPassword
      }
    });
    return response.data;
  },

  changePassword: async (oldPassword: string, newPassword: string) => {
    const response = await apiClient.patch('/users/me/change-password', null, {
      params: {
        oldPassword,
        newPassword
      }
    });
    return response.data;
  },

  updateUserName: async (newUsername: string) => {
    const response = await apiClient.patch('/users/me', {
      username: newUsername
    });
    return response.data;
  }
};