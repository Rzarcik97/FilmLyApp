import { Link, useLocation } from 'react-router-dom';
import empty_img from '../../../public/icons/empty-img.png';
import type { Movie } from '../../types';

interface BrowseCardProps {
  genre: string;
}

export const BrowseCard = ({ genre }: BrowseCardProps) => {
  const location = useLocation();

  return (
    <div className="w-[148px] h-[256px] flex flex-col shrink-0">
      <Link
        to={`/test-browse/${genre.toLowerCase().replace(/ /g, '-')}`}
        state={{ from: location.pathname }}
      >
        <div className="bg-primary-background h-[221px] flex-1 flex justify-center items-center">
          <img
            src={empty_img}
            alt="Item Main Image"
            className="w-27 h-27"
          />
        </div>
        <div className="p-2 bg-dark-background h-[35px] shrink-0 flex items-center">
            <p className="text-white text-[16px]">{genre}</p>     
        </div>
      </Link>
    </div>
  )
}