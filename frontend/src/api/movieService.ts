import type { Actor, Genre, Movie } from '../types';
import apiClient from './apiClient';

export const getMovieDetails = async (id: string | number): Promise<Movie> => {
  const response = await apiClient.get<Movie>(`/movies/${id}`);
  return response.data;
}

export const getTrendingMovies = async (): Promise<Movie[]> => {
  const response = await apiClient.get<Movie[]>('/movies/trending');
  return response.data;
}

export const getTrendingSeries = async (): Promise<Movie[]> => {
  const response = await apiClient.get<Movie[]>('/series/trending');
  return response.data;
}

export const getRecentMovies = async (): Promise<Movie[]> => {
  const response = await apiClient.get<Movie[]>('/movies/recent');
  return response.data;
}

export const getPopularMovies = async (): Promise<Movie[]> => {
  const response = await apiClient.get<Movie[]>('/movies/popular');
  return response.data;
}

export const getGenres = async (): Promise<Genre[]> => {
  const response = await apiClient.get<Genre[]>('/genres');
  return response.data;
}

export const getMovieCast = async (id: string): Promise<Actor[]> => {
  const response = await apiClient.get<Actor[]>(`/movies/${id}/cast`);
  return response.data;
}

export const getSimilarContent = async (id: string, type: string): Promise<Movie[]> => {
  const endpoint = type.toLowerCase() === 'movie' ? 'movies' : 'series';
  const response = await apiClient.get<Movie[]>(`/${endpoint}/${id}/similar`);
  return response.data;
}