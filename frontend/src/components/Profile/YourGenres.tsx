import { useEffect, useState } from 'react';
import type { Genre } from '../../types';
import { getGenres } from '../../api/movieService';
import { addFavoriteGenre, deleteFavoriteGenre, getFavoriteGenres } from '../../api/genreService';

export const YourGenres = () => {
  const [loading, setLoading] = useState(true);
  const [genres, setGenres] = useState<Genre[]>([]);
  const [isExpanded, setIsExpanded] = useState(false);
  const [favoriteGenres, setFavoriteGenres] = useState<string[]>([]);

  useEffect(() => {
    const fetchGenres = async () => {
      try {
        setLoading(true);
        const [allGenresResult, favGenresResult] = await Promise.allSettled([
          getGenres(),
          getFavoriteGenres()
        ]);

        const getValue = <T,>(result: PromiseSettledResult<T>, fallback: T): T => {
          return result.status === 'fulfilled' ? result.value : fallback;
        };

        setGenres(getValue(allGenresResult, []));

        const rawFavs = getValue(favGenresResult, []);

        const favNames = Array.isArray(rawFavs)
          ? rawFavs.map((item: any) => item.genreName)
          : [];

        setFavoriteGenres(favNames);
      } catch (error) {
        console.error('Failed to load genres', error);
      } finally {
        setLoading(false);
      }
    };

    fetchGenres();
  }, []);

  const handleToggleFavorite = async (genreName: string) => {
    const isAlreadyFav = favoriteGenres.includes(genreName);

    if (isAlreadyFav) {
      setFavoriteGenres(prev => prev.filter(name => name !== genreName));
    } else {
      setFavoriteGenres(prev => [...prev, genreName]);
    }

    try {
      if (isAlreadyFav) {
        await deleteFavoriteGenre(genreName);
      } else {
        await addFavoriteGenre(genreName, 10);
      }
    } catch (error) {
      console.error('Sync failed:', error);
      if (isAlreadyFav) {
        setFavoriteGenres(prev => [...prev, genreName]);
      } else {
        setFavoriteGenres(prev => prev.filter(name => name !== genreName));
      }
    }
  };

  const featuredNames = ['Action', 'Adventure', 'Comedy', 'Documentary', 'Kids', 'Sci-Fi & Fantasy'];

  const displayedGenres = isExpanded ? genres : genres.filter(genre => featuredNames.includes(genre.name));

  return (
    <section className="py-10 px-4 md:px-12">
      <div className="flex justify-between items-center pb-3 md:pb-6">
        <div className="flex gap-2 items-center">
          <div className="w-1 h-17 md:h-15 bg-primary-0 rounded-full" />
          <div>
            <h2 className="text-[24px] md:text-[36px] leading-[1.2] font-bold text-secondary-light">Your favorite genres</h2>
            <p className="text-[16px] md:text-[20px] leading-[1.45] font-bold text-secondary-light">Select genres you like to get personalized movie recommendations</p>
          </div>          
        </div>

        <button
          onClick={() => setIsExpanded(!isExpanded)}
          className="cursor-pointer text-[16px] text-secondary-light font-nunito font-bold"
        >
          {isExpanded ? 'Hide' : 'View all'}
        </button>
      </div>

      <div className="flex flex-wrap gap-4 justify-between items-center">
        {displayedGenres.map((genre, index) => {
          const isSelected = favoriteGenres.includes(genre.name);

          return (
            <button
              key={genre.id}
              onClick={() => handleToggleFavorite(genre.name)}
              className={`flex justify-center items-center text-[14px] md:text-[16px] font-nunito 
                        border-1 rounded-[32px] w-full sm:w-51 h-12 cursor-pointer transition-all
                        ${isSelected
                  ? 'bg-primary-0 border-primary-0 text-secondary-dark font-bold'
                  : 'bg-transparent border-gray-30 text-gray-30 hover:border-primary-0 hover:text-primary-0'}
                        ${!isExpanded && index >= 3 ? 'hidden md:flex' : 'flex'} 
              `}
            >
              {genre.name}
            </button>
          );
        })}
      </div>
    </section>
  )
}