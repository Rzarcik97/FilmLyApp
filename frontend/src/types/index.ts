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