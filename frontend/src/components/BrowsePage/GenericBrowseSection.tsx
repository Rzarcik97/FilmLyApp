import { Loader } from 'lucide-react';
import { useEffect, useState } from 'react';
interface GenericBrowseProps<T> {
  fetchFn: () => Promise<T[]>;
  renderItem: (item: T) => React.ReactNode;
}

export const GenericBrowseSection = <T extends { id: number | string }>({
  fetchFn,
  renderItem
}: GenericBrowseProps<T>) => {
  const [items, setItems] = useState<T[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const data = await fetchFn();
        setItems(data);
      } catch (error) {
        console.error('Failed to load data from the backend', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [fetchFn]);

  if (loading) return <Loader />

  return (
    <div
      className="grid gap-x-6 gap-y-4.5 justify-center"
      style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(204px, 1fr))' }}
    >
      {items.map((item) => (
        <div key={item.id}>
          {renderItem(item)}
        </div>
      ))}
    </div>
  );
};