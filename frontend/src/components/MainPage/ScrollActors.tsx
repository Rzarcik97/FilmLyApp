import { useRef } from 'react';
import type { Actor } from '../../types'
import { ActorCard } from './ActorCard';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { Link } from 'react-router-dom';

interface ScrollActorsProps {
  items: Actor[];
  title: string;
}

export const ScrollActors = ({ title, items }: ScrollActorsProps) => {
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
    <section className="px-12 pt-18 pb-2">
      <div className="flex justify-between items-center">
        <div className="flex gap-2 items-center">
          <div className="w-1 h-[35px] bg-primary-0 rounded-full" />
          <h2 className="text-[24px] md:text-[36px] leading-[1.2] font-bold text-secondary-light">{title}</h2>
        </div>

        <Link 
          to='/actors'
          className="cursor-pointer text-[16px] text-secondary-light font-nunito font-bold"
        >
          View all
        </Link>
      </div>

      <div className="flex justify-center items-center gap-2 pt-6">
        <button
          onClick={() => scroll('left')}
          className="text-primary-0 cursor-pointer
          w-12 h-12 flex flex-shrink-0 justify-center items-center
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
          className="flex justify-start items-start gap-2 md:gap-6 flex-nowrap overflow-x-hidden scroll-smooth"
        >
          {items.map(item => (
            <div className="relative z-10">
              <ActorCard key={item.id} actor={item} />
            </div>            
          ))}
        </div>

        <button
          onClick={() => scroll('right')}
          className="text-primary-0 cursor-pointer
          w-12 h-12 flex flex-shrink-0 justify-center items-center
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