import { useCallback } from 'react';
import { useDispatch } from 'react-redux';
import { getUserLikes } from '../api/movieService';
import { setInitialReactions } from '../store/likesSlice';
import type { Movie } from '../types';

export const useSyncLikes = () => {
  const dispatch = useDispatch();

  const sync = useCallback(async () => {
    const token = localStorage.getItem('token');
    if (!token) return;

    try {
      const [likedM, dislikedM, likedS, dislikedS] = await Promise.all([
        getUserLikes('MOVIE', true),
        getUserLikes('MOVIE', false),
        getUserLikes('SERIES', true),
        getUserLikes('SERIES', false),
      ]);

      const reactions = [
        ...likedM.map((item: Movie) => ({ id: String(item.contentId), reaction: 'LIKE' })),
        ...dislikedM.map((item: Movie) => ({ id: String(item.contentId), reaction: 'DISLIKE' })),
        ...likedS.map((item: Movie) => ({ id: String(item.contentId), reaction: 'LIKE' })),
        ...dislikedS.map((item: Movie) => ({ id: String(item.contentId), reaction: 'DISLIKE' })),
      ];

      dispatch(setInitialReactions(reactions));
    } catch (error) {
      console.error("Sync Error:", error);
    }
  }, [dispatch]);

  return sync;
};