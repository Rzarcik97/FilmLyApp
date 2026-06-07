import { useLocation, useNavigate } from 'react-router-dom';
import { ChevronLeft } from 'lucide-react';

export const BackButton = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const navigateBack = () => {
    if (location.state?.from) {
      navigate(location.state.from);
    } else {
      window.location.href = "/";
    }
  };

  return (
    <button
      className="w-8 h-8 md:hidden flex items-center justify-center cursor-pointer rounded-full bg-gray-90 border border-gray-50 backdrop-blur-md text-gray-50"
      onClick={(e) => {
        e.preventDefault();
        navigateBack();
      }}
    >
      <ChevronLeft size={24} />
    </button>
  )
}