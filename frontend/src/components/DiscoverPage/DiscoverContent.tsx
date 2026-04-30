import { useParams, useSearchParams } from 'react-router-dom'
import { GenericBrowseSection } from '../BrowsePage/GenericBrowseSection';
import { getContentByGenre, getMoviesByRating, getPopularMovies, getRecentMovies, getSearchData, getTrendingMovies, getTrendingSeries, getUpcomingMovies } from '../../api/movieService';
import { MovieCard } from '../MainPage/MovieCard';
import type { Genre, Movie } from '../../types';
import { useCallback } from 'react';
import { useSelector } from 'react-redux';
import type { RootState } from '../../store';

export type DateSort = 'newest' | 'oldest' | 'default';
export type TitleSort = 'asc' | 'desc' | 'default';
export interface FilterState {
  selectedGenreIds: number[];
  selectedCountries: string[];
  allGenres: Genre[];
  dateSort: DateSort;
  titleSort: TitleSort;
  imdbRange: number[];
  isImdbActive: boolean;
  hideWatched: boolean;
}

export const DiscoverContent = ({ filters }: { filters: FilterState }) => {
  const { type } = useParams<{ type: string }>();
  const [searchParams] = useSearchParams();
  const searchQuery = searchParams.get('q') || '';
  const watchedIds = useSelector((state: RootState) => state.watchlist.watchedItems);

  const contentType = type?.includes('series') ? 'SERIES' : 'MOVIE';

  const universalFetcher = useCallback(async () => {
    if (filters.isImdbActive) {
      const response = await getMoviesByRating(
        filters.imdbRange[0] ?? 0,
        filters.imdbRange[1] ?? 0,
        contentType
      );
      const results = response.results || response.content || response;
      return Array.isArray(results) ? results : [];
    }

    const currentGenreId = filters.selectedGenreIds[0];

    const defaultRoutes = ['trending-movies', 'trending-series', 'popular-movies', 'recent-movies', 'upcoming-movies', 'search'];
    const isGenreRoute = type && !defaultRoutes.includes(type);

    if (isGenreRoute && currentGenreId) {
      const data = await getContentByGenre(currentGenreId, contentType);
      return data.results || data.content || data;
    }

    const defaultFetchers: Record<string, () => Promise<any>> = {
      'trending-movies': getTrendingMovies,
      'trending-series': getTrendingSeries,
      'popular-movies': getPopularMovies,
      'recent-movies': getRecentMovies,
      'upcoming-movies': getUpcomingMovies,
      'search': () => getSearchData(searchParams.get('q') || ''),
    };

    const activeDefaultFetch = defaultFetchers[type || ''] || getPopularMovies;
    const defaultData = await activeDefaultFetch();
    return defaultData.results || defaultData.content || defaultData;

  }, [filters.isImdbActive, filters.selectedGenreIds, type, contentType, searchParams]);

  const applyFilters = (item: Movie) => {
    if (filters.hideWatched) {
      const isThisWatched = watchedIds.includes(item.id) || watchedIds.includes(Number(item.contentId));

      if (isThisWatched) {
        return false;
      }
    }

    const matchesGenre = filters.selectedGenreIds.length === 0 ||
      item.genres?.some((genreObj: Genre) =>
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
    case 'trending-series':
    case 'popular-movies':
    case 'recent-movies':
    case 'upcoming-movies':
      return (
        <GenericBrowseSection<Movie>
          key={`${type}-${filters.isImdbActive}`}
          fetchFn={universalFetcher}
          filterFn={applyFilters}
          sortFn={sortFn}
          renderItem={(item) => <MovieCard movie={item} />}
        />
      );

    case 'search':
      return (
        <GenericBrowseSection<Movie>
          key={searchQuery}
          fetchFn={() => getSearchData(searchQuery)}
          filterFn={applyFilters}
          sortFn={sortFn}
          renderItem={(item) => <MovieCard movie={item} />}
        />
      );

    default:
      return (
        <GenericBrowseSection<Movie>
          key={`genre-${type}-${filters.isImdbActive}`}
          fetchFn={universalFetcher}
          filterFn={applyFilters}
          sortFn={sortFn}
          renderItem={(item) => <MovieCard movie={item} />}
        />
      );
  }
}