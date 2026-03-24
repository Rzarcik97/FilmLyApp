import logo from '../../../public/icons/logo.png';
import reminder from '../../../public/icons/reminder.png';
import watchlist from '../../../public/icons/watchlist.png';
import sign_in from '../../../public/icons/sign-in.png';
import en from '../../../public/icons/en.png';
import { SearchBar } from '../SearchBar/SearchBar';

const NavItem = ({ icon, text, alt }) => (
  <div className="flex flex-col justify-center items-center cursor-pointer">
    <img src={icon} alt={alt} className="w-8 pb-[2px]" />
    <p className="m-0 text-[13px] leading-none">{text}</p>
  </div>
)

export const Header = () => {
  return (
    <header className="flex justify-between items-end bg-[#C0C0C0] px-12 pb-4 h-[112px]">
      <div className="relative">
        <img src={logo} alt="Filmly Logo" className="m-0 pt-[67px] w-[89px]" />
      </div>
      <div className="relative">
        <SearchBar />
      </div>
      <nav className="flex justify-center items-center gap-6">
        <NavItem icon={reminder} text="Reminder" alt="Reminder icon" />
        <NavItem icon={watchlist} text="Watchlist" alt="Watchlist icon" />
        <NavItem icon={sign_in} text="Sign in" alt="Sign-in icon" />
        <NavItem icon={en} text="EN" alt="Language icon" />
      </nav>
    </header>
  )
}