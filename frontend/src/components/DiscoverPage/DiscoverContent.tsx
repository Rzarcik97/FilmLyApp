import { useParams, useSearchParams } from 'react-router-dom'
import { GenericBrowseSection } from '../BrowsePage/GenericBrowseSection';
import { getPopularMovies, getRecentMovies, getSearchData, getTrendingMovies, getTrendingSeries, getUpcomingMovies } from '../../api/movieService';
import { MovieCard } from '../MainPage/MovieCard';
import type { Genre, Movie } from '../../types';

export type DateSort = 'newest' | 'oldest' | 'default';
export type TitleSort = 'asc' | 'desc' | 'default';
export interface FilterState {
  selectedGenreIds: number[];
  selectedCountries: string[];
  allGenres: Genre[];
  dateSort: DateSort;
  titleSort: TitleSort;
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
    if (filters.dateSort === 'default' && filters.titleSort !== 'default') {
      const titleA = a.title || '';
      const titleB = b.title || '';
      return filters.titleSort === 'asc' ? titleA.localeCompare(titleB) : titleB.localeCompare(titleA);
    }

    if (filters.titleSort === 'default' && filters.dateSort !== 'default') {
      const dateA = a.release_date || '';
      const dateB = b.release_date || '';
      return filters.dateSort === 'newest' ? dateB.localeCompare(dateA) : dateA.localeCompare(dateB);
    }

    if (filters.dateSort !== 'default' && filters.titleSort !== 'default') {
      const dateA = a.release_date || '';
      const dateB = b.release_date || '';
      const dateResult = filters.dateSort === 'newest' ? dateB.localeCompare(dateA) : dateA.localeCompare(dateB);

      if (dateResult !== 0) return dateResult;

      const titleA = a.title || '';
      const titleB = b.title || '';
      return filters.titleSort === 'asc' ? titleA.localeCompare(titleB) : titleB.localeCompare(titleA);
    }

    return 0;
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