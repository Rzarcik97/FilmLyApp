import type { Actor, Movie } from '../types';

export const mockMovies: Movie[] = [
  { id: 1, title: "Inception", poster_path: "", ratingIMDB: 8.8, ratingRT: 87 },
  { id: 2, title: "The Dark Knight", poster_path: "", ratingIMDB: 9.0, ratingRT: 94 },
  { id: 3, title: "Interstellar", poster_path: "", ratingIMDB: 8.7, ratingRT: 73 },
];

export const mockActors: Actor[] = [
  { id: 1, name: "Zendaya" },
  { id: 2, name: "Tom Holland" },
  { id: 3, name: "Emma Stone" },
  { id: 4, name: "Matt Damon" },
  { id: 5, name: "Jonah Hill" },
];