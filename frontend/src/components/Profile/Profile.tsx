import { useState } from 'react'
import { ScrollSection } from '../MainPage/ScrollSection';

export const Profile = () => {
  const [activeTab, setActiveTab] = useState('watchlist');

  const tabs = [
    { id: 'watchlist', label: 'Watchlist' },
    { id: 'settings', label: 'Profile Settings' },
    { id: 'reminder', label: 'Reminder' },
  ];

  return (
    <main className="bg-gray-100 text-gray-0 px-12 pt-50">
      <h1>Hello, name!</h1>
      <div className="flex gap-10">
        {tabs.map(tab => (
          <button key={tab.id} onClick={() => setActiveTab(tab.id)} className="border border-primary-0 bg-gray-90 cursor-pointer w-100">
            {tab.label}
          </button>
        ))}
      </div>

      <div>
        {activeTab === 'watchlist' && (
          <div className="animate-in fade-in duration-300">
            <ScrollSection title="Your Watchlist" items={[]} viewAllPath=''/>
            <ScrollSection title="Your Personalized Watchlist" items={[]} viewAllPath=''/>
            <ScrollSection title="Watched movies" items={[]} viewAllPath=''/>
          </div>
        )}

        {activeTab === 'reminder' && (
          <div className="animate-in fade-in duration-300">
            <p>This feature is not implemented yet</p>
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