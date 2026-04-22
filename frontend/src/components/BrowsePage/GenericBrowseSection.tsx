import { Loader } from '../Utilities/Loader';
import { useLoadData } from '../../hooks/useLoadData';
import { NotFoundPage } from '../Utilities/NotFoundPage';

interface GenericBrowseProps<T> {
  fetchFn: () => Promise<T[]>;
  renderItem: (item: T) => React.ReactNode;
  filterFn?: (item: T) => boolean;
  sortFn?: (a: T, b: T) => number;
}

export const GenericBrowseSection = <T extends { id: number | string }>({
  fetchFn,
  renderItem,
  filterFn,
  sortFn
}: GenericBrowseProps<T>) => {

  const { data: items, loading, error } = useLoadData(fetchFn);

  if (loading) return <Loader />

  if (error) return <NotFoundPage />

  const allItems = Array.isArray(items) ? items : [];
  let displayedItems = filterFn ? allItems.filter(filterFn) : allItems;

  if (sortFn) {
    displayedItems = [...displayedItems].sort(sortFn);
  }

  return (
    <div
      className="grid gap-x-6 gap-y-7.5 justify-center 
               grid-cols-[repeat(auto-fill,minmax(175px,1fr))] 
               lg:grid-cols-[repeat(auto-fill,minmax(204px,1fr))]"
    >
      {displayedItems.map((item) => (
        <div key={item.id}>
          {renderItem(item)}
        </div>
      ))}
    </div>
  );
};