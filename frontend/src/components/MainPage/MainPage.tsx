import { useEffect, useState } from 'react';
import { type Genre, type Actor, type Movie } from '../../types';
import { mockActors, mockMovies } from '../../utils/mockData';
import { AboutUs } from './AboutUs';
import { ScrollActors } from './ScrollActors';
import { ScrollSection } from './ScrollSection';
import { WhatShouldIWatch } from './WhatShouldIWatch';
import { getGenres, getPopularActors, getPopularMovies, getRecentMovies, getTrendingMovies, getTrendingSeries } from '../../api/movieService';
import { MainBrowse } from './MainBrowse';
import { ScrollSectionTrending } from './ScrollSectionTrending';


export const MainPage = () => {
  const [trending, setTrending] = useState<Movie[]>([]);
  const [trendingSeries, setTrendingSeries] = useState<Movie[]>([]);
  const [recent, setRecent] = useState<Movie[]>([]);
  const [popular, setPopular] = useState<Movie[]>([]);
  const [actors, setActors] = useState<Actor[]>([]);
  const [genres, setGenres] = useState<Genre[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        setLoading(true);

        const results = await Promise.allSettled([
          getTrendingMovies(),
          getTrendingSeries(),
          getGenres(),
          getPopularMovies(),
          getRecentMovies(),
          getPopularActors()
        ]);

        const getValue = <T,>(result: PromiseSettledResult<T>, fallback: T): T => {
          return result.status === 'fulfilled' ? result.value : fallback;
        };

        const [
          trendingRes,
          seriesRes,
          genresRes,
          popularRes,
          recentRes,
          actorsRes
        ] = results;

        const prepareMovies = (movies: Movie[]) => {
          return movies
            .filter(movie => movie.posterPath !== null)
            .slice(0, 10)
            .map(movie => ({ ...movie }));
        };

        const prepareActors = (actors: Actor[]) => {
          return actors
            .filter(actor => actor.profile_path !== null)
            .slice(0, 10)
            .map(actor => ({ ...actor }));
        };

        setTrending(prepareMovies(getValue(trendingRes, [])));
        setTrendingSeries(prepareMovies(getValue(seriesRes, [])));
        setGenres(getValue(genresRes, []));
        setPopular(prepareMovies(getValue(popularRes, [])));
        setRecent(prepareMovies(getValue(recentRes, [])));
        setActors(prepareActors(getValue(actorsRes, [])));

      } catch (error) {
        console.error('Critical failure in fetchAllData', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAllData();
  }, []);

  return (
    <main className="bg-gray-100">
      <AboutUs />
      <MainBrowse genres={genres} />
      <ScrollSectionTrending title="Trending Now" items={loading ? [] : trending} />
      <WhatShouldIWatch />
      <ScrollSection title="Critics’ Choice" items={loading ? [] : popular} />
      <ScrollSection title="Top TV Series" items={loading ? [] : trendingSeries} />
      <ScrollSection title="Latest Premieres" items={loading ? [] : recent} />
      <ScrollActors title="Popular actors" items={loading ? [] : actors} />
    </main>
  )
}