import logo from '../../../public/icons/logo.png';
import reminder from '../../../public/icons/reminder.png';
import watchlist from '../../../public/icons/watchlist.png';
import sign_in from '../../../public/icons/sign-in.png';
import en from '../../../public/icons/en.png';
import { SearchBar } from '../SearchBar/SearchBar';
import './Header.scss';

export const Header = () => {
  return (
    <header className="header">
      <div className="header__logo">
        <img src={logo} alt="Filmly Logo" className="header__logo__img" />
      </div>
      <div className="header__search-bar">
        <SearchBar />
      </div>
      <nav className="header__nav">
        <div className="header__nav__item">
          <img src={reminder} alt="Reminder" className="header__nav__item__img" />
          <p className="header__nav__item__text">Reminder</p>
        </div>
        <div className="header__nav__item">
          <img src={watchlist} alt="Watchlist" className="header__nav__item__img" />
          <p className="header__nav__item__text">Watchlist</p>
        </div>
        <div className="header__nav__item">
          <img src={sign_in} alt="Sign-in" className="header__nav__item__img" />
          <p className="header__nav__item__text">Sign in</p>
        </div>
        <div className="header__nav__item">
          <img src={en} alt="Language_EN" className="header__nav__item__img" />
          <p className="header__nav__item__text">EN</p>
        </div>
      </nav>
    </header>
  )
}