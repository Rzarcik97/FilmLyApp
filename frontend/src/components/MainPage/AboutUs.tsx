import { ChevronRight } from 'lucide-react';
import play from '../../../public/icons/play.png';
import mute from '../../../public/icons/mute.png';

export const AboutUs = () => {
  return (
    <main className="bg-gray-100 px-6 md:px-12 min-h-screen md:h-[672px] -mt-[111px] pt-[111px]
    bg-[url('../../../public/backgrounds/All_Of_Us_Strangers.png')]
    bg-cover bg-center bg-no-repeat
    flex flex-col relative
    ">
      <div
        className="absolute inset-0 z-0 lg:hidden"
        style={{
          background: `linear-gradient(
            to bottom, 
          rgba(17, 17, 16, 0) 0%,
          rgba(17, 17, 16, 0) 0%,
          rgba(17, 17, 16, 0.6) 70%,
          rgba(17, 17, 16, 1) 100%
          )`
        }}
      />

      <div className="flex-1 flex flex-col justify-end md:flex-row h-full pb-6 relative z-10">
        <div className="mt-auto lg:m-0 w-full max-w-[540px] lg:max-w-[432px] lg:mr-[85px] md:w-[432px] h-80 md:h-60 md:mr-[85px] shrink-0 order-2 md:order-1">
          <h1 className="lg:m-0 text-5xl lg:pt-[204px] text-gray-0 font-bold">
            About us
          </h1>
          <p className="m-0 py-[18px] text-lg md:text-xl leading-[1.45] tracking-[-0.011em] text-gray-0 font-medium">
            This is a platform  for discovering and choosing movies and TV shows
            that helps users quickly find what to watch, get personalized
            recommendations, and instantly see where the content is available.
          </p>
          <button className="bg-gray-0 border-none w-full lg:w-full h-11 rounded-[32px] text-[16px] leading-[1.5] tracking-[-0.011em] font-bold font-nunito cursor-pointer">
            Get started
          </button>
        </div>

        <div className="flex-1 flex flex-col justify-end lg:justify-between items-end md:pt-[296px] pb-10 lg:pb-8 min-h-full order-1 md:order-2">
          <button
            className="text-primary-0 cursor-pointer 
            w-12 h-12 hidden lg:flex justify-center items-center
            bg-gray-80/10 backdrop-blur-[2px]
            rounded-full border border-gray-80/10
            before:content-[''] before:absolute before:inset-0
            before:rounded-full before:border before:border-white/20
            ">
            <ChevronRight size={24} />
          </button>

          <div className="flex flex-col justify-center items-center lg:items-end">
            <p className="text-[16px] text-gray-50 md:text-gray-80 leading-[1.5]">All Of Us Strangers</p>
            <p className="text-[16px] text-gray-50 md:text-gray-80 leading-[1.5] pb-2">Drama  •  2023  •  1h 45m</p>
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
