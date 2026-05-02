import { useParams } from 'react-router-dom'
import { FiltersSideBar } from '../BrowsePage/FiltersSideBar'
import { DiscoverContent } from './DiscoverContent';
import { useEffect, useState } from 'react';
import { getGenres } from '../../api/movieService';
import type { Genre } from '../../types';
import sort from '/icons/filter.png';
import { useDispatch, useSelector } from 'react-redux';
import { type AppDispatch, type RootState } from '../../store';
import { toggleGenre } from '../../store/filtersSlice';

export const DiscoverPage = () => {
  const [allGenres, setAllGenres] = useState<Genre[]>([]);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const dispatch = useDispatch<AppDispatch>();
  const filters = useSelector((state: RootState) => state.filters);

  const { type } = useParams<{ type: string }>();
  const pageTitle = type ? type.replace('-', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'Discover';

  useEffect(() => {
    const fetchGenres = async () => {
      const genres = await getGenres();
      setAllGenres(genres);

      if (type && genres.length > 0) {
        const matchedGenre = genres.find(
          (g) => g.name.toLowerCase().replace(/[\s&]+/g, '-') === type.toLowerCase()
        );

        if (matchedGenre && !filters.selectedGenreIds.includes(matchedGenre.id)) {
          dispatch(toggleGenre(matchedGenre.id));
        }
      }
    };
    fetchGenres();
  }, [type]);

  return (
    <div className="bg-gray-100 pt-35 flex min-h-screen relative z-50">
      <aside className={`
      ${isSidebarOpen ? 'fixed z-[100] backdrop-blur-sm' : 'hidden'} 
      md:relative md:inset-auto md:z-auto md:flex md:w-[328px] md:flex-shrink-0 md:bg-transparent md:self-start
    `}>
        <div
          className="fixed inset-0 md:hidden"
          onClick={() => setIsSidebarOpen(false)}
        />
        <FiltersSideBar
          title={pageTitle}
          setIsSidebarOpen={setIsSidebarOpen}
        />
      </aside>

      <main className="flex-1 px-4 md:px-10 pt-2">
        <div className="flex gap-2 items-center md:pb-2">
          <div className="w-1 h-[50px] md:h-[35px] bg-primary-0 rounded-full" />
          <h2 className="text-[24px] md:text-[36px] leading-[1.2] font-bold text-secondary-light">
            Discover the best cinema on Filmly
          </h2>
        </div>
        <div className="md:hidden block text-right pt-2">
          <button
            onClick={() => setIsSidebarOpen(true)}
            className="px-2 w-8 h-8 rounded-full bg-gray-90"
          >
            <img src={sort} alt="Open the filtration side bar" />
          </button>
        </div>

        <div className="flex flex-col items-center md:block">
          <DiscoverContent
            filters={{
              ...filters,
              allGenres: allGenres
            }}
          />
        </div>

      </main>
    </div>
  )
}