import { useParams, useSearchParams } from 'react-router-dom'
import { GenericBrowseSection } from '../BrowsePage/GenericBrowseSection';
import { getContentByGenre, getMoviesByRating, getPopularMovies, getRecentMovies, getSearchData, getTrendingMovies, getTrendingSeries, getUpcomingMovies } from '../../api/movieService';
import { MovieCard } from '../MainPage/MovieCard';
import type { Genre, Movie } from '../../types';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { useSelector } from 'react-redux';
import type { RootState } from '../../store';
import { DiscoverByGenre } from './DiscoverByGenre';

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

  const [genreMovies, setGenreMovies] = useState<Movie[]>([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [totalPages, setTotalPages] = useState(1);

  const searchQuery = searchParams.get('q') || '';
  const watchedIds = useSelector((state: RootState) => state.watchlist.watchedItems);

  const contentType = type?.includes('series') ? 'SERIES' : 'MOVIE';

  const defaultRoutes = ['trending-movies', 'trending-series', 'popular-movies', 'recent-movies', 'upcoming-movies', 'search'];
  const isGenreRoute = type && !defaultRoutes.includes(type);

  const currentGenreId = useMemo(() => {
    if (!type || defaultRoutes.includes(type)) return filters.selectedGenreIds[0];

    const normalizedSlug = type.toLowerCase().replace(/[^a-z0-9]/g, '');

    let matchingGenre = filters.allGenres.find(g => {
      const normalizedName = g.name.toLowerCase().replace(/[^a-z0-9]/g, '');
      return normalizedName === normalizedSlug && (g.type === contentType || g.type === 'BOTH');
    });

    if (!matchingGenre) {
      const equivalents: Record<string, string[]> = {
        'kids': ['family', 'animation'],
        'scififantasy': ['sciencefiction', 'fantasy'],
        'actionadventure': ['action', 'adventure']
      };

      const potentialMatches = equivalents[normalizedSlug] || [];

      matchingGenre = filters.allGenres.find(g => {
        const normalizedName = g.name.toLowerCase().replace(/[^a-z0-9]/g, '');
        return potentialMatches.includes(normalizedName) && (g.type === contentType || g.type === 'BOTH');
      });
    }

    return matchingGenre ? matchingGenre.id : filters.selectedGenreIds[0];
  }, [type, contentType, filters.allGenres, filters.selectedGenreIds]);

  const fetchGenrePage = useCallback(async (pageNum: number, isNew: boolean) => {
    console.log("Fetching Genre:", { currentGenreId, contentType, pageNum });

    if (!currentGenreId) {
      console.warn("No currentGenreId found! The mapping might be broken.");
      return;
    }
    setIsLoading(true);

    try {
      const data = await getContentByGenre(currentGenreId, contentType, pageNum);
      const newItems = data.content || data.results || [];
      const total = data.totalPages || 1;

      setTotalPages(total);

      setGenreMovies(prev => isNew ? [...prev, ...newItems] : newItems);
      setHasMore(pageNum < total);
    } catch (error) {
      console.error("Genre fetch error:", error);
    } finally {
      setIsLoading(false);
    }
  }, [currentGenreId, contentType]);

  useEffect(() => {
    if (isGenreRoute && currentGenreId) {
      console.log(`Route Active: ${type} | ID: ${currentGenreId} | Content: ${contentType}`);
      setPage(1);
      fetchGenrePage(1, false);
    }
  }, [currentGenreId, type, fetchGenrePage, isGenreRoute]);

  const handleLoadMore = () => {
    const nextPage = page + 1;
    setPage(nextPage);
    fetchGenrePage(nextPage, true);
  };

  const handleJumpToPage = (targetPage: number) => {
    if (targetPage < 1 || targetPage > totalPages) return;
    setPage(targetPage);
    fetchGenrePage(targetPage, false);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

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

    if (!matchesCountry) return false;

    if (isGenreRoute) return true;

    return matchesGenre;
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

  if (isGenreRoute) {
    const filteredAndSorted = genreMovies.filter(applyFilters).sort(sortFn);

    return (
      <DiscoverByGenre
        items={filteredAndSorted}
        isLoading={isLoading}
        page={page}
        totalPages={totalPages}
        hasMore={hasMore}
        onLoadMore={handleLoadMore}
        onJumpToPage={handleJumpToPage}
      />
    );
  }

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