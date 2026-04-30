import { Link, useLocation } from 'react-router-dom';
import empty_img from '/icons/empty-img.png';

interface BrowseCardProps {
  genre: string;
  imagePath?: string | undefined;
}

export const BrowseCard = ({ genre, imagePath }: BrowseCardProps) => {
  const location = useLocation();
  const baseUrl = import.meta.env.VITE_BASE_URL;

  const hasImage = imagePath && imagePath !== 'string';
  const imageUrl = imagePath ? `${baseUrl}${imagePath}` : empty_img;

  return (
    <div className="w-[204px] h-[254px] flex flex-col shrink-0 rounded-[16px] overflow-hidden
          border border-gray-30/10 backdrop-blur-[2px]
          before:content-[''] before:absolute before:inset-0
          before:rounded-[16px] before:border before:border-gray-80/20
          before:pointer-events-none
          cursor-pointer
    ">
      <Link
        to={`/browse/${genre.toLowerCase().replace(/ /g, '-')}`}
        state={{ from: location.pathname }}
        className="flex flex-col h-full w-full"
      >
        <div className="bg-gray-90 flex-1 flex justify-center items-center overflow-hidden">
          <img
            src={imageUrl}
            alt={genre}
            className={`transition-all duration-500 
              ${hasImage
                ? 'w-full h-full object-cover'
                : 'w-27 h-27 object-contain'
              }`}
            onError={(e) => {
              const target = e.target as HTMLImageElement;
              target.src = empty_img;
              target.className = 'w-27 h-27 object-contain';
            }}
          />
        </div>

        <div className="px-2 bg-gray-100 h-12 shrink-0 flex items-center z-10">
          <p className="text-gray-0 text-[16px] leading-[1.5] font-bold font-nunito truncate">
            {genre}
          </p>
        </div>
      </Link>
    </div>
  )
}