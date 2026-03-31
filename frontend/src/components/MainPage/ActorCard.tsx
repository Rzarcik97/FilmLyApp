import type { Actor } from '../../types';
import empty_img from '../../../public/icons/empty-img.png';


export const ActorCard = ({ actor }: { actor: Actor }) => {
  return (
    <div className="cursor-pointer">
      <div className="bg-primary-background w-[200px] h-[200px] rounded-full flex-1 flex justify-center items-center">
        <img
          src={empty_img}
          alt="Item Main Image"
          className="w-27 h-27"
        />
      </div>
      <div className="p-2 h-18">
        <p className="pt-[13px] text-[#4D4D4D] text-[16px] text-center">{actor.name}</p>
      </div>
    </div>
  )
}