import { configureStore } from '@reduxjs/toolkit';
import watchlistReducer from './watchlistSlice';
import uiReducer from './uiSlice';

export const store = configureStore({
  reducer: {
    watchlist: watchlistReducer,
    ui: uiReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;