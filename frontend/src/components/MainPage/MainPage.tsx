import play_btn from '../../../public/icons/play.png';
import mute from '../../../public/icons/mute.png';
import arr_right from '../../../public/icons/arrow-right.png';
import empty_img from '../../../public/icons/empty-img.png';
// import './MainPage.scss';

export const MainPage = () => {
  return (
    <main className="bg-[#D9D9D9] px-12">
      <div className="pt-[26px] flex justify-end gap-[21px]">
        <button className="bg-none border-none cursor-pointer w-6 h-6 flex justify-center items-center">
          <img src={play_btn} alt="Play" className="w-24" />
        </button>
        <button className="bg-none border-none cursor-pointer w-6 h-6 flex justify-center items-center">
          <img src={mute} alt="Mute" className="w-24" />
        </button>
      </div>

      <div className="flex">
        <div className="w-[464px] h-60">
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

        <div className="main-page__container__img">
          <img src={empty_img} alt="Main Image" />
        </div>

        <button className="main-page__container__btn">
          <img src={arr_right} alt="Next" className="main-page__container__btn__img" />
        </button>
      </div>
    </main>
  )
}