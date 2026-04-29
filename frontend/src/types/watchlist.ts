import type { Movie } from '.';

export interface WatchlistRequest {
  contentId: number;
  contentType: string;
}

export interface WatchlistResponse {
  id: number;
  contentId: number;
  contentType: string;
  title: string;
  posterPath: string;
  watchedAt: string;
  addedAt: string;
  releaseDate?: string;
  voteAverage?: number;
  voteCount?: number;
}

export interface WatchlistState {
  items: number[];
  watchedItems: number[];
  fullList: Movie[];
  loading: boolean;
  error: string | null;
}