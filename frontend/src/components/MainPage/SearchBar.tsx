import { useState } from 'react';
import search from '../../../public/icons/search.png';
import mic from '../../../public/icons/mic.png';

interface SearchBarProps {
  onFocusChange: (focused: boolean) => void;
  isFocused: boolean;
}

const MOCK_RESULTS = [
  { id: 1, title: 'A', category: 'TV Show - Drama' },
  { id: 2, title: 'Ax', category: 'TV Show - Drama' },
  { id: 3, title: 'Asde', category: 'TV Show - Drama' },
]; // temp thing for testing the search bar

export const SearchBar = ({ onFocusChange, isFocused }: SearchBarProps) => {
  const [query, setQuery] = useState('');
  const showDropdown = isFocused && query.length > 0;

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(event.target.value);
    console.log('Searching for:', event.target.value);
  };

  return (
    <div className="flex justify-between items-center gap-[30px] px-2 py-2 rounded-lg bg-gray-0 border border-gray-80 w-[536px] h-10">
      <button className="flex justify-center items-center bg-none cursor-pointer">
        <img src={search} alt="Search button" className="w-6 h-6" />
      </button>
      <input
        type="text"
        className={`py-3 w-full border-none bg-transparent focus:outline-none text-gray-30 text-[16px] font-bold`}
        placeholder="Search for movies, TV series, actors..."
        value={query}
        onChange={handleInputChange}
        onFocus={() => onFocusChange(true)}
        onBlur={() => onFocusChange(false)}
      />

      {showDropdown && (
        <div className="absolute top-[calc(100%+8px)] left-0 right-0 bg-gray-0 rounded-xl shadow-2xl overflow-hidden z-[60] font-nunito">
          <ul className="flex flex-col">
            {MOCK_RESULTS.map((item) => (
              <li
                key={item.id}
                className="px-4 py-3 border-b border-[#D8D8D8] hover:bg-gray-30 cursor-pointer text-gray-100 transition-colors"
              >
                <span className="font-medium">{item.title}</span>
                <span className="text-gray-100 ml-2">({item.category})</span>
              </li>
            ))}
          </ul>

          <div className="p-[10px] w-full">
            <button className="w-full h-[42px] py-3 bg-secondary-dark text-primary-0 rounded-[32px] font-semibold text-sm hover:bg-black transition-all">
              View all results (123)
            </button>
          </div>
        </div>
      )}
      
      <button className="flex justify-center items-center bg-none cursor-pointer">
        <img src={mic} alt="Voice search" className="w-6 h-6" />
      </button>
    </div>
  )
}