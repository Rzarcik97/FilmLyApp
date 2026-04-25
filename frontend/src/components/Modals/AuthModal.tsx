import { X } from 'lucide-react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { closeAuthModal } from '../../store/uiSlice';
import { type RootState } from '../../store';

export const AuthModal = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const isOpen = useSelector((state: RootState) => state.ui.isAuthModalOpen);

  if (!isOpen) return null;

  const handleClose = () => dispatch(closeAuthModal());

  const handleAuthNavigation = (path: string) => {
    const destination = location.pathname + location.search;
    navigate(path, { state: { from: destination } });
    handleClose();
  }

  return (
    <div className="fixed inset-0 z-[2000] flex items-center justify-center p-4 sm:p-6">
      <div
        className="absolute inset-0 bg-gray-100/90 backdrop-blur-md"
        onClick={handleClose}
      />

      <div className="relative w-full max-w-[90%] sm:max-w-md bg-gray-100 border border-gray-80 rounded-2xl p-6 sm:p-10 shadow-2xl text-center transform transition-all animate-in fade-in zoom-in duration-300 overflow-y-auto max-h-[90vh]">
        <button
          onClick={handleClose}
          className="absolute top-4 right-4 text-gray-50 hover:text-primary-0 transition-colors p-1"
        >
          <X size={24} />
        </button>

        <div className="mb-6 sm:mb-8 mt-4">
          <h2 className="text-xl sm:text-2xl font-bold text-gray-0 mb-3">Join the Club!</h2>
          <p className="text-gray-30 text-sm sm:text-[16px] leading-relaxed">
            This feature is only for authenticated users. Sign up or Log in to Filmly!
          </p>
        </div>

        <div className="flex flex-col gap-4">
          <button
            onClick={() => handleAuthNavigation('/sign-up')}
            className="cursor-pointer w-full py-3.5 sm:py-4 bg-primary-0 text-black font-bold rounded-xl hover:bg-primary-10 transition-all shadow-lg shadow-primary-0/20 active:scale-95"
          >
            Create Account
          </button>

          <button
            onClick={() => handleAuthNavigation('/login')}
            className="cursor-pointer w-full py-3.5 sm:py-4 bg-transparent text-primary-0 border border-primary-0 font-bold rounded-xl hover:bg-primary-0/5 transition-all active:scale-95"
          >
            Log In
          </button>
        </div>

        <button
          onClick={handleClose}
          className="cursor-pointer mt-6 sm:mt-8 text-gray-50 text-xs sm:text-sm hover:text-gray-30 transition-colors"
        >
          Maybe later
        </button>
      </div>
    </div>
  );
};