import type { WatchlistRequest, WatchlistResponse } from '../types/watchlist';
import apiClient from './apiClient';

export const userService = {
  addToWatchlist: async (payload: { contentId: number; contentType: string }): Promise<WatchlistResponse> => {
    const response = await apiClient.post<WatchlistResponse>('/users/watchlist', {
      contentId: payload.contentId,
      contentType: payload.contentType.toUpperCase(),
    });
    return response.data;
  },

  removeFromWatchlist: async (payload: WatchlistRequest): Promise<void> => {
    return await apiClient.delete(`/users/watchlist/${payload.contentId}`, {
      data: {
        contentId: payload.contentId,
        contentType: payload.contentType.toUpperCase()
      }
    });
  },

  // removeFromWatchlist: async (payload: { contentId: number; contentType: string }) => {
  //   const token = localStorage.getItem('token');

  //   return await apiClient.delete(`/users/watchlist/${payload.contentId}`, {
  //     headers: { Authorization: `Bearer ${token}` },
  //     data: {
  //       contentId: payload.contentId,
  //       contentType: payload.contentType.toUpperCase()
  //     },
  //     params: {
  //       type: payload.contentType.toUpperCase()
  //     }
  //   });
  // },

  getWatchList: async (type: 'MOVIE' | 'SERIES'): Promise<WatchlistResponse[]> => {
    const response = await apiClient.get<WatchlistResponse[]>(`/users/watchlist?type=${type.toUpperCase()}`);
    return response.data;
  },

  markAsWatched: async (payload: WatchlistRequest): Promise<WatchlistResponse> => {
    const response = await apiClient.patch<WatchlistResponse>(
      '/users/watchlist/watched',
      {
        ...payload,
        contentId: payload.contentId,
        contentType: payload.contentType.toUpperCase()
      }
    );
    return response.data;
  },
}