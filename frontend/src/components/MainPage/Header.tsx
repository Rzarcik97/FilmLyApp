import logo from '../../../public/icons/logo.png';
import { Bell, Popcorn, UserRound, Earth } from 'lucide-react';
import { SearchBar } from './SearchBar';
import { Link } from 'react-router-dom';

interface NavItemsProps {
  icon: React.ReactNode;
  text: string;
  alt: string;
}

const NavItem = ({ icon, text, alt }: NavItemsProps) => (
  <div className="flex flex-col justify-center items-center cursor-pointer">
    <div className="pb-[2px]">
      {icon}
    </div>
    <p className="m-0 text-[13px] leading-none">{text}</p>
  </div>
)

export const Header = () => {
  return (
    <header className="flex justify-between items-end bg-[#C0C0C0] px-12 pb-4 h-[112px]">
      <div className="relative">
        <Link to="/">
          <img src={logo} alt="Filmly Logo" className="m-0 pt-[67px] w-[89px]" />
        </Link>
      </div>
      <div className="relative">
        <SearchBar />
      </div>
      <nav className="flex justify-center items-center gap-6">
        <NavItem icon={<Bell size={32} />} text="Reminder" alt="Reminder icon" />
        <NavItem icon={<Popcorn size={32} />} text="Watchlist" alt="Watchlist icon" />
        <NavItem icon={<UserRound size={32} />} text="Sign in" alt="Sign-in icon" />
        <NavItem icon={<Earth size={32} />} text="EN" alt="Language icon" />
      </nav>
    </header>
  )
}