import empty_img from '../../../public/icons/empty-img.png';
import progress1 from '../../../public/sign_up/progress-1.png';
import google_btn from '../../../public/sign_up/google-btn.png';
import fb_btn from '../../../public/sign_up/fb-btn.png';
import apple_btn from '../../../public/sign_up/apple-btn.png';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { signUpSchema } from '../../utils/validationSchemas';
import type { SignUpFormData } from '../../utils/validationSchemas';

export const SignUp = () => {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: {errors, isValid},
  } = useForm<SignUpFormData>({
    resolver: zodResolver(signUpSchema),
    mode: 'onChange',
    defaultValues: {
      email: '',
      isAgreedToTerms: false as unknown as true,
      isAgreedToPromotions: false,
    }
  });

  const onSubmit = (data: SignUpFormData) => {
    navigate('/sign-up/password', { state: { email: data.email }});
  }

  return (
    <div className="bg-primary-background">
      <h1 className="text-signup-text text-[48px] font-bold pt-[43px] px-24 text-center pb-15">Skip the search. Just choose.</h1>

      <div className="px-6 lg:px-24 flex flex-col lg:flex-row gap-12 lg:gap-[111px] items-center justify-start min-h-screen pb-8">
        <div className="px-6 lg:px-12 py-10 w-full max-w-[609px] flex-shrink-0 flex flex-col bg-signup-primary-background rounded-[32px]">
          <form
            onSubmit={handleSubmit(onSubmit)}
          >
            <p className="pb-8 lg:pb-[50px] text-signup-text-2 text-[20px] lg:text-[24px] text-center font-semibold">Sign up to dive into movies</p>

            <div className="px-24 flex justify-between items-center">
              <img src={google_btn} alt="Sign Up with Google" className="w-20 h-20" />
              <img src={fb_btn} alt="Sign Up with Facebook" className="w-22 h-22 object-fit: contain" />
              <img src={apple_btn} alt="Sign Up with Apple" className="w-20 h-20" />
            </div>

            <div className="flex items-center w-full my-6 gap-4">
              <div className="flex-1 h-[1px] bg-signup-line"></div>

              <span className="text-signup-text-2 text-[20px] font-semibold">or</span>

              <div className="flex-1 h-[1px] bg-signup-line"></div>
            </div>

            <div>
              <div className="flex flex-col gap-2 pb-4">
                <label htmlFor="email" className="text-signup-text-3 text-[16px]">
                  E-mail
                </label>
                <input
                  id="email"
                  type="email"
                  placeholder="example@email.com"
                  className={`w-full bg-transparent border rounded-[8px] px-4 py-3 text-white transition-colors focus:outline-none
                ${errors.email ? 'border-red-500' : 'border-signup-border focus:border-white'}`}
                  {...register('email')}
                />

                {errors.email && (
                  <p className="text-red-500 text-xs mt-1 italic animate-fade-in">
                    Please enter a valid email address (e.g., name@domain.com)
                  </p>
                )}
              </div>

              <div className="flex flex-col gap-2">
                <label className="flex items-center gap-3 cursor-pointer group">
                  <input
                    type="checkbox"
                    className="w-6 h-6 rounded-full border border-signup-text-3 appearance-none checked:bg-primary-background checked:border-signup-text-3 transition-all cursor-pointer relative after:content-[''] after:hidden checked:after:block"
                    {...register('isAgreedToPromotions')}
                  />
                  <span className="text-signup-text-3 text-[14px]">
                    I agree to receive{" "}
                    <span className="underline underline-offset-4 decoration-1">
                      Promotional and informational messages
                    </span>
                  </span>
                </label>

                <label className="flex items-center gap-3 cursor-pointer group">
                  <input
                    type="checkbox"
                    className="w-6 h-6 rounded-full border border-signup-text-3 appearance-none checked:bg-primary-background checked:border-signup-text-3 transition-all cursor-pointer"
                    {...register('isAgreedToTerms')}
                  />
                  <span className="text-signup-text-3 text-[14px]">
                    I agree to our{" "}
                    <span className="underline underline-offset-4 decoration-1">
                      Terms of Service
                    </span>{" "}
                    and{" "}
                    <span className="underline underline-offset-4 decoration-1">
                      Privacy Policy
                    </span>
                  </span>
                </label>
                {errors.isAgreedToTerms && <p className="text-red-500 text-xs">{errors.isAgreedToTerms.message}</p>}
              </div>
            </div>

            <div className="py-[51px]">
              <button
                type="submit"
                disabled={!isValid}
                className={`w-full h-[61px] rounded-[32px] border-none text-[24px] font-bold transition-all duration-300
              ${!isValid
                    ? 'bg-signup-button-disabled text-signup-text-4'
                    : 'bg-white text-signup-primary-background cursor-pointer hover:bg-gray-200 shadow-lg'
                  }`}
              >
                Sign Up
              </button>
            </div>

            <div className="flex gap-[10px] justify-center items-center text-[16px]">
              <span className="text-signup-text-3">Already have an account?</span>
              <button className="cursor-pointer text-decoration-line: underline text-white">
                <span>Log in</span>
              </button>
            </div>
          </form>        
        </div>

        <div className="flex flex-col gap-[115px] justify-center items-center w-full">
          <img src={empty_img} alt="Sign Up Main Image" className="w-27 h-27" />
          <img src={progress1} alt="" className="max-w-[513px]" />
        </div>
      </div>
    </div>
  )
}