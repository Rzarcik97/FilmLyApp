import { useParams } from 'react-router-dom';
import { mockActors, mockMovies } from '../../utils/mockData';
import { ScrollActors } from '../MainPage/ScrollActors';
import { ScrollSection } from '../MainPage/ScrollSection';
import { MainOverview } from './MainOverview';
import { MoreDetails } from './MoreDetails';
import { useEffect, useState } from 'react';
import { getMovieCast, getMovieDetails, getSimilarContent } from '../../api/movieService';
import { type Actor, type Movie } from '../../types';

export const OverviewPage = () => {
  const { id } = useParams<{ id: string }>();
  const [movie, setMovie] = useState<Movie | null>(null);
  const [cast, setCast] = useState<Actor[]>([]);
  const [similarItems, setSimilarItems] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMovie = async () => {
      if (!id) return;
      try {
        setLoading(true);

        const movieData = await getMovieDetails(id);
        setMovie(movieData);

        const [castData, similarData] = await Promise.all([
          getMovieCast(id),
          getSimilarContent(id, movieData.type)
        ]);

        setCast(castData);
        setSimilarItems(similarData);
      } catch (error) {
        console.error('Error fetching movie details occurred: ', error);
      } finally {
        setLoading(false);
      }
    };
    fetchMovie();
  }, [id]);

  if (loading) return <div>Loading...</div>;
  if (!movie) return <div>Movie not found.</div>;

  return (
    <div className="bg-gray-100">
      <MainOverview movie={movie} />
      <MoreDetails movie={movie} />
      <ScrollActors title="Cast" items={cast} />
      <ScrollSection title="Yoy may also like" items={similarItems} />
    </div>
  )
}