import type { Actor } from '../../types';
import empty_img from '../../../public/icons/empty-img.png';
import { ThumbsUp, ThumbsDown } from 'lucide-react';


export const ActorCard = ({ actor }: { actor: Actor }) => {
  return (
    <div>
      <div className="bg-primary-background w-[200px] h-[200px] rounded-full flex-1 flex justify-center items-center">
        <img
          src={empty_img}
          alt="Item Main Image"
          className="w-27 h-27"
        />
      </div>
      <div className="p-2 h-18">
        <div className="px-[33px] flex justify-between items-center">
          <span className="cursor-pointer">
            <ThumbsUp size={24} />
          </span>
          <span className="cursor-pointer">
            <ThumbsDown size={24} fill='currentColor' />
          </span>
        </div>
        <p className="pt-[13px] text-[#4D4D4D] text-[16px] text-center">{actor.name}</p>
      </div>
    </div>
  )
}