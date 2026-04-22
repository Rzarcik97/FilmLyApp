import { useParams } from 'react-router-dom';
import { ScrollActors } from '../MainPage/ScrollActors';
import { ScrollSection } from '../MainPage/ScrollSection';
import { MainOverview } from './MainOverview';
import { MoreDetails } from './MoreDetails';
import { useEffect, useState } from 'react';
import { getMovieCast, getMovieDetails, getSimilarContent } from '../../api/movieService';
import { type Actor, type Movie } from '../../types';
import { Loader } from '../Utilities/Loader';
import { NotFoundPage } from '../Utilities/NotFoundPage';

export const OverviewPage = () => {
  const { id, type } = useParams<{ id: string, type: string }>();
  const [movie, setMovie] = useState<Movie | null>(null);
  const [cast, setCast] = useState<Actor[]>([]);
  const [similarItems, setSimilarItems] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMovie = async () => {
      if (!id || !type) return;
      try {
        setLoading(true);

        const movieData = await getMovieDetails(id, type);
        setMovie(movieData);

        const results = await Promise.allSettled([
          getMovieCast(id, type),
          getSimilarContent(id, movieData.type)
        ]);

        const getValue = <T,>(result: PromiseSettledResult<T>, fallback: T): T => {
          return result.status === 'fulfilled' ? result.value : fallback;
        };

        const [castRes, similarRes] = results;

        const prepareMovies = (movies: Movie[]) => {
          return movies
            .filter(movie => movie.posterPath !== null)
            .slice(0, 10)
            .map(movie => ({ ...movie }));
        };

        setCast(getValue(castRes, []));
        setSimilarItems(prepareMovies(getValue(similarRes, [])));
      } catch (error) {
        console.error('Error fetching movie details occurred: ', error);
      } finally {
        setLoading(false);
      }
    };
    fetchMovie();
  }, [id, type]);

  if (loading) {
    return (
      <div className="fixed inset-0 w-screen h-screen bg-gray-100 flex flex-col items-center justify-center z-[9999]">
        <Loader />
      </div>
    );
  };

  if (!movie) {
    return (
      <NotFoundPage />
    )
  }

  return (
    <div className="bg-gray-100">
      <MainOverview movie={movie} />
      <MoreDetails movie={movie} />
      <ScrollActors title="Cast" items={cast} />
      <ScrollSection 
        title="Yoy may also like" 
        items={similarItems} 
        viewAllPath='/actors'
      />
    </div>
  )
}