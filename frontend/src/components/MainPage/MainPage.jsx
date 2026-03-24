import play_btn from '../../../public/icons/play.png';
import mute from '../../../public/icons/mute.png';
import arr_right from '../../../public/icons/arrow-right.png';
import empty_img from '../../../public/icons/empty-img.png';
import './MainPage.scss';

export const MainPage = () => {
  return (
    <main className="main-page">
      <div className="main-page__top-bar">
        <button className="main-page__top-bar__btn">
          <img src={play_btn} alt="Play" className="main-page__top-bar__img" />
        </button>
        <button className="main-page__top-bar__btn">
          <img src={mute} alt="Mute" className="main-page__top-bar__img" />
        </button>
      </div>

      <div className="main-page__container">
        <div className="main-page__container__info">
          <h1 className="main-page__container__info__title">
            About us
          </h1>
          <p className="main-page__container__info__text">
            This is a platform  for discovering and choosing movies and TV shows
            that helps users quickly find what to watch, get personalized
            recommendations, and instantly see where the content is available.
          </p>
          <button className="main-page__container__info__btn">
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