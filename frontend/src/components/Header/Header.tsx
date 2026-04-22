
import { useState } from 'react';
import search from '../../../public/icons/search.svg';
import { Menu } from 'lucide-react';
import { Logo } from './Logo';
import { NavBar } from './NavBar';
import { BackButton } from './BackButton';
import { useLocation } from 'react-router-dom';
import { SearchBar } from './SearchBar';

export const Header = ({ hasBackButton = false }) => {
  const [isFocused, setIsFocused] = useState(false);
  const location = useLocation();
  const isOverviewPage = /^\/(movies|series)\/\d+/.test(location.pathname);

  return (
    <>
      {isFocused && (
        <div
          className="fixed inset-0 z-100 transition-opacity duration-300"
          style={{ backgroundColor: 'rgba(21, 20, 18, 0.85)' }}
          onClick={() => setIsFocused(false)}
        />
      )}

      <header className="flex justify-between items-center
      absolute left-0 right-0 z-110 top-8 px-4 md:px-6 lg:mx-15 h-8 lg:h-[79px]
      bg-transparent lg:bg-gray-90/28 lg:backdrop-blur-[2px] lg:rounded-2xl lg:border lg:border-gray-90/10
      lg:before:content-[''] lg:before:absolute lg:before:inset-0 lg:before:pointer-events-none
      lg:before:rounded-2xl lg:before:border lg:before:border-gray-90/20
      lg:shadow-[0_20px_50px_rgba(0,0,0,0.8)]
    ">
        {isOverviewPage && <BackButton />}
        <Logo />

        <div className="hidden lg:block relative">
          <SearchBar onFocusChange={setIsFocused} isFocused={isFocused} />
        </div>
        
        <NavBar />

        <div className="flex lg:hidden items-center gap-2">
          <button className="w-8 h-8 flex items-center justify-center cursor-pointer rounded-full bg-gray-90 border border-gray-50 backdrop-blur-md">
            <img src={search} alt="Search" className="w-6 h-6" />
          </button>
          <button className="w-8 h-8 flex items-center justify-center cursor-pointer rounded-full bg-gray-90 border border-gray-50 backdrop-blur-md text-gray-50">
            <Menu size={24} />
          </button>
        </div>
      </header>
    </>
  )
}