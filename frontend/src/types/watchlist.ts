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
}

export interface WatchlistState {
  items: number[];
  watchedItems: number[];
  loading: boolean;
  error: string | null;
}