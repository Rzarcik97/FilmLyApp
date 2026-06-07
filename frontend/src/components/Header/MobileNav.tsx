import { Link, useNavigate } from 'react-router-dom';
import popcorn from '/icons/mobile/popcorn.svg';
import popcorn_focus from '/icons/mobile/popcorn_focus.svg';
import { Power, UserRound, Sun, Moon } from 'lucide-react';
import { useDispatch } from 'react-redux';
import { openAuthModal } from '../../store/uiSlice';
import { useTheme } from '../../context/ThemeContext';

interface MobileNavProps {
  onClose: () => void;
  isLoggedIn: boolean;
  onLogout: () => void;
}

export const MobileNav = ({ onClose, isLoggedIn, onLogout }: MobileNavProps) => {
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
    <nav
      className="flex flex-col gap-1 px-1 py-1 border border-gray-50 bg-gray-90 rounded-[45px] w-8 items-center"
      onClick={(e) => e.stopPropagation()}
    >
      <button
        onClick={handleWatchlistClick}
        className="group w-8 h-8 flex items-center justify-center"
      >
        <img
          src={popcorn}
          alt="Watchlist"
          className="w-6 h-6 block group-active:hidden"
        />
        <img
          src={popcorn_focus}
          alt="Watchlist"
          className="w-6 h-6 hidden group-active:block"
        />
      </button>

      <button
        onClick={toggleTheme}
        className="group w-8 h-8 flex items-center justify-center group"
      >
        {theme === 'dark' ? (
          <Sun className="text-gray-50 group-active:text-primary-0 transition-colors" size={24} />
        ) : (
            <Moon className="text-gray-50 group-active:text-primary-0 transition-colors" size={24} />
        )}
      </button>

      {isLoggedIn ? (
        <Power onClick={onLogout} size={24} className="text-gray-50 group-active:text-primary-0 transition-colors" />
      ) : (
        <Link to='/sign-up' onClick={onClose} className="group w-8 h-8 flex items-center justify-center group">
          <UserRound size={24} className="text-gray-50 group-active:text-primary-0 transition-colors" />
        </Link>
      )}
    </nav>
  );
};