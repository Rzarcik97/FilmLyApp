import { useRef } from 'react';
import type { Movie } from '../../types'
import { MovieCard } from './MovieCard';
import { ChevronLeft, ChevronRight } from 'lucide-react';

interface ScrollSectionProps {
  items: Movie[];
  title: string;
}

export const ScrollSection = ({ title, items }: ScrollSectionProps) => {
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
      <div className="flex justify-between items-center pb-2">
        <h2 className="text-[48px] fw-bold">{title}</h2>
        <button className="cursor-pointer text-[20px]">
          View all
        </button>
      </div>

      <div className="flex justify-center items-center gap-2">
        <button
          onClick={() => scroll('left')}
          className="text-[#626262] cursor-pointer"
        >
          <ChevronLeft size={24} />
        </button>
        <div
          ref={scrollRef}
          className="flex flex-nowrap w-full gap-4 overflow-x-hidden scroll-smooth"
        >
          {items.map(item => (
            <MovieCard key={item.id} movie={item} />
          ))}
        </div>
        <button
          onClick={() => scroll('right')}
          className="text-[#626262] cursor-pointer"
        >
          <ChevronRight size={24} />
        </button>
      </div>
    </section>
  )
}