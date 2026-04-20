import logo from '../../../public/icons/logo.svg';
import watchlist from '../../../public/icons/watchlist.png';
import reminder from '../../../public/icons/reminder.png';
import profile from '../../../public/icons/profile.png';
import lang from '../../../public/icons/lang.png';
import { SearchBar } from './SearchBar';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import search from '../../../public/icons/search.svg';
import { Menu } from 'lucide-react';

interface NavItemsProps {
  icon: string;
  alt: string;
}

const NavItem = ({ icon, alt }: NavItemsProps) => (
  <div className="flex flex-col justify-center items-center cursor-pointer">
    <div className="pb-[2px]">
      <img src={icon as string} alt={alt} className="w-12 h-12 object-contain" />
    </div>
  </div>
)

export const Header = () => {
  const [isFocused, setIsFocused] = useState(false);

  return (
    <>
      {isFocused && (
        <div
          className="fixed inset-0 z-[40] transition-opacity duration-300"
          style={{ backgroundColor: 'rgba(21, 20, 18, 0.85)' }}
          onClick={() => setIsFocused(false)}
        />
      )}

      <header className="flex justify-between items-center
      absolute z-60 top-8 pl-6 lg:mx-15 h-8 lg:h-[79px]
      bg-transparent lg:bg-gray-90/28 lg:backdrop-blur-[2px] lg:rounded-2xl lg:border lg:border-gray-90/10
      relative z-0
      lg:before:content-[''] lg:before:absolute lg:before:inset-0 lg:before:pointer-events-none
      lg:before:rounded-2xl lg:before:border lg:before:border-gray-90/20
      lg:shadow-[0_20px_50px_rgba(0,0,0,0.8)]
    ">
        <div className="relative flex items-center lg:pl-10 w-auto lg:w-[200px]">
          <Link to="/" className="relative flex items-center justify-center">

            <span className="text-secondary-light font-bold text-[18px] md:text-[48px] tracking-tight z-10">
              Filmly
            </span>

            <img
              src={logo}
              alt=""
              className="absolute w-[120px] md:w-[380px] h-[auto] max-w-none top-[-8px] md:top-[-28px] left-[-22px] md:left-[-76px] z-20 pointer-events-none opacity-90"
            />
          </Link>
        </div>

        <div className="hidden lg:block relative">
          <SearchBar onFocusChange={setIsFocused} isFocused={isFocused}/>
        </div>
        <nav className="hidden lg:flex justify-center items-center gap-6 pr-6">
          <NavItem icon={watchlist} alt="Watchlist icon" />
          <NavItem icon={reminder} alt="Reminder icon" />
          <NavItem icon={lang} alt="Language icon" />
          <Link to="/sign-up" className="">
            <NavItem icon={profile} alt="Sign-in icon" />
          </Link>
        </nav>

        <div className="flex lg:hidden items-center gap-2 pr-6">
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