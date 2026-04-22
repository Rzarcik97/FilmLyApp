import { useParams } from 'react-router-dom'
import { FiltersSideBar } from '../BrowsePage/FiltersSideBar'
import { DiscoverContent, type FilterState, type SortOrder } from './DiscoverContent';
import { useEffect, useState } from 'react';
import { getGenres } from '../../api/movieService';
import type { Genre } from '../../types';
import sort from '../../../public/icons/filter.png';

export const DiscoverPage = () => {
  const [selectedGenreIds, setSelectedGenreIds] = useState<number[]>([]);
  const [selectedCountries, setSelectedCountries] = useState<string[]>([]);
  const [allGenres, setAllGenres] = useState<Genre[]>([]);
  const [sortBy, setSortBy] = useState<SortOrder>('default');
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const { type } = useParams<{ type: string }>();
  const pageTitle = type ? type.replace('-', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'Discover';

  useEffect(() => {
    const fetchGenres = async () => {
      const genres = await getGenres();
      setAllGenres(genres);
    };
    fetchGenres();
  }, []);

  const filters: FilterState = {
    selectedGenreIds,
    selectedCountries,
    allGenres,
    sortBy
  };

  return (
    <div className="bg-gray-100 pt-30 flex min-h-screen relative z-50">
      <aside className={`
      ${isSidebarOpen ? 'fixed inset-0 z-[100] flex justify-center items-center bg-black/60 backdrop-blur-sm' : 'hidden'} 
      md:relative md:inset-auto md:z-auto md:flex md:w-[328px] md:flex-shrink-0 md:bg-transparent
    `}>
        <div
          className="fixed inset-0 md:hidden"
          onClick={() => setIsSidebarOpen(false)}
        />
        <FiltersSideBar 
          title={pageTitle} 
          selectedGenreIds={selectedGenreIds}
          setSelectedGenreIds={setSelectedGenreIds}
          selectedCountries={selectedCountries}
          setSelectedCountries={setSelectedCountries}
          allGenres={allGenres}
          sortBy={sortBy}
          setSortBy={setSortBy}
        />
      </aside>
      
      <main className="flex-1 px-10 pt-2">
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
            filters={filters}
          />
        </div>
        
      </main>
    </div>
  )
}