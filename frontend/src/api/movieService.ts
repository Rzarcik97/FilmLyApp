import type { Actor, Genre, Movie } from '../types';
import apiClient from './apiClient';

export const getMovieDetails = async (id: string | number, type: string): Promise<Movie> => {
  const endpoint = type.toLowerCase() === 'movies' ? 'movies' : 'series';

  const response = await apiClient.get<Movie>(`/${endpoint}/${id}`);
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

export const getMovieCast = async (id: string, type: string): Promise<Actor[]> => {
  const endpoint = type.toLowerCase() === 'movies' ? 'movies' : 'series';

  const response = await apiClient.get<Actor[]>(`/${endpoint}/${id}/cast`);
  return response.data;
}

export const getSimilarContent = async (id: string, type: string): Promise<Movie[]> => {
  const endpoint = type.toLowerCase() === 'movie' ? 'movies' : 'series';
  const response = await apiClient.get<Movie[]>(`/${endpoint}/${id}/similar`);
  return response.data;
}

export const getPopularActors = async (): Promise<Actor[]> => {
  const response = await apiClient.get<Actor[]>('/actors/popular');
  return response.data;
}