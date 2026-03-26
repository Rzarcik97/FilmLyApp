import type { Actor } from '../../types'
import { ActorCard } from './ActorCard';
import { ChevronLeft, ChevronRight } from 'lucide-react';

interface ScrollActorsProps {
  items: Actor[];
  title: string;
}

export const ScrollActors = ({ title, items }: ScrollActorsProps) => {
  return (
    <section className="py-10 px-12 pb-[122px]">
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
            <ActorCard key={item.id} actor={item} />
          ))}
        </div>
        <button className="text-[#626262] cursor-pointer">
          <ChevronRight size={24} />
        </button>
      </div>
    </section>
  )
}