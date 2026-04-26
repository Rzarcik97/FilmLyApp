import imdb from '../../../public/icons/imdb.png';
import thumbsUp from '../../../public/icons/thumbUp-primary.png';
import play from '../../../public/icons/play.png';
import mute from '../../../public/icons/mute.png';
import type { Movie } from '../../types';

interface MainOverviewProps {
  movie: Movie;
}

export const MainOverview = ({ movie }: MainOverviewProps) => {
  const genres = movie?.genres;
  const backdropUrl = movie?.backdrop_path
    ? `https://image.tmdb.org/t/p/original${movie.backdrop_path}`
    : '';
  
  return (
    <div
      className="bg-gray-100 px-10 min-h-screen bg-cover bg-center bg-no-repeat relative flex flex-col"
      style={{
        backgroundImage: `linear-gradient(to top, rgba(0,0,0,0.8) 0%, rgba(0,0,0,0.3) 40%, transparent 100%), url(${backdropUrl})`
      }}
    >
      <div className="flex-grow" />

      <div className="flex justify-between items-end pb-8">
        <div className="w-[432px] flex flex-col gap-2">
          <div className="flex md:hidden gap-[20px] justify-end pb-2">
            <img src={play} alt="Play Button" className="cursor-pointer w-8 h-8" />
            <img src={mute} alt="Play Button" className="cursor-pointer w-8 h-8" />
          </div>
          <h1 className="text-[36px] md:text-[48px] text-gray-0 font-bold text-center font-nunito leading-[1]">{movie?.title || 'Movie Title'}</h1>
          {movie?.tagLine && (
            <p className="text-[20px] leading-[1.45] text-gray-0 font-semibold text-center font-nunito">{`"${movie?.tagLine}"`}</p>
          )}

          <div className="flex flex-wrap justify-center items-center gap-2">
            {genres?.map((genre) => (
              <span
                key={genre.id}
                className="px-[10px] py-[2.5px] pb-[5px] text-gray-30 text-[16px] border border-gray-30 rounded-[32px]"
              >
                {genre.name}
              </span>
            ))}
          </div>
          <p className="text-[20px] text-gray-30 text-center leading-[1.45]">
            {movie?.release_date?.split('-')[0] || 'YYYY'} • {' '}

            {movie?.type === 'MOVIE' ? (
              <span>{movie?.runtime || 0}m </span>
            ) : (
              <span>{movie?.numberOfEpisodes || 0} Episodes </span>
            )}

            • <span className="capitalize">
              {movie?.type === 'SERIES' ? 'TV Series' : movie?.type?.toLowerCase()}
            </span>
          </p>

          <div className="flex justify-evenly items-center">
            <div className="flex justify-center items-center gap-2">
              <p className="text-[16px] leading-[1.5] text-gray-0 font-bold">{Number(movie.voteAverage || movie.vote_average || 0).toFixed(1) || '0.0'}</p>
              <img src={imdb} alt="Imdb Rating" className="w-6 h-5" />
            </div>
            <div className="flex justify-center items-center gap-2">
              <p className="text-[16px] leading-[1.5] text-gray-0 font-bold">85%</p>
              <img src={thumbsUp} alt="Filmly Rating" className="w-8 h-8" />
            </div>
          </div>
        </div>

        <div className="hidden md:flex gap-[30px]">
            <img src={play} alt="Play Button" className="cursor-pointer w-8 h-8" />
            <img src={mute} alt="Play Button" className="cursor-pointer w-8 h-8" />
        </div>
      </div>
      
    </div>
  )
}