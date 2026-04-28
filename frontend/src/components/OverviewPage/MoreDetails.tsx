import type { Movie } from '../../types';
import { ButtonsLikeDislike } from './ButtonsLikeDislike';
import { ButtonsWatchlistSeen } from './ButtonsWatchlistSeen';

interface MoreDetailsProps {
  movie: Movie;
}

export const MoreDetails = ({ movie }: MoreDetailsProps) => {
  return (
    <div className="px-4 md:px-12 pt-8 bg-gray-100">
      <div className="flex justify-center md:justify-between items-center pb-[42px]">
        <ButtonsWatchlistSeen contentId={movie.id} contentType={movie.type} />
        <ButtonsLikeDislike />
      </div>

      <div className="py-6 border-b border-secondary-light flex flex-col gap-8">
        <h1 className="text-[36px] text-secondary-light font-bold leading-[1.2] font-nunito">More Details</h1>
        {movie.overview && (
          <p className="text-[20px] text-gray-50 font-semibold">{movie?.overview}</p>
        )}
        <div className="flex justify-start items-center gap-1 text-[24px] leading-[1.3] font-semibold font-nunito">
          {movie.production_countries.length === 1 ? (
            <p className="text-gray-80">Production Country:</p>
          ) : (
              <p className="text-gray-80">Production Counties:</p>
          )}
          <span className="text-primary-0">{movie.production_countries.map(country => country.name).join(', ')}</span>
        </div>
      </div>
    </div>
  )
}