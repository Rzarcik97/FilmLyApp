import { Loader } from '../Utilities/Loader';
import { useLoadData } from '../../hooks/useLoadData';
import { NotFoundPage } from '../Utilities/NotFoundPage';

interface GenericBrowseProps<T> {
  fetchFn: () => Promise<T[]>;
  renderItem: (item: T) => React.ReactNode;
}

export const GenericBrowseSection = <T extends { id: number | string }>({
  fetchFn,
  renderItem
}: GenericBrowseProps<T>) => {

  const { data: items, loading, error } = useLoadData(fetchFn);

  if (loading) return <Loader />

  if (error) return <NotFoundPage />

  return (
    <div
      className="grid gap-x-6 gap-y-4.5 justify-center"
      style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(204px, 1fr))' }}
    >
      {items?.map((item) => (
        <div key={item.id}>
          {renderItem(item)}
        </div>
      ))}
    </div>
  );
};