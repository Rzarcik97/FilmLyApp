import { useParams, useSearchParams } from 'react-router-dom'
import { GenericBrowseSection } from '../BrowsePage/GenericBrowseSection';
import { getPopularMovies, getRecentMovies, getSearchData, getTrendingMovies, getTrendingSeries, getUpcomingMovies } from '../../api/movieService';
import { MovieCard } from '../MainPage/MovieCard';
import type { Genre, Movie } from '../../types';

export type SortOrder = 'newest' | 'oldest' | 'default';
export interface FilterState {
  selectedGenreIds: number[];
  selectedCountries: string[];
  allGenres: Genre[];
  sortBy: SortOrder;
}

export const DiscoverContent = ({ filters }: { filters: FilterState }) => {
  const { type } = useParams<{ type: string }>();
  const [searchParams] = useSearchParams();
  const searchQuery = searchParams.get('q') || '';

  const applyFilters = (item: any) => {
    const matchesGenre = filters.selectedGenreIds.length === 0 ||
      item.genres?.some((genreObj: any) =>
        filters.selectedGenreIds.includes(genreObj.id)
      );

    const itemCountries = Array.isArray(item.origin_country)
      ? item.origin_country
      : [item.origin_country];

    const matchesCountry = filters.selectedCountries.length === 0 ||
      itemCountries.some((c: string) => filters.selectedCountries.includes(c));

    return matchesGenre && matchesCountry;
  };

  const sortFn = (a: Movie, b: Movie) => {
    if (filters.sortBy === 'default') return 0;

    const dateA = a.release_date || '';
    const dateB = b.release_date || '';

    if (filters.sortBy === 'newest') {
      return dateB.localeCompare(dateA);
    } else {
      return dateA.localeCompare(dateB);
    }
  };

  switch (type) {
    case 'trending-movies':
      return <GenericBrowseSection<Movie> 
        fetchFn={getTrendingMovies}
        filterFn={applyFilters}
        sortFn={sortFn}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'trending-series':
      return <GenericBrowseSection<Movie>
        fetchFn={getTrendingSeries}
        filterFn={applyFilters}
        sortFn={sortFn}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'popular-movies':
      return <GenericBrowseSection<Movie>
        fetchFn={getPopularMovies}
        filterFn={applyFilters}
        sortFn={sortFn}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'recent-movies':
      return <GenericBrowseSection<Movie>
        fetchFn={getRecentMovies}
        filterFn={applyFilters}
        sortFn={sortFn}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'upcoming-movies':
      return <GenericBrowseSection<Movie>
        fetchFn={getUpcomingMovies}
        filterFn={applyFilters}
        sortFn={sortFn}
        renderItem={(item) => <MovieCard movie={item} />}
      />

    case 'search':
      return <GenericBrowseSection<Movie>
        key={searchQuery}
        fetchFn={() => getSearchData(searchQuery)}
        filterFn={applyFilters}
        sortFn={sortFn}
        renderItem={(item) => <MovieCard movie={item} />}
      />
  }
}