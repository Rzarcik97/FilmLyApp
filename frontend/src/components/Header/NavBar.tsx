import watchlist from '../../../public/icons/watchlist.png';
import reminder from '../../../public/icons/reminder.png';
import profile from '../../../public/icons/profile.png';
import lang from '../../../public/icons/lang.png';
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
  </div>
)

export const NavBar = () => {
  return (
    <nav className="hidden lg:flex justify-center items-center gap-6">
      <NavItem icon={watchlist} alt="Watchlist icon" />
      <NavItem icon={reminder} alt="Reminder icon" />
      <NavItem icon={lang} alt="Language icon" />
      <Link to="/sign-up" className="">
        <NavItem icon={profile} alt="Sign-in icon" />
      </Link>
    </nav>
  )
}