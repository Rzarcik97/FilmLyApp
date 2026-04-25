import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import progress2 from '../../../public/sign_up/progress-2.png';
import empty_img from '../../../public/icons/empty-img.png';
import { Eye, EyeOff } from 'lucide-react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { createPasswordSchema } from '../../utils/validationSchemas';
import type { CreatePasswordFormData } from '../../utils/validationSchemas';
import { authService } from '../../api/authService';

export const CreatePassword = () => {
  const location = useLocation();
  const from = location.state?.from || '/';
  const navigate = useNavigate();

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const { email, username } = location.state || {};

  if (!email || !username) {
    setTimeout(() => navigate('/sign-up'), 0);
    return null;
  }

  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm<CreatePasswordFormData>({
    resolver: zodResolver(createPasswordSchema),
    mode: 'onChange',
    defaultValues: {
      password: '',
      confirmPassword: '',
    }
  });

  const onSubmit = async (data: CreatePasswordFormData) => {
    const finalData = {
      username: username,
      email: email,
      password: data.password,
      confirmPassword: data.confirmPassword,
      firstName: username || 'User',
      lastName: 'Filmly Member',
    };

    try {
      const result = await authService.register(finalData);
      console.log("Registered user ID:", result.userId);
      navigate(from, { replace: true });
    } catch (error: any) {
      if (error.response?.data?.errors) {
        console.table(error.response.data.errors);
      }
      console.error("Reason:", error.response?.data?.message);
    }
  }

  return (
    <div className="px-10 py-12 flex justify-center items-center">
      <div className="border border-primary-0 
          backdrop-blur-[2px]
          before:content-[''] before:absolute before:inset-0
          before:rounded-[32px] before:border before:border-primary-0/20
          before:pointer-events-none bg-signup-bg/70
          px-6 lg:px-12 py-10 w-full max-w-[660px] max-h-[770px] flex-shrink-0 rounded-[32px]"
      >
        <form
          onSubmit={handleSubmit(onSubmit)}
        >
          <div className="flex flex-col gap-4 lg:gap-7">
            <p className="text-signup-1 text-[20px] leading-[1.45] text-center font-semibold">Create a password</p>

            <div>
              <div className="flex flex-col gap-2 pb-4">
                <label className="text-gray-30 text-[16px] md:text-[20px] leading-[1.45] font-nunito">Password</label>
                <div className="relative">
                  <input
                    type={showPassword ? "text" : "password"}
                    className={`w-full bg-signup-input/91 border rounded-[8px] px-4 py-3 text-gray-80 transition-colors focus:outline-none
                              autofill:shadow-[inset_0_0_0_1000px_var(--color-gray-100)]
                              [-webkit-text-fill-color:var(--color-primary-0)]
                              autofill:text-fill-primary-0
                    ${errors.password ? 'border-system-error' : 'border-transparent focus:border-primary-0'}`}
                    {...register('password')}
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2"
                  >
                    {showPassword ? <Eye size={20} className="text-primary-0" /> : <EyeOff size={20} className="text-signup-1" />}
                  </button>
                </div>
                {errors.password && (
                  <p className="text-system-error text-xs mt-1 font-nunito animate-fade-in">{errors.password.message}</p>
                )}
              </div>

              <div className="flex flex-col gap-2">
                <label className="text-gray-30 text-[16px] md:text-[20px] leading-[1.45] font-nunito">Confirm Password</label>
                <div className="relative">
                  <input
                    type={showConfirmPassword ? "text" : "password"}
                    className={`w-full bg-signup-input /91 border rounded-[8px] px-4 py-3 text-gray-80 transition-colors focus:outline-none
                                autofill:shadow-[inset_0_0_0_1000px_var(--color-gray-100)]
                                [-webkit-text-fill-color:var(--color-primary-0)]
                                autofill:text-fill-primary-0
                    ${errors.confirmPassword ? 'border-system-error' : 'border-transparent focus:border-primary-0'}`}
                    {...register('confirmPassword')}
                  />
                  <button
                    type="button"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2"
                  >
                    {showConfirmPassword ? <Eye size={20} className="text-primary-0" /> : <EyeOff size={20} className="text-signup-1" />}
                  </button>
                </div>

                {errors.confirmPassword && (
                  <p className="text-system-error text-xs mt-1 font-nunito animate-fade-in">{errors.confirmPassword.message}</p>
                )}
              </div>
            </div>

            <div className="cursor-pointer w-full">
              <button
                type="submit"
                disabled={!isValid}
                className={`w-full h-12 md:h-[61px] rounded-[32px] border border-gray-30 text-[24px] md:text-[32px] font-bold transition-all duration-300
                ${!isValid
                    ? 'bg-gray-30 text-gray-50'
                    : 'bg-gray-100 text-primary-0 cursor-pointer hover:bg-gray-90 border border-primary-0'
                  }`}
              >
                Sign Up
              </button>
            </div>
          </div>          
        </form>
      </div>
    </div>
  )
}