import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import progress2 from '../../../public/sign_up/progress-2.png';
import empty_img from '../../../public/icons/empty-img.png';
import { Eye, EyeClosed } from 'lucide-react';

export const CreatePassword = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const userEmail = location.state?.email || '';
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const isMinLength = password.length >= 8;
  const isMatch = password === confirmPassword && password !== '';

  const isButtonDisabled = !isMinLength || !isMatch;

  const handleFinish = () => {
    if (!isButtonDisabled) {
      // my future API call with smth like authService.register(email, password)
      navigate('/');
    }
  }

  return (
    <div className="bg-primary-background">
      <h1 className="text-signup-text text-[48px] font-bold pt-[43px] px-24 text-center pb-15">Skip the search. Just choose.</h1>

      <div className="px-6 lg:px-24 flex flex-col lg:flex-row gap-12 lg:gap-[111px] items-center justify-start min-h-screen pb-8">
        <div className="px-6 lg:px-12 py-10 w-full max-w-[609px] flex-shrink-0 flex flex-col gap-[50px] justify-center items-center bg-signup-primary-background rounded-[32px]">
          <p className="pb-8 lg:pb-[50px] text-password-text-1 text-[20px] lg:text-[24px] text-center font-bold">Create a password</p>

          <div className="w-full flex flex-col gap-[50px]">
            <div className="flex flex-col gap-2 relative">
              <label className="text-signup-text-3 text-[16px]">Password</label>
              <div className="relative">
                <input
                  type={showPassword ? "text" : "password"}
                  className="w-full bg-transparent border border-signup-border rounded-[8px] px-4 py-3 text-white focus:outline-none focus:border-white"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-signup-text-3 hover:text-white"
                >
                  {showPassword ? <EyeClosed size={20} /> : <Eye size={20} />}
                </button>
              </div>
              {password && !isMinLength && (
                <p className="text-red-500 text-xs mt-1">Minimum 8 characters required</p>
              )}
            </div>

            <div className="flex flex-col gap-2">
              <label className="text-signup-text-3 text-[16px]">Confirm Password</label>
              <div className="relative">
                <input
                  type={showConfirmPassword ? "text" : "password"}
                  className={`w-full bg-transparent border rounded-[8px] px-4 py-3 text-white focus:outline-none 
          ${confirmPassword && !isMatch ? 'border-red-500' : 'border-signup-border focus:border-white'}`}
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-signup-text-3 hover:text-white"
                >
                  {showConfirmPassword ? <EyeClosed size={20} /> : <Eye size={20} />}
                </button>
              </div>

              {confirmPassword && !isMatch && (
                <p className="text-red-500 text-xs mt-1">Passwords do not match</p>
              )}
            </div>
          </div>

          <div className="cursor-pointer w-full">
            <button
              disabled={isButtonDisabled}
              className={`w-full h-[61px] rounded-[32px] border-none text-[24px] font-bold transition-all duration-300
              ${isButtonDisabled
                  ? 'bg-signup-button-disabled text-signup-text-4'
                  : 'bg-white text-signup-primary-background cursor-pointer hover:bg-gray-200 shadow-lg'
                }`}
              onClick={handleFinish}
            >
              Sign Up
            </button>
          </div>


        </div>

        <div className="flex flex-col gap-[115px] justify-center items-center w-full">
          <img src={empty_img} alt="Sign Up Main Image" className="w-27 h-27" />
          <img src={progress2} alt="" className="max-w-[513px]" />
        </div>
      </div>
    </div>
  )
}