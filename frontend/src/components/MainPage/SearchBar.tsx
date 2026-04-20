import { useEffect, useState } from 'react';
import search from '../../../public/icons/search.svg';
import mic from '../../../public/icons/mic.svg';
import { useNavigate } from 'react-router-dom';
import type { Movie } from '../../types';
import { getSearchData } from '../../api/movieService';
import { Loader2 } from 'lucide-react';
interface SearchBarProps {
  onFocusChange: (focused: boolean) => void;
  isFocused: boolean;
}

export const SearchBar = ({ onFocusChange, isFocused }: SearchBarProps) => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<Movie[]>([]);
  const [allResults, setAllResults] = useState(0);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const showDropdown = isFocused && query.trim().length >= 0;

  useEffect(() => {
    if (query.trim().length < 0) {
      setResults([]);
      setAllResults(0);
      return;
    }

    const timer = setTimeout(async () => {
      setLoading(true);
      try {
        const data = await getSearchData(query);
        setResults(data.slice(0, 5));
        setAllResults(data.length);
      } catch (err) {
        console.error('Autocompletion error: ', err);
      } finally {
        setLoading(false);
      }
    }, 300);

    return () => clearTimeout(timer);
  }, [query]);

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(event.target.value);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && query.trim()) {
      console.log('Search submitted for:', query);

      navigate(`/discover/search?q=${encodeURIComponent(query)}`);
      onFocusChange(false);
      setQuery('');
    }
  };

  const handleItemClick = (item: Movie) => (e: React.MouseEvent) => {
    e.preventDefault();

    const id = item.id || (item as any).contentId;
    const rawType = item.type?.toLowerCase() || 'movie';
    const typePath = rawType === 'movie' ? 'movies' : 'series';

    if (!id) {
      console.error("Could not find an ID for this item:", item);
      return;
    }

    const path = `/${typePath}/${id}`;
    console.log('Navigating to correct plural path:', path);

    navigate(path);
    onFocusChange(false);
    setQuery('');
  };

  const formatType = (type: string) => {
    const types: Record<string, string> = {
      MOVIE: 'Movie',
      SERIES: 'TV-Series',
    };

    return types[type.toUpperCase()] || type;
  };

  return (
    <div className="flex justify-between items-center gap-[30px] px-2 py-2 rounded-lg bg-gray-90 border border-gray-80 lg:w-[536px] h-10">
      <button className="flex justify-center items-center bg-none cursor-pointer">
        <img src={search} alt="Search button" className="w-6 h-6" />
      </button>
      <input
        type="text"
        className={`py-3 w-full border-none bg-transparent focus:outline-none text-gray-30 text-[16px] font-bold`}
        placeholder="Search for movies, TV series, actors..."
        value={query}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        onFocus={() => onFocusChange(true)}
        onBlur={() => onFocusChange(false)}
      />

      {showDropdown && (
        <div className="absolute top-[calc(100%+8px)] left-0 right-0 bg-gray-90 rounded-xl shadow-2xl overflow-hidden z-[60] font-nunito">
          <ul className="flex flex-col">
            {loading ? (
              <div className="flex flex-col items-center justify-center p-10 gap-2">
                <Loader2 className="w-8 h-8 text-primary-20 animate-spin" />
                <span className="text-gray-30 text-sm">Searching...</span>
              </div>
            ) : results.length > 0 ? (
              results.map((item) => (
                <li
                  key={item.id}
                  onMouseDown={handleItemClick(item)}
                  className="px-4 py-3 border-b border-secondary-dark hover:bg-gray-100 cursor-pointer text-gray-30 transition-colors"
                >
                  <span className="font-medium">{item.title}</span>
                  <span className="text-gray-30 ml-2">({formatType(item.type)})</span>
                </li>
              ))
            ) : (
              <li className="px-4 py-3 text-gray-30">No results found</li>
            )}
          </ul>

          {results.length > 0 && (
            <div className="p-[10px] w-full">
              <button 
                onMouseDown={(e) => e.preventDefault()}
                onClick={() => {
                  navigate(`/discover/search?q=${encodeURIComponent(query)}`);
                  onFocusChange(false);
                  setQuery('');
                }}
                className="w-full h-[42px] border border-primary-20 py-3 bg-secondary-dark text-primary-0 rounded-[32px] font-semibold text-sm hover:bg-gray-100 transition-all"
              >
                View all results ({allResults})
              </button>
            </div>
          )}     
        </div>
      )}
      
      <button className="flex justify-center items-center bg-none cursor-pointer">
        <img src={mic} alt="Voice search" className="w-6 h-6" />
      </button>
    </div>
  )
}