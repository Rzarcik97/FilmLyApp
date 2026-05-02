import { ChevronLeft, ChevronRight } from 'lucide-react';
import { Loader } from '../Utilities/Loader';
import { MovieCard } from '../MainPage/MovieCard';
import type { Movie } from '../../types';

interface DiscoverByGenreProps {
  items: Movie[];
  isLoading: boolean;
  page: number;
  totalPages: number;
  hasMore: boolean;
  onLoadMore: () => void;
  onJumpToPage: (targetPage: number) => void; 
}

export const DiscoverByGenre = ({
  items,
  isLoading,
  page,
  totalPages,
  hasMore,
  onLoadMore,
  onJumpToPage,
}: DiscoverByGenreProps) => {
  const renderPageNumbers = () => {
    const pages = [];
    const start = Math.max(1, page - 1);
    const end = Math.min(totalPages, page + 1);

    for (let i = start; i <= end; i++) {
      pages.push(
        <button
          key={i}
          onClick={() => onJumpToPage(i)}
          className={`cursor-pointer w-10 h-10 rounded-full font-bold transition-all ${page === i
            ? 'bg-primary-0 text-black'
            : 'text-secondary-light hover:bg-gray-80/20'
            }`}
        >
          {i}
        </button>
      );
    }
    return pages;
  };

  return (
    <div className="flex flex-col items-center w-full">
      <div className="grid grid-cols-[repeat(auto-fill,minmax(204px,1fr))] gap-x-4 gap-y-8 w-full justify-items-center">
        {items.map((movie) => (
          <MovieCard key={`${movie.id}-${movie.contentId}`} movie={movie} />
        ))}
      </div>

      {isLoading && <Loader />}

      {hasMore && !isLoading && items.length > 0 && (
        <button
          onClick={onLoadMore}
          className="mt-10 px-8 py-3 bg-primary-0 text-black font-bold rounded-full 
                     hover:scale-105 transition-all duration-300 shadow-lg cursor-pointer"
        >
          Load More
        </button>
      )}

      <div className="flex items-center gap-4 mt-4 mb-8">
        <button
          onClick={() => onJumpToPage(page - 1)}
          disabled={page === 1}
          className="relative text-primary-0 cursor-pointer w-12 h-12 flex justify-center items-center
                     bg-gray-80/10 backdrop-blur-[2px] rounded-full border border-gray-80/10
                     before:content-[''] before:absolute before:inset-0 before:rounded-full 
                     before:border before:border-gray-80/20 hover:bg-gray-30/10 
                     transition-all duration-300 ease-in-out disabled:opacity-20"
        >
          <ChevronLeft size={24} />
        </button>

        <div className="flex items-center gap-2">
          {renderPageNumbers()}
        </div>

        <button
          onClick={() => onJumpToPage(page + 1)}
          disabled={page === totalPages}
          className="relative text-primary-0 cursor-pointer w-12 h-12 flex justify-center items-center
                     bg-gray-80/10 backdrop-blur-[2px] rounded-full border border-gray-80/10
                     before:content-[''] before:absolute before:inset-0 before:rounded-full 
                     before:border before:border-gray-80/20 hover:bg-gray-30/10 
                     transition-all duration-300 ease-in-out disabled:opacity-20"
        >
          <ChevronRight size={24} />
        </button>
      </div>
    </div>
  );
}