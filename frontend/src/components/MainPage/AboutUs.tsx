import { ChevronRight } from 'lucide-react';
import play from '../../../public/icons/play.png';
import mute from '../../../public/icons/mute.png';

export const AboutUs = () => {
  return (
    <main className="bg-gray-100 px-12 h-[672px] -mt-[111px] pt-[111px] flow-root
    bg-[url('../../../public/backgrounds/All_Of_Us_Strangers.png')]
    bg-cover bg-center bg-no-repeat
    ">
      <div className="flex h-full">
        <div className="w-[432px] h-60 mr-[85px] shrink-0">
          <h1 className="m-0 text-[48px] pt-[204px] text-gray-0 font-bold">
            About us
          </h1>
          <p className="m-0 py-[18px] text-[20px] leading-[1.45] tracking-[-0.011em] text-gray-0 font-medium">
            This is a platform  for discovering and choosing movies and TV shows
            that helps users quickly find what to watch, get personalized
            recommendations, and instantly see where the content is available.
          </p>
          <button className="bg-gray-0 border-none w-full h-11 rounded-[32px] text-[16px] leading-[1.5] tracking-[-0.011em] font-bold font-nunito cursor-pointer">
            Get started
          </button>
        </div>

        <div className="flex-1 flex flex-col justify-between items-end pt-[296px] pb-8 min-h-full">
          <button
            className="text-primary-0 cursor-pointer 
            w-12 h-12 flex justify-center items-center
            bg-gray-80/10 backdrop-blur-[2px]
            rounded-full border border-gray-80/10
            before:content-[''] before:absolute before:inset-0
            before:rounded-full before:border before:border-white/20
            ">
            <ChevronRight size={24} />
          </button>

          <div className="flex flex-col justify-center items-center">
            <p className="text-[16px] text-gray-80 leading-[1.5]">All Of Us Strangers</p>
            <p className="text-[16px] text-gray-80 leading-[1.5] pb-2">Drama  •  2023  •  1h 45m</p>
            <div className="flex gap-[30px] justify-center items-center">
              <img src={play} alt="Play button" className="cursor-pointer w-8 h-8" />
              <img src={mute} alt="Mute button" className="cursor-pointer w-8 h-8" />
            </div>
          </div>
        </div>
      </div>
    </main >
  )
}
