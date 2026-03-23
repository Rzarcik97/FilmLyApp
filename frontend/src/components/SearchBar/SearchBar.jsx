import { useState } from 'react';
import search_icon from '../../../public/icons/search.png';
import './SearchBar.scss';

export const SearchBar = () => {
  const [query, setQuery] = useState('');

  const handleInputChange = (event) => {
    setQuery(event.target.value);
    console.log('Searching for:', event.target.value);
  };

  return (
    <div className="search-container">
      <input
        type="text" 
        className="search-container__input"
        placeholder="Search for movies, TV series, actors..."
        value={query}
        onChange={handleInputChange}
      />
      <button className="search-container__button">
        <img src={search_icon} alt="Search" className="search-container__button__img" />
      </button>
    </div>
  )
}