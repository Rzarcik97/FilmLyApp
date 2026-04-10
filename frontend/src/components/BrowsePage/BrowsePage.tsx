import { mockBrowseGenres } from '../../utils/mockData'
import { BrowseSection } from './BrowseSection'
import { FiltersSideBar } from './FiltersSideBar'

export const BrowsePage = () => {
  return (
    <div className="flex min-h-screen bg-gray-100 -mt-[111px] pt-[111px] flow-root">
      {/* <FiltersSideBar title="Discover the best cinema on Filmly" /> */}

      <main className="flex-1 p-22 px-12">
        <h1 className="text-[36px] leading-[1.2] text-secondary-light font-bold mb-4">Browse</h1>
        <BrowseSection />
      </main>
    </div>
  )
}