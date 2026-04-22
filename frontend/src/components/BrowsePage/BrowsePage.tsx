import { getGenres } from '../../api/movieService'
import { BrowseCard } from './BrowseCard'
import { GenericBrowseSection } from './GenericBrowseSection'

export const BrowsePage = () => {
  return (
    <div className="flex min-h-screen bg-gray-100 pt-24 flow-root">

      <main className="flex-1 p-22 px-12">
        <h1 className="text-[36px] leading-[1.2] text-secondary-light font-bold mb-4">Browse</h1>
        <GenericBrowseSection
          fetchFn={getGenres}
          renderItem={(genre) => <BrowseCard genre={genre.name} />}
        />
      </main>
    </div>
  )
}