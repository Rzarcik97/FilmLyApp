import { getPopularActors } from '../../api/movieService'
import type { Actor } from '../../types'
import { ActorCard } from '../MainPage/ActorCard'
import { GenericBrowseSection } from './GenericBrowseSection'

export const ActorsPage = () => {
  return (
      <div className="flex min-h-screen bg-gray-100 pt-24 flow-root">
  
        <main className="flex-1 p-22 px-12">
          <h1 className="text-[36px] leading-[1.2] text-secondary-light font-bold mb-4">Browse the Actors</h1>
          <GenericBrowseSection<Actor>
            fetchFn={getPopularActors}
            renderItem={(actor) => <ActorCard actor={actor} />}
          />
        </main>
      </div>
    )
}