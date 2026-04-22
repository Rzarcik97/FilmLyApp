import { useParams, useSearchParams } from 'react-router-dom'
import { GenericBrowseSection } from '../BrowsePage/GenericBrowseSection';
import { getPopularMovies, getRecentMovies, getSearchData, getTrendingMovies, getTrendingSeries, getUpcomingMovies } from '../../api/movieService';
import { MovieCard } from '../MainPage/MovieCard';
import type { Movie } from '../../types';

export const DiscoverContent = () => {
  const { type } = useParams<{ type: string }>();
  const [searchParams] = useSearchParams();
  const searchQuery = searchParams.get('q') || '';

  switch (type) {
    case 'trending-movies':
      return <GenericBrowseSection<Movie> 
        fetchFn={getTrendingMovies}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'trending-series':
      return <GenericBrowseSection<Movie>
        fetchFn={getTrendingSeries}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'popular-movies':
      return <GenericBrowseSection<Movie>
        fetchFn={getPopularMovies}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'recent-movies':
      return <GenericBrowseSection<Movie>
        fetchFn={getRecentMovies}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'upcoming-movies':
      return <GenericBrowseSection<Movie>
        fetchFn={getUpcomingMovies}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'search':
      return <GenericBrowseSection<Movie>
        key={searchQuery}
        fetchFn={() => getSearchData(searchQuery)}
        renderItem={(item) => <MovieCard movie={item} />}
      />
  }
}