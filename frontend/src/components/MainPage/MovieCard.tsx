import type { Movie } from '../../types';
import { Link, useLocation } from 'react-router-dom';
import imdb from '../../../public/icons/imdb.png';
import { UserStar } from 'lucide-react';
import empty_img from '../../../public/icons/empty-img.png';

export const MovieCard = ({ movie }: { movie: Movie }) => {
  const location = useLocation();

  return (
    <div className="w-[200px] h-[400px] flex flex-col shrink-0">
      <Link
        to={`/movie/${movie.id}`}
        state={{ from: location.pathname }}
      >
        <div className="bg-primary-background h-[328px] flex-1 flex justify-center items-center">
          {/* <img src={movie.poster_path} alt="Item Main Image" /> */}
          <img
            src={empty_img}
            alt="Item Main Image"
            className="w-27 h-27"
          />
        </div>
        <div className="p-2 bg-dark-background h-18 shrink-0">
          <div className="flex items-center justify-between text-white">
            <div className="flex gap-[7px] items-center">
              <p className="text-[16px]">7.6</p>
              <img src={imdb} alt="IMDB Rating" className="w-6 h-6" />
            </div>
            <div className="flex gap-[7px] items-center">
              <p className="text-[16px]">85%</p>
              <UserStar size={22} />
              {/* <img src={rt} alt="Rotten Tomatoes Rating" className="w-6 h-6" /> */}
            </div>
          </div>
          {/* <p>{movie.title}</p> */}
          <p className="pt-[13px] text-white text-[16px]">Movie Title</p>
        </div>
      </Link>
    </div>
  )
}