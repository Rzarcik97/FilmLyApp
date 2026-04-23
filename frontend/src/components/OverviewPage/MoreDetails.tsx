import { ThumbsUp, ThumbsDown, Check } from 'lucide-react';
import watchlist from '../../../public/icons/watchlist_popcorn.png';
import thumbsUp from '../../../public/icons/thumbUp.png';
import thumbsUpPrimary from '../../../public/icons/thumbUp-primary.png';
import thumbsDown from '../../../public/icons/thumbDown.png';
import type { Movie } from '../../types';

interface MoreDetailsProps {
  movie: Movie;
}

export const MoreDetails = ({ movie }: MoreDetailsProps) => {
  return (
    <div className="px-4 md:px-12 pt-8 bg-gray-100">
      <div className="flex justify-center md:justify-between items-center pb-[42px]">
        <div className="flex justify-center items-center gap-2 md:gap-6 text-[16px] font-nunito font-semibold leading-[1]">
          <button className="text-black bg-primary-0 cursor-pointer w-[175px] md:w-51 h-8 md:h-14 flex justify-center items-center gap-2 border-1 border-primary-10 rounded-[9px]">
            <img src={watchlist} alt="Add to watchlist" className="w-6 h-6" />
            <span className="text-black text-[13px]">Watchlist</span>
          </button>
          <button className="text-primary-0 bg-secondary-dark cursor-pointer w-[175px] md:w-51 h-8 md:h-14 flex justify-center items-center gap-2 border-1 border-gray-80 rounded-[9px]">
            <Check size={12} />
            <span className="text-gray-30 text-[13px]">Seen it</span>
          </button>
        </div>

        <div className="hidden md:flex justify-center items-center gap-6">
          <button className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
            <img src={thumbsUpPrimary} alt="Likes" className="w-6 h-6 object-cover" />
            <span className="text-white text-[12px] font-semibold font-nunito">82k</span>
          </button>
          <button className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
            <img src={thumbsDown} alt="Dislikes" className="w-6 h-6 object-cover" />
            <span className="text-white text-[12px] font-semibold font-nunito">2.8k</span>
          </button>
        </div>
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