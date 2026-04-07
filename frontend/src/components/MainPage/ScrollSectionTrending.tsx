import { useRef } from 'react';
import type { Movie } from '../../types'
import { MovieCard } from './MovieCard';
import { ChevronLeft, ChevronRight } from 'lucide-react';

interface ScrollSectionTrendingProps {
  items: Movie[];
  title: string;
}

export const ScrollSectionTrending = ({ title, items }: ScrollSectionTrendingProps) => {
  const scrollRef = useRef<HTMLDivElement>(null);

  const scroll = (direction: 'left' | 'right') => {
    if (scrollRef.current) {
      const { scrollLeft, clientWidth } = scrollRef.current;
      const scrollAmount = clientWidth * 0.8;

      const scrollTo = direction === 'left'
        ? scrollLeft - scrollAmount
        : scrollLeft + scrollAmount;

      scrollRef.current.scrollTo({ left: scrollTo, behavior: 'smooth' });
    }
  };

  return (
    <section className="py-10 px-12">
      <div className="flex justify-between items-center">
        <div className="flex gap-2 items-center">
          <div className="w-1 h-[35px] bg-primary-0 rounded-full" />
          <h2 className="text-[36px] leading-[1.2] font-bold text-secondary-light">{title}</h2>
        </div>
        
        <button className="cursor-pointer text-[16px] text-secondary-light font-nunito font-bold">
          View all
        </button>
      </div>

      <div className="flex justify-center items-center gap-2">
        <button
          onClick={() => scroll('left')}
          className="text-primary-0 cursor-pointer
          w-12 h-12 flex justify-center items-center
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          "
        >
          <ChevronLeft size={24} />
        </button>
        <div
          ref={scrollRef}
          className="flex flex-nowrap w-full gap-6 overflow-x-hidden scroll-smooth px-12 py-4"
        >
          {items.map((item, index) => (
            <div 
              key={item.id}
              className="relative flex-none w-[240px] snap-start"
            >
              <span className="
                absolute -left-12 top-3/4 -translate-y-1/2 
                text-[200px] font-bold leading-none
                text-primary-0/20
                select-none z-0
                font-nunito tracking-tighter
                opacity-80 blur-[0.5px]"
              >
                {index + 1}
              </span>

              <div className="relative z-10">
                <MovieCard movie={item} />
              </div>
            </div>
          ))}
        </div>
        <button
          onClick={() => scroll('right')}
          className="text-primary-0 cursor-pointer
          w-12 h-12 flex justify-center items-center
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          "
        >
          <ChevronRight size={24} />
        </button>
      </div>
    </section>
  )
}