import { useEffect, useState } from 'react'
import { ScrollSection } from '../MainPage/ScrollSection';
import { ProfileMain } from './ProfileMain';
import { ChevronDown } from 'lucide-react';
import { useDispatch, useSelector } from 'react-redux';
import { type AppDispatch, type RootState } from '../../store';
import { fetchCurrentUser } from '../../store/userSlice';
import { ProfileSettings } from './ProfileSettings';

export const Profile = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { user, loading } = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    if (!user) {
      dispatch(fetchCurrentUser());
    }
  }, [dispatch, user]);

  const displayName = loading ? '...' : user?.username || 'Member';
  const displayEmail = loading ? '...' : user?.email || 'email@example.com'

  const [activeTab, setActiveTab] = useState('watchlist');

  const tabs = [
    { id: 'watchlist', label: 'Watchlist' },
    { id: 'settings', label: 'Profile Settings' },
  ];

  return (
    <main className="bg-gray-100">
      <ProfileMain name={displayName} />

      <div className="pt-10 lg:pt-[58px] flex px-4 md:px-12 pb-0 lg:pb-10">
        {tabs.map(tab => {
          const isActive = activeTab === tab.id;

          return (
            <div
              key={tab.id}
              className="flex items-center bg-gray-100 cursor-pointer font-bold text-[20px] md:text-[36px] leading-[1.45] md:leading-[1.2] w-full md:w-auto"
            >
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`flex-1 lg:w-[330px] lg:flex-none 
                            text-center cursor-pointer font-bold transition-all duration-300
                            text-[20px] md:text-[36px] leading-[1.45] md:leading-[1.2]
                            py-2 md:py-6 px-2
                ${isActive
                    ? 'text-gray-0 border-b-2 border-primary-0'
                    : 'text-gray-70 hover:text-gray-0 border-b border-gray-70'}
                `}
              >
                {tab.label}
              </button>
            </div>
          )
        })}
      </div>

      <div>
        {activeTab === 'watchlist' && (
          <div className="animate-in fade-in duration-300">
            <ScrollSection title="To watch" items={[]} viewAllPath='' />
            <ScrollSection title="Top Picks for You" items={[]} viewAllPath='' />
            <ScrollSection title="Your Favorites" items={[]} viewAllPath='' />
            <ScrollSection title="Already Discovered" items={[]} viewAllPath='' />
          </div>
        )}

        {activeTab === 'settings' && (
          <div className="animate-in fade-in duration-300">
            <ProfileSettings name={displayName} email={displayEmail} />
          </div>
        )}
      </div>
    </main>
  )
}