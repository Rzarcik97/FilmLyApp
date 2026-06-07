import { useEffect } from 'react';
import type { Movie } from '../../types';
import { ButtonsLikeDislike } from './ButtonsLikeDislike';
import { ButtonsWatchlistSeen } from './ButtonsWatchlistSeen';
import { useDispatch } from 'react-redux';
import { updateStats } from '../../store/likesSlice';

interface MoreDetailsProps {
  movie: Movie;
}

export const MoreDetails = ({ movie }: MoreDetailsProps) => {
  const dispatch = useDispatch();

  useEffect(() => {
    if (movie) {
      dispatch(updateStats({
        id: String(movie.id),
        likes: movie.likes || 0,
        dislikes: movie.dislikes || 0,
        reaction: movie.userReaction || null
      }));
    }
  }, [movie, dispatch]);
  
  return (
    <div className="px-4 md:px-12 pt-8 bg-main-bg">
      <div className="flex justify-center md:justify-between items-center pb-[42px]">
        <ButtonsWatchlistSeen contentId={movie.id} contentType={movie.type || ''} />
        <ButtonsLikeDislike contentId={movie.id} contentType={movie.type || ''} />
      </div>

      <div className="py-6 border-b border-secondary-light flex flex-col gap-8">
        <h1 className="text-[36px] text-secondary-light font-bold leading-[1.2] font-nunito">More Details</h1>
        {movie.overview && (
          <p className="text-[20px] text-gray-50-text font-semibold">{movie?.overview}</p>
        )}
        <div className="flex justify-start items-center gap-1 text-[24px] leading-[1.3] font-semibold font-nunito">
          {movie.production_countries.length === 1 ? (
            <p className="text-gray-80">Production Country:</p>
          ) : (
              <p className="text-gray-80">Production Counties:</p>
          )}
          <span className="text-featured">{movie.production_countries.map(country => country.name).join(', ')}</span>
        </div>
      </div>
    </div>
  )
}