import { useLocation, useNavigate } from 'react-router-dom';
import type { Genre } from '../../types'

interface MainBrowseProps {
  genres: Genre[];
}

export const MainBrowse = ({ genres }: MainBrowseProps) => {
  const navigate = useNavigate();
  const location = useLocation();

  const featuredNames = ['Action', 'Adventure', 'Comedy', 'Documentary', 'Kids', 'Sci-Fi & Fantasy'];

  const displayedGenres = genres.filter(genre => featuredNames.includes(genre.name));

  const handleGenreClick = (genreName: string) => {
    const slug = genreName.toLowerCase().replace(/ /g, '-');
    navigate(`/browse/${slug}`, { state: { from: location.pathname } });
  };

  return (
    <section className="px-12 pb-8">
      <div className="pt-10 flex justify-end items-center pb-4">
        <button
          onClick={() => navigate('/browse')}
          className="cursor-pointer text-[16px] text-secondary-light font-nunito font-bold"
        >
          View all
        </button>
      </div>

      <div className="flex gap-6 justify-center items-center">
        {displayedGenres.map((genre, index) => (
          <button
            key={genre.id}
            onClick={() => handleGenreClick(genre.name)}
            className={`flex justify-center items-center text-[16px] text-gray-30 font-nunito 
                        border-1 border-gray-30 rounded-[32px]
                        w-51 h-12 cursor-pointer
                        ${index >= 4 ? 'hidden md:flex' : 'flex'} 
            `}
          >
            {genre.name}
          </button>
        ))}
      </div>
    </section>
  )
}