export interface Genre {
  id: number;
  name: string;
  type?: 'MOVIE' | 'SERIES' | 'BOTH';
  imagePath?: string;
}

export interface ProductionCountry {
  iso_3166_1: string;
  name: string;
}

export interface Movie {
  id: number;
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
  production_countries: ProductionCountry[];
  numberOfEpisodes?: number;
  isWatched?: boolean;
  watchedAt?: string | null;
  origin_country?: string;
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

export interface User {
  username: string;
  firstName: string;
  lastName: string;
  avatarUrl: string;
};