import { useEffect, useState } from 'react';
import type { Actor, Movie } from '../../types';
import { mockActors, mockMovies } from '../../utils/mockData';
import { AboutUs } from './AboutUs';
import { ScrollActors } from './ScrollActors';
import { ScrollSection } from './ScrollSection';
import { WhatShouldIWatch } from './WhatShouldIWatch';
import { getPopularMovies, getRecentMovies, getTrendingMovies, getTrendingSeries } from '../../api/movieService';


export const MainPage = () => {
  const [trending, setTrending] = useState<Movie[]>([]);
  const [trendingSeries, setTrendingSeries] = useState<Movie[]>([]);
  const [recent, setRecent] = useState<Movie[]>([]);
  const [popular, setPopular] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        const [trendingData, seriesData, recentData, popularData] = await Promise.all([
          getTrendingMovies(),
          getTrendingSeries(),
          getRecentMovies(),
          getPopularMovies(),
        ]);

        const prepareMovies = (movies: Movie[]) => {
          return movies
            .slice(0, 10) // I believe we discussed to take only first 10 items for this
            .map(movie => ({ ...movie }));
        };

        setTrending(prepareMovies(trendingData));
        setTrendingSeries(prepareMovies(seriesData));
        setRecent(prepareMovies(recentData));
        setPopular(prepareMovies(popularData));
      } catch (error) {
        console.error('Failed to load movies from the backend', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAllData();
  }, []);


  return (
    <main className="bg-light-background">
      <AboutUs />
      <ScrollSection title="Trending now" items={loading ? [] : trending} />
      <WhatShouldIWatch />
      <ScrollSection title="Critics’ Choice" items={mockMovies} />
      <ScrollSection title="Top TV Series" items={loading ? [] : trendingSeries} />
      <ScrollSection title="Coming Soon" items={mockMovies} />
      <ScrollActors title="Popular actors" items={mockActors} />
    </main>
  )
}