import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { userService } from '../api/userService';
import type { WatchlistRequest, WatchlistState } from '../types/watchlist';
import type { Movie } from '../types';

export const fetchWatchList = createAsyncThunk(
  'watchlist/fetch',
  async (showWatched?: boolean) => {
    const data = await userService.getWatchList(showWatched);
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
    return payload.contentId;
  }
);

export const markAsWatched = createAsyncThunk(
  'watchlist/markAsWatched',
  async (payload: WatchlistRequest) => {
    return await userService.markAsWatched(payload);
  }
)

const initialState: WatchlistState = {
  items: [],
  watchedItems: [],
  fullList: [],
  loading: false,
  error: null,
};

const watchlistSlice = createSlice({
  name: 'watchlist',
  initialState,
  reducers: {
    clearWatchlist: (state) => {
      state.items = [];
      state.watchedItems = [];
      state.fullList = [];
      state.error = null;
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchWatchList.pending, (state) => { state.loading = true })
      .addCase(fetchWatchList.fulfilled, (state, action) => {
        state.loading = false;

        state.fullList = action.payload.map(item => ({
          ...item,
          id: item.contentId,
          type: item.contentType || 'MOVIE',
          contentType: item.contentType || 'MOVIE',

          title: item.title || 'Unknown Title',
          poster_path: item.posterPath || '',

          overview: '',
          release_date: item.releaseDate || '',
          vote_average: item.voteAverage || 0,
        })) as unknown as Movie[];

        state.items = action.payload.map(item => item.contentId);
        state.watchedItems = action.payload
          .filter(item => item.watchedAt !== null)
          .map(item => item.contentId);
      })
      .addCase(addToWatchlist.fulfilled, (state, action) => {
        const { contentId } = action.meta.arg;
        if (!state.items.includes(contentId)) {
          state.items.push(contentId);
        }
      })
      .addCase(removeFromWatchlist.fulfilled, (state, action) => {
        const removedId = action.payload;

        state.items = state.items.filter(id => id !== removedId);
        state.watchedItems = state.watchedItems.filter(id => id !== removedId);

        state.fullList = state.fullList.filter(movie =>
          movie.id !== removedId && movie.contentId !== removedId
        );
      })
      .addCase(markAsWatched.fulfilled, (state, action) => {
        const { contentId } = action.payload;

        if (!state.watchedItems.includes(contentId)) {
          state.watchedItems.push(contentId);
        }

        if (!state.items.includes(contentId)) {
          state.items.push(contentId);
        }
      })
  },
});

export default watchlistSlice.reducer;
export const { clearWatchlist } = watchlistSlice.actions;