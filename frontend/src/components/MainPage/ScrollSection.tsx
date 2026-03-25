import type { Movie } from '../../types'
import { MovieCard } from './MovieCard';
import { ChevronLeft, ChevronRight } from 'lucide-react';

interface ScrollSectionProps {
  items: Movie[];
  title: string;
}

export const ScrollSection = ({ title, items }: ScrollSectionProps) => {
  return (
    <section className="py-10 px-12">
      <div className="flex justify-between items-center pb-2">
        <h2 className="text-[48px] fw-bold">{title}</h2>
        <button className="cursor-pointer text-[20px]">
          View all
        </button>
      </div>

      <div className="flex justify-center items-center gap-2">
        <button className="text-[#626262] cursor-pointer">
          <ChevronLeft size={24} />
        </button>
        <div className="flex justify-center items-center gap-4">
          {items.map(item => (
            <MovieCard key={item.id} movie={item} />
          ))}
        </div>
        <button className="text-[#626262] cursor-pointer">
          <ChevronRight size={24} />
        </button>
      </div>
    </section>
  )
}