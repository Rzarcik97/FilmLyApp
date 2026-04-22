import type { Movie } from '../../types';
import { Link, useLocation } from 'react-router-dom';
import imdb from '../../../public/icons/imdb.png';
import thumbUp from '../../../public/icons/thumbUp.png';
import { UserStar } from 'lucide-react';
import empty_img from '../../../public/icons/empty-img.png';

export const MovieCard = ({ movie }: { movie: Movie }) => {
  const location = useLocation();

  const contentType = movie.type.toLowerCase();
  const basePath = contentType === 'movie' ? 'movies' : 'series';

  const posterUrl = (movie.posterPath || movie.poster_path)
    ? `https://image.tmdb.org/t/p/w500${movie.posterPath || movie.poster_path}`
    : empty_img;

  return (
    <div className="w-[180px] md:w-[203px] h-auto flex flex-col shrink-0 rounded-[16px] overflow-hidden 
          border border-gray-30/10 backdrop-blur-[2px]
          before:content-[''] before:absolute before:inset-0
          before:rounded-[16px] before:border before:border-gray-80/20
          before:pointer-events-none
          cursor-pointer
    ">
      <Link
        to={`/${basePath}/${movie.contentId}`}
        state={{ from: location.pathname }}
      >
        <div className="h-[328px] flex-1 flex justify-center items-center">
          <img
            src={posterUrl}
            alt={movie.title || "Movie Poster"}
            className={`w-full h-full ${posterUrl === empty_img ? 'object-contain p-8' : 'object-cover'}`}
          />
        </div>
        <div className="p-2 bg-gray-100 h-28 shrink-0 text-gray-30 text-[16px] font-bold">
          <div className="flex items-center justify-between">
            <div className="flex gap-2 items-center">
              <p className="">
                {movie.voteAverage || movie.vote_average
                  ? Number(movie.voteAverage || movie.vote_average).toFixed(1)
                  : '0.0'}
              </p>
              <img src={imdb} alt="IMDB Rating" className="w-6 h-5" />
            </div>
            <div className="flex gap-2 items-center">
              <p className="">2.1k</p>
              <img src={thumbUp} alt="Filmly Rating" className="w-8 h-8" />
            </div>
          </div>
          <p className="pt-[13px] leading-[1.5]">{movie.title || 'Movie Title'}</p>
        </div>
      </Link>
    </div>
  )
}