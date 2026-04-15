import { useEffect, useState } from 'react';
import type { Genre } from '../../types';
import { BrowseCard } from './BrowseCard';
import { getGenres } from '../../api/movieService';

export const BrowseSection = () => {
  const [allGenres, setAllGenres] = useState<Genre[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAllGenres = async () => {
      try {
        setLoading(true);

        const genres = await getGenres();
        setAllGenres(genres);
      } catch (error) {
        console.error('Failed to load data from the backend', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAllGenres();
  }, []);
  
  return (
    <div className="grid gap-x-6 gap-y-4.5 justify-center"
      style={{
        gridTemplateColumns: 'repeat(auto-fill, minmax(204px, 1fr))'
      }}>
      {allGenres.map((genre) => (
        <BrowseCard key={genre.id} genre={genre.name} />
      ))}
    </div>
  )
}