import { ChevronRight } from 'lucide-react';
import popcorn from '../../../public/icons/popcorn.png';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { openAuthModal } from '../../store/uiSlice';

export const WhatShouldIWatch = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isLoggedIn = !!localStorage.getItem('token');

  const handleWatchListAccess = () => {
    if (isLoggedIn) {
      navigate('/profile');
    } else {
      dispatch(openAuthModal());
    }
  };

  const handleAuthAction = () => {
    if (isLoggedIn) {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    } else {
      navigate('/sign-up');
    }
  };

  return (
    <div className="mx-12 mt-11 bg-gray-100 px-8 py-11 rounded-[51px] overflow-hidden
          bg-gray-80/10 backdrop-blur-[2px]
          border border-gray-80/10
          before:content-[''] before:absolute before:inset-0
          before:rounded-[51px] before:border before:border-gray-80/20
          shadow-[0_20px_50px_rgba(0,0,0,0.8)]
          flex flex-col gap-6 font-nunito
    ">
      <div className="flex flex-col md:flex-row justify-start md:justify-between items-start md:items-center">
        <div className="flex gap-4 items-center">
          <div className="w-1 h-[43px] bg-secondary-light rounded-full" />
          <h2 className="text-[24px] md:text-[36px] text-primary-0 leading-[1.2] font-bold">What should I watch?</h2>
        </div>
        <div className="">
          <button
            onClick={handleWatchListAccess}
            className="cursor-pointer flex justify-center items-center gap-2 text-black font-bold text-8 relative z-10"
          >
            <span className="text-secondary-light font-bold text-[16px] pl-6 md:pl-0">From your Watchlist</span>
            <div className="text-primary-0 cursor-pointer">
              <ChevronRight size={16} />
            </div>
          </button>
        </div>
      </div>

      <button
        onClick={handleWatchListAccess}
        className="flex justify-center items-center cursor-pointer relative z-10 pb-6 md:pb-0"
      >
        <img src={popcorn} alt="Go to watchlist" className="w-22 h-22 md:w-50 md:h-50" />
      </button>

      <div className="h-[119px] pb-8 flex flex-col justify-center items-center pb-6 md:pb-0">
        <p className="text-[24px] md:text-[32px] leading-[1.3] font-bold text-gray-0 text-center pb-6 md:pb-0">
          Stop scrolling. <br className="md:hidden" />Start watching.
        </p>
        <p className="text-[16px] md:text-[20px] leading-[1.45] text-gray-0 text-center">
          Join now to get personalized recommendations and find
          your next movie instantly.
        </p>
      </div>

      <div className="flex justify-center items-center">
        <button
          onClick={handleAuthAction}
          className="cursor-pointer border-none rounded-[32px] bg-primary-0 w-[522px] h-10 md:h-[56px] flex justify-center items-center relative z-10"
        >
          <span className="text-secondary-dark text-[20px] leading-[1.45] font-semibold">Sing in to FILMLY</span>
        </button>
      </div>

    </div>
  )
}