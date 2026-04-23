import { Link } from 'react-router-dom';
import popcorn from '../../../public/icons/mobile/popcorn.svg';
import popcorn_focus from '../../../public/icons/mobile/popcorn_focus.svg';
import { Bell, Earth, UserRound } from 'lucide-react';

interface MobileNavProps {
  onClose: () => void;
}

export const MobileNav = ({ onClose }: MobileNavProps) => {
  return (
    <nav
      className="flex flex-col gap-1 px-1 py-1 border border-gray-50 bg-gray-90 rounded-[45px] w-8 items-center"
      onClick={(e) => e.stopPropagation()}
    >
      <Link to='/watchlist' onClick={onClose} className="group w-8 h-8 flex items-center justify-center">
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
      </Link>

      <Link to='/' onClick={onClose} className="group w-8 h-8 flex items-center justify-center">
        <Bell size={24} className="text-gray-50 group-active:text-primary-0 transition-colors" />
      </Link>

      <Link to='/' onClick={onClose} className="group w-8 h-8 flex items-center justify-center group">
        <Earth size={24} className="text-gray-50 group-active:text-primary-0 transition-colors" />
      </Link>

      <Link to='/sign-up' onClick={onClose} className="group w-8 h-8 flex items-center justify-center group">
        <UserRound size={24} className="text-gray-50 group-active:text-primary-0 transition-colors" />
      </Link>
    </nav>
  );
};