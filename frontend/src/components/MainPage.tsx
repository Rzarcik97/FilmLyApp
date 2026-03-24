import type { Movie } from '../types';
import { AboutUs } from './AboutUs';
import { MovieCard } from './MovieCard';
import { ScrollSection } from './ScrollSection';
import { WhatShouldIWatch } from './WhatShouldIWatch';

const mockMovies: Movie[] = [
  { id: 1, title: "Inception", poster_path: "", ratingIMDB: 8.8, ratingRT: 87 },
  { id: 2, title: "The Dark Knight", poster_path: "", ratingIMDB: 9.0, ratingRT: 94 },
  { id: 3, title: "Interstellar", poster_path: "", ratingIMDB: 8.7, ratingRT: 73 },
];

export const MainPage = () => {
  return (
    <main className="bg-[#ededed]">
      <AboutUs />
      <ScrollSection title="Trending now" items={mockMovies} />
      <WhatShouldIWatch />
    </main>

  )
}