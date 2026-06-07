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

      const existingReaction = state.items[id]?.userReaction;
      const finalReaction = (reaction === null && existingReaction) ? existingReaction : reaction;

      state.items[id] = {
        likes,
        dislikes,
        userReaction: finalReaction
      };
    },

    setInitialReactions: (state, action: PayloadAction<{ id: string; reaction: 'LIKE' | 'DISLIKE' }[]>) => {
      action.payload.forEach(({ id, reaction }) => {
        if (!state.items[id]) {
          state.items[id] = { likes: 0, dislikes: 0, userReaction: reaction };
        } else {
          state.items[id].userReaction = reaction;
        }
      });
    },
  },
});

export const { updateStats, setInitialReactions } = likesSlice.actions;
export default likesSlice.reducer;