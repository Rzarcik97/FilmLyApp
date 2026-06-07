import type { Movie } from '.';

export interface WatchlistRequest {
  contentId: number;
  contentType: string;
  watchedAt?: string;
}

export interface WatchlistResponse {
  id: number;
  contentId: number;
  contentType?: 'MOVIE' | 'SERIES';
  type?: 'MOVIE' | 'SERIES';
  title: string;

  posterPath?: string;
  poster_path?: string;

  watchedAt: string | null;
  addedAt?: string;

  releaseDate?: string;
  release_date?: string;

  voteAverage?: number;
  vote_average?: number;

  voteCount?: number;
  vote_count?: number;

  overview?: string;
}

export interface WatchlistState {
  movies: Movie[];
  series: Movie[];
  loading: boolean;
  error: string | null;
  watchedItems: number[];
}