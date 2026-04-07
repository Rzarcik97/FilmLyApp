import { ChevronLeft } from 'lucide-react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import imdb from '../../../public/icons/imdb.png';
import thumbsUp from '../../../public/icons/thumbUp-primary.png';
import play from '../../../public/icons/play.png';
import mute from '../../../public/icons/mute.png';
import { useEffect, useState } from 'react';
import type { Movie } from '../../types';
import { getMovieDetails } from '../../api/movieService';

export const MainOverview = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<{ id: string }>();
  const [movie, setMovie] = useState<Movie | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMovie = async () => {
      if (!id) return;
      try {
        setLoading(true);
        const data = await getMovieDetails(id);
        setMovie(data);
      } catch (error) {
        console.error('Error fetching movie details occurred: ', error);
      } finally {
        setLoading(false);
      }
    };
    fetchMovie();
  }, [id]);

  // if (loading) {
  //   return (

  //   )
  // }

  // if (!movie) {
  //   return (

  //   )
  // } add logic for loading and no movie found later

  const genres = movie?.genres;

  const navigateBack = () => {
    if (location.state?.from) {
      navigate(location.state.from);
    } else {
      window.location.href = "/";
    }
  };

  const backdropUrl = movie?.backdrop_path
    ? `https://image.tmdb.org/t/p/original${movie.backdrop_path}`
    : '';

  return (
    <div
      className="bg-gray-100 px-10 min-h-screen bg-cover bg-center bg-no-repeat relative flex flex-col -mt-[111px]"
      style={{
        backgroundImage: `linear-gradient(to top, rgba(0,0,0,0.8) 0%, rgba(0,0,0,0.3) 40%, transparent 100%), url(${backdropUrl})`
      }}
    >
      <div className="flex justify-between items-center pt-36">
        <button
          className="flex justify-center items-center cursor-pointer"
          onClick={(e) => {
            e.preventDefault();
            navigateBack();
          }}
        >
          <span className="text-secondary-light">
            <ChevronLeft size={32} />
          </span>
          <p className="text-[24px] text-secondary-light">Back</p>
        </button>
      </div>

      <div className="flex-grow" />

      <div className="flex justify-between items-end pb-8">
        <div className="w-[432px] flex flex-col gap-2">
          <h1 className="text-[48px] text-gray-0 font-bold text-center font-nunito leading-[1]">{movie?.title || 'Movie Title'}</h1>
          <p className="text-[20px] leading-[1.45] text-gray-0 font-semibold text-center font-nunito">Slogan</p>

          <div className="flex flex-wrap justify-center items-center gap-2">
            {genres?.map((genre) => (
              <span
                key={genre.id}
                className="px-[10px] py-[2.5px] pb-[5px] text-gray-30 text-[16px] border border-gray-30 rounded-[32px]"
              >
                {genre.name}
              </span>
            ))}
          </div>
          <p className="text-[20px] text-gray-30 text-center leading-[1.45]">
            {movie?.release_date?.split('-')[0] || 'YYYY'} • {movie?.runtime}m • TV-Ma
          </p>

          <div className="flex justify-evenly items-center">
            <div className="flex justify-center items-center gap-2">
              <p className="text-[16px] leading-[1.5] text-gray-0 font-bold">{movie?.voteAverage || movie?.vote_average || '0.0'}</p>
              <img src={imdb} alt="Imdb Rating" className="w-6 h-5" />
            </div>
            <div className="flex justify-center items-center gap-2">
              <p className="text-[16px] leading-[1.5] text-gray-0 font-bold">85%</p>
              <img src={thumbsUp} alt="Filmly Rating" className="w-8 h-8" />
            </div>
          </div>
        </div>

        <div className="flex gap-[30px]">
            <img src={play} alt="Play Button" className="cursor-pointer w-8 h-8" />
            <img src={mute} alt="Play Button" className="cursor-pointer w-8 h-8" />
        </div>
      </div>
      
    </div>
  )
}