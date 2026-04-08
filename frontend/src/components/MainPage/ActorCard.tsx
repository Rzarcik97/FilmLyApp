import type { Actor } from '../../types';
import empty_img from '../../../public/icons/empty-img.png';


export const ActorCard = ({ actor }: { actor: Actor }) => {
  const profileUrl = actor.profile_path
    ? `https://image.tmdb.org/t/p/w185${actor.profile_path}`
    : null;
  
  return (
    <div className="cursor-pointer">
      <div className="bg-gray-90 w-[200px] h-[200px] rounded-full flex-1 flex justify-center items-center overflow-hidden
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
      ">
        {profileUrl ? (
          <img
            src={profileUrl}
            alt={actor.name}
            className="w-full h-full object-cover"
          />
        ) : (
          <img
            src={empty_img}
            alt="No Image Available"
            className="w-27 h-27"
          />
        )}
      </div>
      <div className="h-18">
        <p className="text-white text-[16px] leading-[1.5] text-center font-bold font-nunito">{actor.name}</p>
      </div>
    </div>
  )
}