import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { closeLogoutModal } from '../../store/uiSlice';
import { clearWatchlist } from '../../store/watchlistSlice';
import { type RootState } from '../../store';

export const LogoutModal = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const isOpen = useSelector((state: RootState) => state.ui.isLogoutModalOpen);

  if (!isOpen) return null;

  const handleLogout = () => {
    localStorage.removeItem('token');
    dispatch(clearWatchlist());
    dispatch(closeLogoutModal());
    navigate('/');
  };

  const handleClose = () => dispatch(closeLogoutModal());

  return (
    <div className="fixed inset-0 z-[2000] flex items-center justify-center p-4">
      <div
        className="absolute inset-0 bg-gray-100/80 backdrop-blur-sm"
        onClick={handleClose}
      />

      <div className="relative w-full max-w-sm bg-gray-90 border border-gray-80 rounded-2xl p-8 shadow-2xl text-center">
        <h2 className="text-2xl font-bold text-white mb-4">Logging out?</h2>
        <p className="text-gray-30 mb-8 font-nunito">
          Are you sure you want to sign out of Filmly? You'll need to log in again to access your profile.
        </p>

        <div className="flex flex-col gap-3">
          <button
            onClick={handleLogout}
            className="cursor-pointer w-full py-3 bg-system-error text-white font-bold rounded-xl hover:bg-red-600 transition-all active:scale-95"
          >
            Yes, Log Me Out
          </button>

          <button
            onClick={handleClose}
            className="cursor-pointer w-full py-3 bg-transparent text-gray-30 font-semibold rounded-xl hover:text-white transition-all"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};