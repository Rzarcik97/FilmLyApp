import type { Actor, Movie } from '../../types';
import { mockActors, mockMovies } from '../../utils/mockData';
import { AboutUs } from './AboutUs';
import { ScrollActors } from './ScrollActors';
import { ScrollSection } from './ScrollSection';
import { WhatShouldIWatch } from './WhatShouldIWatch';


export const MainPage = () => {
  return (
    <main className="bg-light-background">
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