import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { userService } from '../api/userService';
import type { WatchlistRequest, WatchlistState } from '../types/watchlist';
import type { Movie } from '../types';

const mapToMovie = (item: any): Movie => ({
  ...item,
  id: item.contentId || item.id,
  title: item.title,
  poster_path: item.posterPath || item.poster_path || '',
  watchedAt: item.watchedAt || item.watched_at || null,
  release_date: item.releaseDate || item.release_date || '',
  vote_average: item.voteAverage || item.vote_average || 0,
  type: item.contentType || item.type || 'MOVIE'
});

export const fetchWatchList = createAsyncThunk(
  'watchlist/fetch',
  async (type: 'MOVIE' | 'SERIES') => {
    const data = await userService.getWatchList(type);
    return data;
  }
);

export const addToWatchlist = createAsyncThunk(
  'watchlist/add',
  async (payload: WatchlistRequest) => {
    return await userService.addToWatchlist(payload);
  }
);

export const removeFromWatchlist = createAsyncThunk(
  'watchlist/remove',
  async (payload: WatchlistRequest) => {
    await userService.removeFromWatchlist(payload);
    return { id: payload.contentId, type: payload.contentType.toUpperCase() };
  }
);

export const markAsWatched = createAsyncThunk(
  'watchlist/markAsWatched',
  async (payload: WatchlistRequest) => {
    return await userService.markAsWatched(payload);
  }
)

const initialState: WatchlistState = {
  movies: [],
  series: [],
  loading: false,
  error: null,
  watchedItems: []
};

const watchlistSlice = createSlice({
  name: 'watchlist',
  initialState,
  reducers: {
    clearWatchlist: (state) => {
      state.movies = [];
      state.series = [];
      state.error = null;
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchWatchList.pending, (state) => { state.loading = true })
      .addCase(fetchWatchList.fulfilled, (state, action) => {
        state.loading = false;
        const type = action.meta.arg;

        const mapped = action.payload.map((item: any) => {
          return {
            ...item,
            id: item.contentId || item.id || 0,

            watchedAt: item.watchedAt || item.watched_at || null,

            poster_path: item.posterPath || item.poster_path || '',
            release_date: item.releaseDate || item.release_date || '',
          };
        })

        if (type === 'MOVIE') {
          state.movies = mapped;
        } else {
          state.series = mapped;
        }

        state.watchedItems = action.payload
          .filter(item => item.watchedAt !== null)
          .map(item => item.contentId);
      })
      .addCase(addToWatchlist.fulfilled, (state, action) => {
        const newItem = mapToMovie(action.payload);
        if (newItem.type === 'MOVIE') state.movies.push(newItem);
        else state.series.push(newItem);
      })
      .addCase(removeFromWatchlist.fulfilled, (state, action) => {
        const { contentId, contentType } = action.meta.arg;

        if (contentType === 'MOVIE') {
          state.movies = state.movies.filter(m => m.contentId !== contentId);
        } else {
          state.series = state.series.filter(s => s.contentId !== contentId);
        }

        state.watchedItems = state.watchedItems.filter(id => id !== contentId);
      })
      .addCase(markAsWatched.fulfilled, (state, action) => {
        const updated = action.payload;
        const { contentId, contentType } = action.meta.arg;

        const list = contentType === 'MOVIE' ? state.movies : state.series;

        const item = list.find(m => m.contentId === contentId);

        if (item) {
          item.watchedAt = updated.watchedAt || new Date().toISOString();
        }

        if (!state.watchedItems.includes(contentId)) {
          state.watchedItems.push(contentId);
        }
      });
  },
});

export default watchlistSlice.reducer;
export const { clearWatchlist } = watchlistSlice.actions;