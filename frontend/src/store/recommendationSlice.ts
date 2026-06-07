import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import apiClient from '../api/apiClient';
import { type RecommendedMovie } from '../types';

export const fetchRecommendations = createAsyncThunk(
  'movies/recommendations',
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.get<RecommendedMovie[]>('/movies/recommendations');
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response.data);
    }
  }
);

const recommendationsSlice = createSlice({
  name: 'recommendations',
  initialState: {
    items: [] as RecommendedMovie[],
    isLoading: false,
    error: null as string | null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchRecommendations.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(fetchRecommendations.fulfilled, (state, action) => {
        state.isLoading = false;
        state.items = action.payload;
      })
      .addCase(fetchRecommendations.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export default recommendationsSlice.reducer;