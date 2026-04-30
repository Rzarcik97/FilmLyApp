import { Plus } from 'lucide-react';
import profile from '../../../public/profile/profile.png';
import { Reminder } from './Reminder';
import { Preferences } from './Preferences';
import { AccountSecurity } from './AccountSecurity';
import { UsernameField } from './UsernameField';
import { useState } from 'react';
import { Toast } from './Toast';

export const ProfileSettings = ({ name, email }: { name: string, email: string }) => {
  const [showToast, setShowToast] = useState(false);

  const handleToast = () => {
    setShowToast(true);
    setTimeout(() => setShowToast(false), 2000);
  };

  return (
    <main className="py-5 md:py-10 px-6 md:px-12">
      <div>
        <h2 className="font-bold text-white text-[18px] md:text-[32px] leading-[1.3] pb-2">My Profile</h2>
        <div className="bg-gray-dark rounded-[16px] px-4 md:px-6 py-4 md:py-6">
          <div className="flex gap-2 md:gap-4 pb-3 md:pb-6">
            <img src={profile} alt="Profile Pic" className="w-15 h-15 md:w-21 md:h-21" />
            <div className="flex flex-col">
              <button
                onClick={handleToast}
                className="cursor-pointer flex justify-center items-center px-1 md:px-2 py-1.5 md:py-3 gap-1 md:gap-2 border border-gray-80 bg-secondary-dark rounded-[9px] hover:bg-gray-90"
              >
                <Plus className="text-primary-0 w-5 h-5 md:w-8 md:h-8" />
                <span className="font-semibold font-nunito text-gray-30 text-[12px] md:text-[24px]">Change Image</span>
              </button>
              <p className="text-gray-70 text-[14px] md:text-[20px] leading-[1.45]">We support under 2 MB</p>
            </div>
            <Toast isVisible={showToast} />
          </div>

          <div className="flex flex-col gap-1">
            <p className="text-gray-30 text-[14px] md:text-[20px] leading-[1.45]">User Name</p>
            <UsernameField initialName={name} />
          </div>
        </div>
      </div>

      <AccountSecurity email={email} />
      <Reminder />
      <Preferences />
    </main>
  )
}