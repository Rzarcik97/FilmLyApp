import { useParams } from 'react-router-dom'
import { FiltersSideBar } from '../BrowsePage/FiltersSideBar'
import { DiscoverContent } from './DiscoverContent';

export const DiscoverPage = () => {
  const { type } = useParams<{ type: string }>();
  const pageTitle = type ? type.replace('-', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'Discover';

  return (
    <div className="bg-gray-100 -mt-[111px] pt-37 flex min-h-screen relative z-50">
      <aside className="w-[328px] flex-shrink-0 hidden md:block">
        <FiltersSideBar title={pageTitle} />
      </aside>
      
      <main className="flex-1 px-10 pt-18">
        <div className="flex gap-2 items-center pb-4">
          <div className="w-1 h-[35px] bg-primary-0 rounded-full" />
          <h2 className="text-[24px] md:text-[36px] leading-[1.2] font-bold text-secondary-light">
            Discover the best cinema on Filmly
          </h2>
        </div>
        <DiscoverContent />
      </main>
    </div>
  )
}