import { Link } from 'react-router-dom';
import telegram from '../../../public/icons/telegram.png';
import youtube from '../../../public/icons/youtube.png';
import fb from '../../../public/icons/facebook.png';

export const Footer = () => {
  const baseColor = 'rgba(17, 17, 16, 1)';

  return (
    <footer className="relative w-full md:h-[700px] bg-gray-100 overflow-hidden">
      <div
        className="absolute inset-0 z-0 bg-cover md:bg-center bg-no-repeat brightness-[0.4] saturate-[0.5]"
        style={{ backgroundImage: "url('/backgrounds/footer_bg_img.webp')" }}
      />

      <div
        className="absolute inset-0 z-10"
        style={{
          background: `linear-gradient(
            to bottom, 
            ${baseColor} 0%, 
            ${baseColor} 12%,
            rgba(17, 17, 16, 0.6) 30%, 
            rgba(17, 17, 16, 0.2) 60%, 
            rgba(17, 17, 16, 0.8) 100%
          )`
        }}
      />

      <div className="absolute top-0 left-0 right-0 h-64 backdrop-blur-2xl z-20 [mask-image:linear-gradient(to_bottom,black_20%,transparent_100%)]" />

      <div className="relative z-20 h-[700px] md:h-full flex items-end">
        <div className="
                w-full 
                bg-gray-90/40 
                backdrop-blur-xl 
                rounded-t-[60px] 
                border-t border-x border-gray-80/10
                flex justify-between
        ">
          <div className="flex flex-col gap-4 md:flex-row pt-12 md:pr-30 pb-8 md:pb-[105px] pl-12 w-full justify-between items-start">
            <div className="flex flex-col gap-1.5">
              <Link to='/' className="h-[66px] flex flex-col items-start justify-center">
                <span className="text-[48px] text-secondary-light font-bold">Filmly</span>
              </Link>
              <p className="text-gray-80 text-[20px] leading-[1.45]">@ 2026 All rights  reserved</p>
              <div className="flex gap-2">
                <img src={telegram} alt="Telegram Icon" className="w-6 h-6" />
                <img src={youtube} alt="Youtube Icon" className="w-6 h-6" />
                <img src={fb} alt="Facebook Icon" className="w-6 h-6" />
              </div>
            </div>

            <div className="flex flex-col gap-[5px] text-[20px] leading-[1.45] w-51 font-nunito">
              <p className="text-primary-20">Navigation</p>
              <Link to="/watchlist">
                <p className="text-gray-70">Watchlist</p>
              </Link>
              <Link to="/reminder">
                <p className="text-gray-70">Reminder</p>
              </Link>
              <Link to="/sign-in">
                <p className="text-gray-70">Sign in</p>
              </Link>
              <Link to="/language">
                <p className="text-gray-70">Language</p>
              </Link>
            </div>

            <div className="flex flex-col gap-[5px] text-[20px] leading-[1.45] w-61 font-nunito">
              <p className="text-primary-20">Information</p>
              <p className="text-gray-70">Privacy Policy</p>
              <p className="text-gray-70">FAQ</p>
              <p className="text-gray-70">Cookie Policy</p>
              <Link to="/">
                <p className="text-gray-70">About us</p>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  )
}