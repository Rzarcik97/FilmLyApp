import { Link } from 'react-router-dom';
import logo from '../../../public/icons/logo.svg';

export const Logo = () => {
  return (
    <div className="relative flex items-center w-auto lg:w-[200px]">
      <Link to="/" className="relative flex items-center justify-center">

        <span className="text-secondary-light font-bold text-[24px] md:text-[48px] tracking-tight z-10">
          Filmly
        </span>

        <img
          src={logo}
          alt=""
          className="absolute w-[170px] md:w-[380px] h-[auto] max-w-none top-[-12px] md:top-[-28px] left-[-33px] md:left-[-76px] z-20 pointer-events-none opacity-90"
        />
      </Link>
    </div>
  )
}