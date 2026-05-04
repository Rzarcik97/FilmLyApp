import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

export interface LikeState {
  items: Record<string, {
    likes: number;
    dislikes: number;
    userReaction: 'LIKE' | 'DISLIKE' | null;
  }>;
}

const initialState: LikeState = { items: {} };

const likesSlice = createSlice({
  name: 'likes',
  initialState,
  reducers: {
    updateStats: (state, action: PayloadAction<{
      id: string;
      likes: number;
      dislikes: number;
      reaction: 'LIKE' | 'DISLIKE' | null;
    }>) => {
      const { id, likes, dislikes, reaction } = action.payload;
      state.items[id] = { likes, dislikes, userReaction: reaction };
    },
  },
});

export const { updateStats } = likesSlice.actions;
export default likesSlice.reducer;