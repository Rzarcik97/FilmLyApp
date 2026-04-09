export interface Genre {
  id: number;
  name: string;
}

export interface Movie {
  id?: number;
  contentId?: number;

  title: string | null;
  tagLine: string;
  type: string;
  overview: string;
  status: string;
  runtime: number;
  popularity: number;
  release_date?: string;

  vote_average?: number;
  voteAverage?: number;

  vote_count: number;
  trailerKey: string;

  poster_path?: string;
  posterPath?: string;

  backdrop_path: string;
  genres: Genre[];
}

export interface Actor {
  id: number;
  name: string;
  profile_path?: string;
}

export interface RangeState {
  min: number;
  max: number;
}

export type BrowseGenres = string[];