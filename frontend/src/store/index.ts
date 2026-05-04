import { configureStore } from '@reduxjs/toolkit';
import watchlistReducer from './watchlistSlice';
import userReducer from './userSlice';
import uiReducer from './uiSlice';
import filtersReducer from './filtersSlice';
import likesReducer from './likesSlice';

export const store = configureStore({
  reducer: {
    watchlist: watchlistReducer,
    ui: uiReducer,
    auth: userReducer,
    filters: filtersReducer,
    likes: likesReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;