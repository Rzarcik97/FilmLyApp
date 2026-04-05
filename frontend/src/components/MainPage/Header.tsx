import logo from '../../../public/icons/logo.png';
import watchlist from '../../../public/icons/watchlist.png';
import reminder from '../../../public/icons/reminder.png';
import profile from '../../../public/icons/profile.png';
import lang from '../../../public/icons/lang.png';
import { SearchBar } from './SearchBar';
import { Link } from 'react-router-dom';

interface NavItemsProps {
  icon: string;
  alt: string;
}

const NavItem = ({ icon, alt }: NavItemsProps) => (
  <div className="flex flex-col justify-center items-center cursor-pointer">
    <div className="pb-[2px]">
      <img src={icon as string} alt={alt} className="w-12 h-12 object-contain" />
    </div>
    {/* <p className="m-0 text-[13px] leading-none">{text}</p> */}
  </div>
)

export const Header = () => {
  return (
    <header className="flex justify-between items-center
      sticky top-8 z-50 mx-15 h-[79px]
      bg-white/10 backdrop-blur-[2px] rounded-2xl border border-gray-90/10
      relative z-0
      before:content-[''] before:absolute before:inset-0 before:pointer-events-none
      before:rounded-2xl before:border before:border-gray-90/20
    ">
      <div className="relative">
        <Link to="/">
          <img src={logo} alt="Filmly Logo" className="m-0 pl-[73px] h-[29px]" />
        </Link>
      </div>
      <div className="relative">
        <SearchBar />
      </div>
      <nav className="flex justify-center items-center gap-6">
        <NavItem icon={watchlist} alt="Watchlist icon" />
        <NavItem icon={reminder} alt="Reminder icon" />
        <NavItem icon={lang} alt="Language icon" />
        <Link to="/sign-up" className="">
          <NavItem icon={profile} alt="Sign-in icon" />
        </Link>
      </nav>
    </header>
  )
}