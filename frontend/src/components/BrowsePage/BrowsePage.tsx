import { mockBrowseGenres } from '../../utils/mockData'
import { BrowseSection } from './BrowseSection'
import { FiltersSideBar } from './FiltersSideBar'

export const BrowsePage = () => {
  return (
    <div className="flex min-h-screen">
      <FiltersSideBar title="Discover the best cinema on Filmly" />

      <main className="flex-1 p-10">
        <h1 className="text-3xl font-bold mb-4">Browse</h1>
        <BrowseSection genres={mockBrowseGenres} />
      </main>
    </div>
  )
}