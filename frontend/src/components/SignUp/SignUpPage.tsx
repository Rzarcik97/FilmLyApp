import { useEffect, useState } from 'react'
import { SignUp } from './SignUp'
import { Loader } from '../Utilities/Loader';

export const SignUpPage = () => {
  const [loading, setLoading] = useState(true);
  const baseColor = 'rgba(17, 17, 16, 1)';

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, []);

  if (loading) return <Loader />

  return (
    <main 
      className="relative py-24 bg-cover bg-center bg-no-repeat"
      style={{ backgroundImage: "url('/backgrounds/sign_up.png')" }}
    >
      <SignUp />

      <div
        className="absolute inset-x-0 bottom-0 h-40 z-0"
        style={{
          background: `linear-gradient(to bottom, transparent, ${baseColor})`
        }}
      />
    </main>
  )
}