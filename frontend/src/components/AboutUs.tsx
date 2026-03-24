import { Play, VolumeX, ChevronRight, Image as ImageIcon } from 'lucide-react';
import empty_img from '../../public/icons/empty-img.png';

export const AboutUs = () => {
  return (
    <main className="bg-[#D9D9D9] px-12 h-[553px]" >
      <div className="pt-[26px] flex justify-end gap-[21px]">
        <button className="text-white bg-none border-none cursor-pointer w-6 h-6 flex justify-center items-center">
          <Play size={24} />
        </button>
        <button className="text-white bg-none border-none cursor-pointer w-6 h-6 flex justify-center items-center">
          <VolumeX size={24} />
        </button>
      </div>

      <div className="flex">
        <div className="w-[464px] h-60 mr-[85px] shrink-0">
          <h1 className="m-0 text-[48px] pt-[150px]">
            About us
          </h1>
          <p className="m-0 py-[18px] text-[16px] leading-[1.5] tracking-[-0.011em]">
            This is a platform  for discovering and choosing movies and TV shows
            that helps users quickly find what to watch, get personalized
            recommendations, and instantly see where the content is available.
          </p>
          <button className="bg-white border-none w-full h-11 rounded-[30px] text-[16px] leading-[1.5] tracking-[-0.011em] font-bold cursor-pointer">
            Get started
          </button>
        </div>

        <div className="flex-1 flex justify-between items-center pt-[69px]">
          <div className="w-[246px] h-[246px]">
            <img src={empty_img} alt="Main Image" />
          </div>

          <button className="text-white cursor-pointer">
            <ChevronRight size={64} />
          </button>
        </div>
      </div>
    </main >
  )
}
