import { Link } from 'react-router-dom';
import logo from '/icons/logo_dark.png';
import logo_light from '/icons/Logo.png';
import { useTheme } from '../../context/ThemeContext';

export const Logo = () => {
  const { theme } = useTheme();

  return (
    <div className="relative flex items-center w-auto lg:w-[200px]">
      <Link to="/" className="relative flex items-center justify-center">
        <img
          src={theme === 'light' ? logo_light : logo}
          alt="Filmly Logo"
          className="w-14 md:w-20 h-[auto] max-w-none z-20 pointer-events-none"
        />
      </Link>
    </div>
  )
}