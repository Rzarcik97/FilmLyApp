import { ChevronRight } from 'lucide-react';

export const WhatShouldIWatch = () => {
  return (
    <div className="px-12">
      <div className="flex justify-between items-center pb-8">
        <h2 className="text-[48px]">What should I watch?</h2>
        <div className="">
          <button className="cursor-pointer flex justify-center items-center gap-2 text-black font-bold text-8">
            <span>From your Watchlist</span>
            <div className="text-[#626262] cursor-pointer">
              <ChevronRight size={24} />
            </div>
          </button>
        </div>        
      </div>

      <div className="h-[119px] flex flex-col justify-center items-center">
        <p className="text-[32px]">
          Stop scrolling. Start watching.
        </p>
        <p className="text-[24px]">
          Join now to get personalized recommendations and find 
          your next movie instantly.
        </p>
      </div>
    </div>
  )
}