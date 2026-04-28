import thumbsUp from '../../../public/icons/like.svg';
import thumbsUpPrimary from '../../../public/icons/thumbUp-primary.png';
import thumbsDown from '../../../public/icons/dislike.svg';

export const ButtonsLikeDislike = () => {
  return (
    <div className="hidden md:flex justify-center items-center gap-6">
      <button className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
        <img src={thumbsUp} alt="Likes" className="w-6 h-6 object-cover" />
        <span className="text-white text-[12px] font-semibold font-nunito">82k</span>
      </button>
      <button className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
        <img src={thumbsDown} alt="Dislikes" className="w-6 h-6 object-cover" />
        <span className="text-white text-[12px] font-semibold font-nunito">2.8k</span>
      </button>
    </div>
  )
}