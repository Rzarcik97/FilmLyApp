import { configureStore } from '@reduxjs/toolkit';
import watchlistReducer from './watchlistSlice';
import userReducer from './userSlice';
import uiReducer from './uiSlice';
import filtersReducer from './filtersSlice';

export const store = configureStore({
  reducer: {
    watchlist: watchlistReducer,
    ui: uiReducer,
    auth: userReducer,
    filters: filtersReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;