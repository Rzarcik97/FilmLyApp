import { useState } from 'react';
import search from '../../../public/icons/search.png';
import mic from '../../../public/icons/mic.png';

export const SearchBar = () => {
  const [query, setQuery] = useState('');

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(event.target.value);
    console.log('Searching for:', event.target.value);
  };

  return (
    <div className="flex justify-between items-center gap-[30px] px-2 py-2 rounded-lg bg-gray-0 border border-gray-80 w-[536px] h-10">
      <button className="flex justify-center items-center bg-none cursor-pointer">
        <img src={search} alt="Search button" className="w-6 h-6"/>
      </button>
      <input
        type="text" 
        className="py-3 w-full border-none bg-transparent focus:outline-none text-gray-30 text-[16px] font-bold"
        placeholder="Search for movies, TV series, actors..."
        value={query}
        onChange={handleInputChange}
      />
      <button className="flex justify-center items-center bg-none cursor-pointer">
        <img src={mic} alt="Voice search" className="w-6 h-6" />
      </button>
    </div>
  )
}