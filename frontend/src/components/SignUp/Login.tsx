import google_btn from '../../../public/icons/google.svg';
import apple_btn from '../../../public/icons/apple.svg';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { loginSchema } from '../../utils/validationSchemas';
import type { LoginFormData } from '../../utils/validationSchemas';
import { useState } from 'react';
import { Eye, EyeClosed } from 'lucide-react';

export const Login = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    mode: 'onChange',
    defaultValues: {
      email: '',
      password: ''
    }
  });

  const onSubmit = (data: LoginFormData) => {
    navigate('/', { state: { email: data.email, password: data.password } });
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
            <p className="text-signup-1 text-[20px] leading-[1.45] text-center font-semibold">Log in to dive into movies</p>

            <div className="flex gap-10 justify-center items-center">
              <img src={google_btn} alt="Log in with Google" className="w-10 h-10 md:w-20 md:h-20 cursor-pointer" />
              <img src={apple_btn} alt="Log in with Apple" className="w-10 h-10 md:w-20 md:h-20 cursor-pointer" />
            </div>

            <div className="flex items-center w-full my-6 gap-4">
              <div className="flex-1 h-[1px] bg-gray-70" />

              <span className="text-signup-2 text-[20px] leading-[1.45] font-semibold">or</span>

              <div className="flex-1 h-[1px] bg-gray-70"></div>
            </div>

            <div>
              <div className="flex flex-col gap-2 pb-4">
                <label htmlFor="email" className="text-gray-30 text-[16px] md:text-[20px] leading-[1.45] font-nunito">
                  E-mail
                </label>
                <input
                  id="email"
                  type="email"
                  placeholder="example@email.com"
                  className={`w-full bg-signup-input/91 border rounded-[8px] px-4 py-3 text-gray-80 transition-colors focus:outline-none
                              autofill:shadow-[inset_0_0_0_1000px_var(--color-gray-100)]
                              [-webkit-text-fill-color:var(--color-primary-0)]
                              autofill:text-fill-primary-0
                              placeholder:[-webkit-text-fill-color:var(--color-gray-80)]
                  ${errors.email ? 'border-system-error' : 'border-transparent focus:border-primary-0'}`}
                  {...register('email')}
                />

                {errors.email && (
                  <p className="text-system-error text-xs mt-1 font-nunito animate-fade-in">
                    Please enter a valid email address (e.g., name@domain.com)
                  </p>
                )}
              </div>

              <div className="flex flex-col gap-2 pb-4">
                <label htmlFor="password" className="text-gray-30 text-[16px] md:text-[20px] leading-[1.45] font-nunito">
                  Password
                </label>
                <div className="relative">
                  <input
                    id="password"
                    placeholder='********'
                    type={showPassword ? "text" : "password"}
                    className={`w-full bg-signup-input/91 border rounded-[8px] px-4 py-3 text-gray-80 transition-colors focus:outline-none
                              autofill:shadow-[inset_0_0_0_1000px_var(--color-gray-100)]
                              [-webkit-text-fill-color:var(--color-primary-0)]
                              autofill:text-fill-primary-0
                              placeholder:[-webkit-text-fill-color:var(--color-gray-80)]
                  ${errors.password ? 'border-system-error' : 'border-transparent focus:border-primary-0'}`}
                    {...register('password')}
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2"
                  >
                    {showPassword ? <EyeClosed size={20} className="text-signup-1" /> : <Eye size={20} className="text-signup-1" />}
                  </button>
                </div>
                {errors.password && (
                  <p className="text-system-error text-xs mt-1 font-nunito animate-fade-in">{errors.password.message}</p>
                )}
              </div>
            </div>

            <div className="">
              <button
                type="submit"
                disabled={!isValid}
                className={`w-full h-12 md:h-[61px] rounded-[32px] border border-gray-30 text-[24px] md:text-[32px] font-bold transition-all duration-300
              ${!isValid
                    ? 'bg-gray-30 text-gray-50'
                    : 'bg-gray-100 text-primary-0 cursor-pointer hover:bg-gray-90 border border-primary-0'
                  }`}
              >
                Log In
              </button>
            </div>

            <div className="flex gap-[10px] justify-center items-center text-[12px] md:text-[16px]">
              <span className="text-signup-1">Don't have an account?</span>
              <button 
                type="button"
                onClick={() => navigate('/sign-up')}
                className="cursor-pointer underline text-white"
              >
                <span>Sign Up</span>
              </button>
            </div>
          </div>          
        </form>
      </div>
    </div>
  )
}