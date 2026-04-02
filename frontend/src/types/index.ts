export interface Genre {
  id: number;
  name: string;
}

export interface Movie {
  id: number;
  title: string;
  overview: string;
  status: string;
  runtime: number;
  popularity: number;
  vote_average: number;
  vote_count: number;
  trailerKey: string;
  poster_path: string;
  backdrop_path: string;
  genres: Genre[];
}

export interface Actor {
  id: number;
  name: string;
}

export interface RangeState {
  min: number;
  max: number;
}

export type BrowseGenres = string[];