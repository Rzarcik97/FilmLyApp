import { useEffect, useState } from 'react';
import type { Movie } from '../../types';
import { useNavigate } from 'react-router-dom';
import { getSearchData } from '../../api/movieService';

export const useSearchLogic = (onClose?: () => void) => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<Movie[]>([]);
  const [allResults, setAllResults] = useState(0);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

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

  const handleSearchSubmit = (searchQuery: string) => {
    if (!searchQuery.trim()) return;
    navigate(`/discover/search?q=${encodeURIComponent(searchQuery)}`);
    setQuery('');
    if (onClose) onClose();
  };

  const formatType = (type: string) => {
    const types: Record<string, string> = {
      MOVIE: 'Movie',
      SERIES: 'TV-Series',
    };

    return types[type.toUpperCase()] || type;
  };

  return { query, setQuery, results, allResults, loading, handleSearchSubmit, navigate, formatType };
}