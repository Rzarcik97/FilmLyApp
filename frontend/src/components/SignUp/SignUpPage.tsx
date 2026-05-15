import { useEffect, useState } from 'react'
import { SignUp } from './SignUp'
import { Loader } from '../Utilities/Loader';

export const SignUpPage = () => {
  const [loading, setLoading] = useState(true);
  const bgImageUrl = '/backgrounds/sign_up.png';
  const baseColor = 'rgba(17, 17, 16, 1)';

  useEffect(() => {
    const img = new Image();
    img.src = bgImageUrl;

    img.onload = () => {
      setLoading(false);
    };

    img.onerror = () => {
      setLoading(false);
    };
  }, [bgImageUrl]);

  if (loading) return <Loader />

  return (
    <main 
      className="relative py-24 bg-cover bg-center bg-no-repeat"
      style={{ backgroundImage: `url('${bgImageUrl}')` }}
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