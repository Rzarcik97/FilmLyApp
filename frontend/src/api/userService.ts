import type { WatchlistRequest, WatchlistResponse } from '../types/watchlist';
import apiClient from './apiClient';

export const userService = {
  addToWatchlist: async (payload: WatchlistRequest): Promise<WatchlistResponse> => {
    const response = await apiClient.post<WatchlistResponse>('/users/watchlist', payload);
    return response.data;
  },

  removeFromWatchlist: async (payload: WatchlistRequest): Promise<void> => {
    await apiClient.delete(`/users/watchlist/${payload.contentId}`, {
      data: payload
    });
  },

  getWatchList: async (showWatched: boolean = true): Promise<WatchlistResponse[]> => {
    const response = await apiClient.get<WatchlistResponse[]>('/users/watchlist', {
      params: { showWatched }
    });
    return response.data;
  },

  markAsWatched: async (payload: WatchlistRequest): Promise<WatchlistRequest> => {
    const response = await apiClient.patch<WatchlistResponse>(
      '/users/watchlist/watched',
      payload
    );
    return response.data;
  }
}