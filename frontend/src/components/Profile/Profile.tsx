import { useEffect, useState } from 'react'
import { ScrollSection } from '../MainPage/ScrollSection';
import { ProfileMain } from './ProfileMain';
import { ChevronDown } from 'lucide-react';
import { useDispatch, useSelector } from 'react-redux';
import { type AppDispatch, type RootState } from '../../store';
import { fetchCurrentUser } from '../../store/userSlice';

export const Profile = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { user, loading } = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    if (!user) {
      dispatch(fetchCurrentUser());
    }
  }, [dispatch, user]);

  const displayName = loading ? '...' : user?.username || 'Member';

  const [activeTab, setActiveTab] = useState('watchlist');

  const tabs = [
    { id: 'watchlist', label: 'Watchlist' },
    { id: 'settings', label: 'Profile Settings' },
  ];

  return (
    <main className="bg-gray-100">
      <ProfileMain name={displayName}/>

      <div className="pt-10 lg:pt-[58px] flex gap-4 md:gap-7 px-4 md:px-12 pb-0 lg:pb-10">
        {tabs.map(tab => (
          <div className="flex gap-2 items-center border-b border-gray-70 bg-gray-100 cursor-pointer">
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className="text-gray-0 px-2 py-2 md:py-3 cursor-pointer"
            >
              {tab.label}
            </button>
            <ChevronDown size={24} className="text-gray-70" />
          </div>          
        ))}
      </div>

      <div>
        {activeTab === 'watchlist' && (
          <div className="animate-in fade-in duration-300">
            <ScrollSection title="To watch" items={[]} viewAllPath=''/>
            <ScrollSection title="Top Picks for You" items={[]} viewAllPath=''/>
            <ScrollSection title="Your Favorites" items={[]} viewAllPath=''/>
            <ScrollSection title="Already Discovered" items={[]} viewAllPath=''/>
          </div>
        )}

        {activeTab === 'settings' && (
          <div className="animate-in fade-in duration-300">
            <p>Change name / change password / some other settings bullshit</p>
          </div>
        )}
      </div>
    </main>
  )
}