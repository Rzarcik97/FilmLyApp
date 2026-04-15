import { useParams } from 'react-router-dom'
import { FiltersSideBar } from '../BrowsePage/FiltersSideBar'

export const DiscoverPage = () => {
  const { type } = useParams<{ type: string }>();
  const pageTitle = type === 'movies' ? 'Movies' : 'TV Series';

  return (
    <div className="bg-gray-100 -mt-[111px] pt-37 flow-root">
      <FiltersSideBar title={pageTitle} />
    </div>
  )
}