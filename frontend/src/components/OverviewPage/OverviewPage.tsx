import type { Actor, Movie } from '../../types';
import { mockActors, mockMovies } from '../../utils/mockData';
import { Header } from '../MainPage/Header';
import { ScrollActors } from '../MainPage/ScrollActors';
import { ScrollSection } from '../MainPage/ScrollSection';
import { MainOverview } from './MainOverview';
import { MoreDetails } from './MoreDetails';

export const OverviewPage = () => {
  return (
    <div className="bg-light-background">
      <MainOverview />
      <MoreDetails />
      <ScrollActors title="Cast" items={mockActors}/>
      <ScrollSection title="Yoy may also like" items={mockMovies} />
    </div>
  )
}