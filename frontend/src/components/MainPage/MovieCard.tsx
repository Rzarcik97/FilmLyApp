import type { Movie } from '../../types';
import { Link, useLocation } from 'react-router-dom';
import imdb from '../../../public/icons/imdb.png';
import { UserStar } from 'lucide-react';
import empty_img from '../../../public/icons/empty-img.png';

export const MovieCard = ({ movie }: { movie: Movie }) => {
  const location = useLocation();

  return (
    <div className="w-[200px] h-auto flex flex-col shrink-0">
      <Link
        to={`/movie/${movie.id}`}
        state={{ from: location.pathname }}
      >
        <div className="bg-primary-background h-[328px] flex-1 flex justify-center items-center">
          <img
            src={
              (movie.posterPath || movie.poster_path)
                ? `https://image.tmdb.org/t/p/w500${movie.posterPath || movie.poster_path}`
                : empty_img
            }
            alt={movie.title || "Movie Poster"}
            className="w-full h-full object-cover"
          />
        </div>
        <div className="p-2 bg-dark-background h-auto shrink-0">
          {/* h-auto replace h-18 here because of that Mario Movie, I need to ask a designer what to do in such cases */}
          <div className="flex items-center justify-between text-white">
            <div className="flex gap-[7px] items-center">
              <p className="text-[16px]">
                {movie.voteAverage || movie.vote_average
                  ? Number(movie.voteAverage || movie.vote_average).toFixed(1)
                  : '0.0'}
              </p>
              <img src={imdb} alt="IMDB Rating" className="w-6 h-6" />
            </div>
            <div className="flex gap-[7px] items-center">
              <p className="text-[16px]">85%</p>
              <UserStar size={22} />
              {/* <img src={rt} alt="Rotten Tomatoes Rating" className="w-6 h-6" /> */}
            </div>
          </div>
          <p className="pt-[13px] text-white text-[16px]">{movie.title || 'Movie Title'}</p>
        </div>
      </Link>
    </div>
  )
}