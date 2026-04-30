import watchlist from '../../../public/icons/header_popcorn.svg';
import { Link, useNavigate } from 'react-router-dom';
import { type LucideIcon, Power, UserRound, Earth, Bell } from 'lucide-react';
import { useDispatch } from 'react-redux';
import { openAuthModal } from '../../store/uiSlice';

interface NavItemProps {
  icon?: string;
  LucideIcon?: LucideIcon;
  alt?: string;
  onClick?: () => void;
  className?: string;
}

interface NavBarProps {
  isLoggedIn: boolean;
  onLogout: () => void;
}

const NavItem = ({ icon, LucideIcon, alt, onClick, className }: NavItemProps) => (
  <div
    onClick={onClick}
    className={`w-12 h-12 flex items-center justify-center cursor-pointer 
    rounded-full bg-gray-90 border border-gray-50/20 backdrop-blur-md 
    hover:bg-gray-80/40 transition-all duration-300 shadow-lg ${className}`}
  >
    {LucideIcon && <LucideIcon size={24} className="text-gray-50" />}

    {icon && <img src={icon} alt={alt} className="w-6 h-6 object-contain" />}
  </div>
);

export const NavBar = ({ isLoggedIn, onLogout }: NavBarProps) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleWatchlistClick = () => {
    if (isLoggedIn) {
      navigate('/profile');
    } else {
      dispatch(openAuthModal());
    }
  }

  return (
    <nav className="hidden lg:flex justify-center items-center gap-6">
      <NavItem
        icon={watchlist}
        alt="Watchlist icon"
        onClick={handleWatchlistClick}
      />
      {/* <NavItem LucideIcon={Bell} /> */}
      <NavItem LucideIcon={Earth} />
      {isLoggedIn ? (
        <NavItem LucideIcon={Power} onClick={onLogout} />
      ) : (
        <Link to="/sign-up">
          <NavItem LucideIcon={UserRound} />
        </Link>
      )}
    </nav>
  )
}