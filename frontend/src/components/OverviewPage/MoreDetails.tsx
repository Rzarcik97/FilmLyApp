import { Popcorn, ThumbsUp, ThumbsDown, Check } from 'lucide-react';

export const MoreDetails = () => {
  return (
    <div className="px-12 pt-8 bg-light-background">
      <div className="flex justify-between items-center pb-[42px]">
        <div className="flex justify-center items-center gap-6">
          <button className="text-black bg-white cursor-pointer w-38 h-14 flex justify-center items-center gap-0.5">
            <Popcorn size={24} />
            <span className="text-black text-[13px]">Watchlist</span>
          </button>
          <button className="text-foreground-light bg-dark-button cursor-pointer w-38 h-14 flex justify-center items-center gap-0.5">
            <Check size={24} />
            <span className="text-foreground-light text-[13px]">Seen it</span>
          </button>
        </div>

        <div className="flex justify-center items-center gap-6">
          <button className="cursor-pointer border border-light-border rounded-full w-16 h-16 text-black text-[13px] flex justify-center items-center gap-1">
            <ThumbsUp size={20} />
            <span>82k</span>
          </button>
          <button className="cursor-pointer border border-light-border rounded-full w-16 h-16 text-black text-[13px] flex justify-center items-center gap-1">
            <ThumbsDown size={20} />
            <span>2.8k</span>
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