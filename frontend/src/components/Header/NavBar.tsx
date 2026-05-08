import watchlist from '../../../public/icons/header_popcorn.svg';
import { Link, useNavigate } from 'react-router-dom';
import { type LucideIcon, Power, Sun, Moon } from 'lucide-react';
import { useDispatch } from 'react-redux';
import { openAuthModal } from '../../store/uiSlice';
import { useTheme } from '../../context/ThemeContext';

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

  const { theme, toggleTheme } = useTheme();

  return (
    <nav className="hidden lg:flex justify-center items-center gap-6">
      <NavItem
        icon={watchlist}
        alt="Watchlist icon"
        onClick={handleWatchlistClick}
      />
      <button
        onClick={toggleTheme}
        className="w-12 h-12 flex items-center justify-center cursor-pointer 
                  rounded-full bg-gray-90 border border-gray-50/20 backdrop-blur-md 
                  hover:bg-gray-80/40 transition-all duration-300 shadow-lg"
      >
        {theme === 'dark' ? (
          <Sun className="text-gray-50" size={24} />
        ) : (
            <Moon className="text-gray-50" size={24} />
        )}
      </button>
      {isLoggedIn ? (
        <NavItem LucideIcon={Power} onClick={onLogout} />
      ) : (
        <Link to="/sign-up">
          <div
            className="w-22 h-12 flex items-center justify-center cursor-pointer 
                        rounded-full bg-gray-90 border border-gray-50/20 backdrop-blur-md 
                        hover:bg-gray-80/40 transition-all duration-300 shadow-lg"
          >
            <p className="font-bold font-nunito text-[16px] leading-[1.35] text-primary-0">Sign Up</p>
          </div>
        </Link>
      )}
    </nav>
  )
}