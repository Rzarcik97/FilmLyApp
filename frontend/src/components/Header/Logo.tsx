import { Link } from 'react-router-dom';
import logo from '../../../public/icons/logo.svg';

export const Logo = () => {
  return (
    <div className="relative flex items-center w-auto lg:w-[200px]">
      <Link to="/" className="relative flex items-center justify-center">

        {/* <span className="text-secondary-light font-bold text-[24px] md:text-[48px] tracking-tight z-10">
          Filmly
        </span> */}

        <img
          src={logo}
          alt=""
          className="w-14 md:w-20 h-[auto] max-w-none z-20 pointer-events-none"
        />
      </Link>
    </div>
  )
}