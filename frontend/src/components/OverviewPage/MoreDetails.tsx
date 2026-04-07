import { ThumbsUp, ThumbsDown, Check } from 'lucide-react';
import watchlist from '../../../public/icons/watchlist_popcorn.png';
import thumbsUp from '../../../public/icons/thumbUp.png';
import thumbsUpPrimary from '../../../public/icons/thumbUp-primary.png';
import thumbsDown from '../../../public/icons/thumbDown.png';

export const MoreDetails = () => {
  return (
    <div className="px-12 pt-8 bg-gray-100">
      <div className="flex justify-between items-center pb-[42px]">
        <div className="flex justify-center items-center gap-6 text-[16px] font-nunito font-semibold leading-[1]">
          <button className="text-black bg-primary-0 cursor-pointer w-51 h-14 flex justify-center items-center gap-2 border-1 border-primary-10 rounded-[9px]">
            <img src={watchlist} alt="Add to watchlist" className="w-6 h-6" />
            <span className="text-black text-[13px]">Watchlist</span>
          </button>
          <button className="text-primary-0 bg-secondary-dark cursor-pointer w-51 h-14 flex justify-center items-center gap-2 border-1 border-gray-80 rounded-[9px]">
            <Check size={12} />
            <span className="text-gray-30 text-[13px]">Seen it</span>
          </button>
        </div>

        <div className="flex justify-center items-center gap-6">
          <button className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
            <img src={thumbsUpPrimary} alt="Likes" className="w-6 h-6 object-cover" />
            <span className="text-white text-[12px] font-semibold font-nunito">82k</span>
          </button>
          <button className="cursor-pointer w-16 h-16 flex justify-center items-center gap-1
          bg-gray-80/10 backdrop-blur-[2px]
          rounded-full border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-full before:border before:border-gray-80/20
          ">
            <div className="w-6 h-6 flex justify-center items-center">
              <img src={thumbsDown} alt="Dislikes"/>
            </div>
            <span className="text-white text-[12px] font-semibold font-nunito">2.8k</span>
          </button>
        </div>
      </div>

      <div className="py-6 border-y border-primary-background">
        <h1 className="text-[32px] text-black font-bold pb-8">More Details</h1>
        <p className="text-[16px] text-black font-bold pb-8">A chemistry teacher diagnosed with inoperable lung cancer
          turns to manufacturing and selling methamphetamine with a former student
          to secure his family's future.</p>
        <div className="flex justify-start items-center gap-1 text-[16px] font-bold pb-8">
          <p className="text-foreground-light-2">CREATOR:</p>
          <span className="text-black">Vince Gilligan</span>
        </div>
        <div className="flex justify-start items-center gap-1 text-[16px] font-bold">
          <p className="text-foreground-light-2">PRODUCTION COUNTRY:</p>
          <span className="text-black">United States</span>
        </div>
      </div>
    </div>
  )
}