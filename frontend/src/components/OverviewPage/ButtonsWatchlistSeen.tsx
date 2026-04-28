import { useDispatch, useSelector } from 'react-redux';
import watchlist from '../../../public/icons/watchlist_popcorn.png';
import { Check } from 'lucide-react';
import { type AppDispatch, type RootState } from '../../store/index';
import { addToWatchlist, markAsWatched, removeFromWatchlist } from '../../store/watchlistSlice';
import { openAuthModal } from '../../store/uiSlice';

interface ButtonsProps {
  contentId: number;
  contentType: string;
  variant?: 'default' | 'compact';
}

export const ButtonsWatchlistSeen = ({ contentId, contentType, variant = 'default' }: ButtonsProps) => {
  const dispatch = useDispatch<AppDispatch>();
  const watchlistIds = useSelector((state: RootState) => state.watchlist.items);
  const isAdded = watchlistIds.includes(contentId);
  const watchedIds = useSelector((state: RootState) => state.watchlist.watchedItems);
  const isWatched = watchedIds.includes(contentId);
  const isLoggedIn = !!localStorage.getItem('token');

  const handleWatchlistToggle = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();

    if (!isLoggedIn) {
      dispatch(openAuthModal());
      return;
    }

    const payload = { contentId, contentType };
    if (isAdded) {
      dispatch(removeFromWatchlist(payload));
    } else {
      dispatch(addToWatchlist(payload));
    }
  };

  const handleMarkWatched = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();

    if (!isWatched) {
      dispatch(markAsWatched({ contentId, contentType }));
    }
  };

  if (variant === 'compact') {
    return (
      <div className="flex flex-col gap-2 w-full h-full justify-center px-3 py-2 font-nunito font-semibold">
        <button
          onClick={handleWatchlistToggle}
          className={`cursor-pointer w-full h-10 flex justify-center items-center gap-2 border-1 rounded-[9px] transition-all
            ${isAdded ? 'bg-primary-0 border-primary-10' : 'bg-gray-100 border-primary-0'}`}
        >
          <img src={watchlist} alt="icon" className="w-4 h-4" />
          <span className={`${isAdded ? 'text-black' : 'text-primary-0'} text-[11px]`}>
            {isAdded ? 'Added to your Watchlist' : 'Add to Watchlist'}
          </span>
        </button>

        <button
          onClick={handleMarkWatched}
          className={`cursor-pointer w-full h-10 flex justify-center items-center gap-2 border-1 rounded-[9px] transition-all
            ${isWatched ? 'bg-secondary-dark border-gray-80' : 'bg-gray-100 border-primary-0'}`}
        >
          {isWatched ? <Check size={10} className="text-primary-0" /> : null}
          <span className={`${isWatched ? 'text-gray-30' : 'text-primary-0'} text-[11px]`}>
            {isWatched ? 'Seen it' : 'Mark as watched'}
          </span>
        </button>
      </div>
    );
  }

  return (
    <div className="flex justify-center items-center gap-2 md:gap-6 text-[16px] font-nunito font-semibold leading-[1]">
      <button
        onClick={handleWatchlistToggle}
        className={`cursor-pointer w-[175px] md:w-51 h-8 md:h-14 flex justify-center items-center gap-2 border-1 rounded-[9px] transition-all
          ${isAdded
            ? 'bg-primary-0 border-primary-10'
            : 'bg-gray-100 border-primary-0'}`}
      >
        <img src={watchlist} alt="Add to watchlist" className="w-6 h-6" />
        {isAdded ? (
          <span className="text-black text-[13px]">Added to your Watchlist</span>
        ) : (
          <span className="text-primary-0 text-[13px]">Add to Watchlist</span>
        )}

      </button>
      <button
        onClick={handleMarkWatched}
        className={`cursor-pointer w-[175px] md:w-51 h-8 md:h-14 flex justify-center items-center gap-2 border-1 rounded-[9px]
          ${isWatched
            ? 'bg-secondary-dark border-gray-80'
            : 'bg-gray-100 border-primary-0'}`}
      >
        {isWatched ? (
          <>
            <Check size={12} className="text-primary-0" />
            <span className="text-gray-30 text-[13px]">Seen it</span>
          </>
        ) : (
            <span className="text-primary-0 text-[13px]">Mark as watched</span>
        )}

      </button>
    </div>
  )
}