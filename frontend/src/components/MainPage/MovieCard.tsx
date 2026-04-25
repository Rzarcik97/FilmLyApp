import type { Movie } from '../../types';
import { Link, useLocation } from 'react-router-dom';
import imdb from '../../../public/icons/imdb.png';
import thumbUp from '../../../public/icons/thumbUp.png';
import empty_img from '../../../public/icons/empty-img.png';
import { ButtonsWatchlistSeen } from '../OverviewPage/ButtonsWatchlistSeen';

export const MovieCard = ({ movie }: { movie: Movie }) => {
  const location = useLocation();

  const contentType = movie.type.toLowerCase();
  const basePath = contentType === 'movie' ? 'movies' : 'series';

  const posterUrl = (movie.posterPath || movie.poster_path)
    ? `https://image.tmdb.org/t/p/w500${movie.posterPath || movie.poster_path}`
    : empty_img;

  return (
    <div className="group w-[175px] md:w-[203px] h-auto flex flex-col shrink-0 rounded-[16px] overflow-hidden 
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
        <div className="relative h-28 bg-gray-100 overflow-hidden">

          <div className="p-2 flex flex-col h-full transition-all duration-300 group-hover:translate-y-[-100%] group-hover:opacity-0">
            <div className="flex items-center justify-between text-gray-0 text-[16px] font-bold">
              <div className="flex gap-2 items-center">
                <p>{Number(movie.voteAverage || movie.vote_average || 0).toFixed(1)}</p>
                <img src={imdb} alt="IMDB" className="w-5 h-4" />
              </div>
              <div className="flex gap-2 items-center">
                <p>2.1k</p>
                <img src={thumbUp} alt="Filmly" className="w-6 h-6" />
              </div>
            </div>
            <p className="pt-[13px] leading-[1.5] text-gray-0 text-[16px] font-bold">{movie.title || 'Movie Title'}</p>
          </div>

          <div className="absolute inset-0 translate-y-[100%] opacity-0 transition-all duration-300 group-hover:translate-y-0 group-hover:opacity-100 bg-gray-100/90 backdrop-blur-sm">
            <ButtonsWatchlistSeen
              contentId={Number(movie.contentId)}
              contentType={movie.type}
              variant="compact"
            />
          </div>

        </div>
      </Link>
    </div>
  )
}