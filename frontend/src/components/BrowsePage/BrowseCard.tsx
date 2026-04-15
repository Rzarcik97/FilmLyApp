import { Link, useLocation } from 'react-router-dom';
import empty_img from '../../../public/icons/empty-img.png';
import type { Movie } from '../../types';

interface BrowseCardProps {
  genre: string;
}

export const BrowseCard = ({ genre }: BrowseCardProps) => {
  const location = useLocation();

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
      >
        <div className="bg-gray-90 h-[208px] flex-1 flex justify-center items-center">
          <img
            src={empty_img}
            alt="Item Main Image"
            className="w-27 h-27"
          />
        </div>
        <div className="px-2 bg-gray-100 h-12 shrink-0 flex items-center">
            <p className="text-gray-0 text-[16px] leading-[1.5] font-bold font-nunito">{genre}</p>     
        </div>
      </Link>
    </div>
  )
}