import { Loader2, Search, X } from 'lucide-react';
import { useSearchLogic } from './useSearchLogic';
import type { Movie } from '../../types';

export const MobileSearch = ({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) => {
  const { query, setQuery, results, allResults, loading, handleSearchSubmit, navigate, formatType } = useSearchLogic(onClose);

    const handleItemClick = (item: Movie) => (e: React.MouseEvent) => {
    e.preventDefault();

    const id = item.id || (item as any).contentId;
    const rawType = item.type?.toLowerCase() || 'movie';
    const typePath = rawType === 'movie' ? 'movies' : 'series';

    if (!id) return;

    const path = `/${typePath}/${id}`;

    navigate(path);
    setQuery('');
  };

  if (!isOpen) return null;

  return (
    <div 
      className="fixed inset-0 z-200 bg-gray-100/60 backdrop-blur-sm flex flex-col p-4 animate-in fade-in duration-200"
      onClick={onClose}
    >
      <div className="flex items-center gap-2 w-full">
        <div className="flex-1 flex items-center gap-2 bg-gray-90 border border-gray-50 rounded-xl px-4 h-12">
          <Search size={24} className="text-gray-50" />
          <input
            autoFocus
            type="text"
            className="bg-transparent w-full outline-none text-white font-medium"
            placeholder="Search..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSearchSubmit(query)}
          />
        </div>
      </div>

      <div className="mt-2 bg-gray-90 rounded-xl overflow-hidden flex flex-col shadow-2xl">
        {loading ? (
          <div className="p-8 flex justify-center">
            <Loader2 className="w-8 h-8 text-primary-20 animate-spin" />
            <span className="text-gray-30 text-sm">Searching...</span>
          </div>
        ) : (
          <>
            <ul className="flex flex-col">
              {results.map((item) => (
                <li
                  key={item.id}
                  onMouseDown={handleItemClick(item)}
                  className="px-4 py-3 border-b border-secondary-dark hover:bg-gray-100 text-gray-30 transition-colors"
                >
                  <span className="font-semibold">{item.title}</span>
                  <span className="text-gray-30 ml-2">({formatType(item.type)})</span>
                </li>
              ))}
            </ul>

            {query.length > 0 && (
              <div className="p-[10px] w-full">
                <button
                  onClick={() => handleSearchSubmit(query)}
                  className="w-full h-[42px] border border-primary-20 py-3 bg-secondary-dark text-primary-0 rounded-[32px] font-semibold text-sm hover:bg-gray-100 transition-all"
                >
                  View all results ({allResults})
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  )
}