import { ChevronLeft, Play, VolumeX, UserStar } from 'lucide-react';
import { useNavigate, useParams } from 'react-router-dom';
import empty_img from '../../../public/icons/empty-img.png';
import imdb from '../../../public/icons/imdb_black.png';

export const MainOverview = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const genres = ["Drama Mystery", "Thriller", "Crime", "Dark Crime"];

  return (
    <div className="bg-primary-background p-10 min-h-screen">
      <div className="flex justify-between items-center">
        <button
          className="flex justify-center items-center cursor-pointer"
          onClick={() => navigate(-1)}
        >
          <span className="text-whitish-text-color">
            <ChevronLeft size={32} />
          </span>
          <p className="text-[24px] text-whitish-text-color">Back</p>
        </button>

        <span className="text-white flex justify-center items-center gap-5">
          <span className="cursor-pointer">
            <Play size={24} />
          </span>
          <span className="cursor-pointer">
            <VolumeX size={24} />
          </span>
        </span>
      </div>

      <div className="flex justify-start gap-[136px]">
        <div className="pt-[290px] w-[401px] h-[243px]">
          <h1 className="text-[48px] font-500 text-center pb-4">Breaking Bad</h1>
          <div className="flex justify-evenly items-center pb-4">
            <div className="flex justify-center items-center gap-[9px]">
              <p className="text-[16px] text-black font-bold">7.6</p>
              <img src={imdb} alt="Imdb Rating" className="w-6 h-6" />
            </div>
            <div className="flex justify-center items-center gap-[9px]">
              <p className="text-[16px] text-black font-bold">85%</p>
              <UserStar size={22} />
            </div>
          </div>
          <div className="flex flex-wrap justify-center items-center gap-2 pb-[21px]">
            {genres.map((genre, index) => (
              <span
                key={index}
                className="px-[10px] py-[2.5px] pb-[5px] text-greyish-text-color text-[16px] text-opacity-80 border border-greyish-text-color rounded-[32px]"
              >
                {genre}
              </span>
            ))}
          </div>
          <p className="text-[16px] text-[#4A4A4A] text-center pb-4">2008 • 47m • TV-Ma • TV Series </p>
          <button className="h-11 bg-white border-none cursor-pointer w-full rounded-[30px]">
            <span className="text-black text-[16px] font-bold">
              Watch from $7.99
            </span>
          </button>
        </div>
        <img
          src={empty_img}
          alt="Main Item Picture"
          className="w-[246px] h-[246px]"
        />
      </div>
    </div>
  )
}