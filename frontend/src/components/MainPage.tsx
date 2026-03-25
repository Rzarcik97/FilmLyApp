import type { Actor, Movie } from '../types';
import { AboutUs } from './AboutUs';
import { ScrollActors } from './ScrollActors';
import { ScrollSection } from './ScrollSection';
import { WhatShouldIWatch } from './WhatShouldIWatch';

const mockMovies: Movie[] = [
  { id: 1, title: "Inception", poster_path: "", ratingIMDB: 8.8, ratingRT: 87 },
  { id: 2, title: "The Dark Knight", poster_path: "", ratingIMDB: 9.0, ratingRT: 94 },
  { id: 3, title: "Interstellar", poster_path: "", ratingIMDB: 8.7, ratingRT: 73 },
];

const mockActors: Actor[] = [
  {id: 1, name: "Zendaya"},
  {id: 2, name: "Tom Holland"},
  {id: 3, name: "Emma Stone"},
  {id: 4, name: "Matt Damon"},
  {id: 5, name: "Jonah Hill"},
]

export const MainPage = () => {
  return (
    <main className="bg-[#ededed]">
      <AboutUs />
      <ScrollSection title="Trending now" items={mockMovies} />
      <WhatShouldIWatch />
      <ScrollSection title="Critics’ Choice" items={mockMovies} />
      <ScrollSection title="Top TV Series" items={mockMovies} />
      <ScrollSection title="Coming Soon" items={mockMovies} />
      <ScrollActors title="Popular actors" items={mockActors} />
    </main>
  )
}