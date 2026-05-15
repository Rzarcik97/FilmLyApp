import apiClient from './apiClient'

export const addFavoriteGenre = async (genreName: string, rating: number) => {
    const response = await apiClient.post('/users/favorite-genres', {
      genreName,
      rating,
    });
    return response.data;
};

export const deleteFavoriteGenre = async (genreName: string) => {
  const response = await apiClient.delete('/users/favorite-genres', {
    params: { genreName },
  });
  return response.data;
};

export const updateFavoriteGenre = async (genreName: string, rating: number) => {
  const response = await apiClient.patch('/users/favorite-genres', {
    genreName,
    rating,
  });
  return response.data;
};

export const getFavoriteGenres = async () => {
  const response = await apiClient.get('/users/favorite-genres');
  return response.data;
};