import { BrowseCard } from './BrowseCard';

interface BrowseSectionProps {
  genres: string[];
}

export const BrowseSection = ({ genres }: BrowseSectionProps) => {
  return (
    <div className="grid gap-x-4 gap-y-4 justify-center"
      style={{
        gridTemplateColumns: 'repeat(auto-fill, minmax(148px, 1fr))'
      }}>
      {genres.map((genre) => (
        <BrowseCard key={genre} genre={genre} />
      ))}
    </div>
  )
}