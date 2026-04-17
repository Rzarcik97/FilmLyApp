import bg_image from '../../../public/backgrounds/bg_not_found.png';
import oops from '../../../public/backgrounds/oops.svg';
import not_found from '../../../public/backgrounds/page_not_found.svg';

export const NotFoundPage = () => {
  const bgPath = '/backgrounds/not_found_bg.svg';
  const oopsPath = '/backgrounds/oops.svg';
  const notFoundPath = '/backgrounds/page_not_found.svg';
  const baseColor = '#151412';

  return (
    <div className="-mt-[111px] relative w-full min-h-screen flex flex-col items-center justify-center overflow-hidden bg-[#151412]">
      <div
        className="absolute inset-0 z-0 bg-cover bg-center bg-no-repeat mt-60"
        style={{
          backgroundImage: `url(${bgPath})`,
          WebkitMaskImage: 'linear-gradient(to bottom, transparent 0%, black 15%, black 85%, transparent 100%)',
          maskImage: 'linear-gradient(to bottom, transparent 0%, black 15%, black 85%, transparent 100%)',
          filter: 'brightness(0.6) saturate(0.9)'
        }}
      />

      <div
        className="absolute inset-0 z-10 pointer-events-none"
        style={{
          background: `
            radial-gradient(circle at 50% 70%, rgba(21, 20, 18, 0) 0%, rgba(21, 20, 18, 0.4) 40%, ${baseColor} 95%),
            linear-gradient(to bottom, ${baseColor} 0%, transparent 20%, transparent 80%, ${baseColor} 100%)
          `
        }}
      />
    </div>
  )
}