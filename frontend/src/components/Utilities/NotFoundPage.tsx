import { Link } from 'react-router-dom';
import missing from '/backgrounds/missing.png';

export const NotFoundPage = () => {
  return (
    <div className="relative w-full min-h-screen flex flex-col gap-8 items-center justify-center overflow-hidden bg-gray-100 pt-40">
      <img src={missing} alt="" className="w-100 md:w-[562px] md:h-[192px]"/>
      <p className="text-[16px] md:text-[20px] text-gray-0 font-semibold leading-[1.45]">This page didn't make the final cut.</p>
      <button className="w-60 md:w-[303px] md:h-[66px] bg-primary-0 border-none cursor-pointer rounded-[32px]">
        <Link to='/discover/movies' className="text-black text-[20px] md:text-[24px] font-nunito font-bold">
          Back to Movies
        </Link>
      </button>
    </div>
  )
}