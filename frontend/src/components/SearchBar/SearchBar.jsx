import { useState } from 'react';
import search_icon from '../../../public/icons/search.png';

export const SearchBar = () => {
  const [query, setQuery] = useState('');

  const handleInputChange = (event) => {
    setQuery(event.target.value);
    console.log('Searching for:', event.target.value);
  };

  return (
    <div className="flex justify-between items-center px-2 rounded-lg bg-white w-[536px] h-10">
      <input
        type="text" 
        className="py-3 w-full border-none bg-transparent focus:outline-none"
        placeholder="Search for movies, TV series, actors..."
        value={query}
        onChange={handleInputChange}
      />
      <button className="flex justify-center items-center bg-none cursor-pointer">
        <img src={search_icon} alt="Search" className="w-6 h-6" />
      </button>
    </div>
  )
}