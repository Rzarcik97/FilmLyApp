import { useEffect, useState } from 'react';
import { type Genre, type Actor, type Movie } from '../../types';
import { mockActors, mockMovies } from '../../utils/mockData';
import { AboutUs } from './AboutUs';
import { ScrollActors } from './ScrollActors';
import { ScrollSection } from './ScrollSection';
import { WhatShouldIWatch } from './WhatShouldIWatch';
import { getGenres, getPopularMovies, getRecentMovies, getTrendingMovies, getTrendingSeries } from '../../api/movieService';
import { MainBrowse } from './MainBrowse';
import { ScrollSectionTrending } from './ScrollSectionTrending';


export const MainPage = () => {
  const [trending, setTrending] = useState<Movie[]>([]);
  const [trendingSeries, setTrendingSeries] = useState<Movie[]>([]);
  const [recent, setRecent] = useState<Movie[]>([]);
  const [popular, setPopular] = useState<Movie[]>([]);
  const [genres, setGenres] = useState<Genre[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        setLoading(true);

        const [trendingData, seriesData, allGenres] = await Promise.all([
          getTrendingMovies(),
          getTrendingSeries(),
          getGenres(),
        ]);

        const prepareMovies = (movies: Movie[]) => {
          return movies
            .slice(0, 10) // I believe we discussed to take only first 10 items for this
            .map(movie => ({ ...movie }));
        };

        setTrending(prepareMovies(trendingData));
        setTrendingSeries(prepareMovies(seriesData));
        setGenres(allGenres);
      } catch (error) {
        console.error('Failed to load movies from the backend', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAllData();
  }, []);


  return (
    <main className="bg-primary-background-dark">
      <AboutUs />
      <MainBrowse genres={genres} />
      <ScrollSectionTrending title="Trending Now" items={loading ? [] : trending} />
      <WhatShouldIWatch />
      <ScrollSection title="Critics’ Choice" items={mockMovies} />
      <ScrollSection title="Top TV Series" items={loading ? [] : trendingSeries} />
      <ScrollSection title="Coming Soon" items={mockMovies} />
      <ScrollActors title="Popular actors" items={mockActors} />
    </main>
  )
}