export interface Movie {
  id: number;
  title: string;
  ratingIMDB: number;
  ratingRT: number;
  poster_path?: string;
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