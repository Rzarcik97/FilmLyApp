import reminderIcon from '/profile/reminder.svg';
import { Ban } from 'lucide-react';
import { Checkbox } from '../../assets/CheckBox';
import { useState } from 'react';
import { Toast } from './Toast';

const REMIND_ME_OPTIONS = [
  'The movie becomes available',
  'A new episode is released',
  'It’s added to your region',
  'Before release (1day)',
];

const DONT_REMIND_OPTIONS = [
  "I've already watched it",
  'A new episode is released',
  'I removed it from watchlist',
];

export const Reminder = () => {
  const [showToast, setShowToast] = useState(false);

  const handleToast = () => {
    setShowToast(true);
    setTimeout(() => setShowToast(false), 2000);
  };

  return (
    <section>
      <h2 className="font-bold text-white text-[18px] md:text-[32px] leading-[1.3] pb-2 md:pb-4">
        My Reminders
      </h2>

      <div className="flex flex-col md:flex-row gap-2 md:gap-6 w-full">
        <div className="flex-1">
          <div className="flex gap-2 items-center pb-2">
            <img src={reminderIcon} alt="" className="w-4 h-4 md:w-6 md:h-6" />
            <span className="font-nunito text-white text-[14px] md:text-[20px] leading-[1.45]">
              Remind me When...
            </span>
          </div>

          <div className="bg-gray-dark rounded-[16px] p-2 md:p-4">
            {REMIND_ME_OPTIONS.map((text, index) => (
              <label
                key={text}
                onClick={handleToast}
                className={`flex items-center gap-2 md:gap-4 p-3 md:p-6 cursor-pointer hover:bg-white/5 transition-colors
                  ${index !== REMIND_ME_OPTIONS.length - 1 ? 'border-b border-gray-80' : ''}`}
              >
                <Checkbox />
                <span className="font-nunito text-secondary-light text-[14px] md:text-[20px] leading-[1.45]">
                  {text}
                </span>
              </label>
            ))}
          </div>
          <Toast isVisible={showToast} />
        </div>

        <div className="flex-1">
          <div className="flex gap-2 items-center pb-2">
            <Ban className="text-system-error w-4 h-4 md:w-6 md:h-6" />
            <span className="font-nunito text-white text-[14px] md:text-[20px] leading-[1.45]">
              Don’t remind me if:
            </span>
          </div>

          <div className="bg-gray-dark rounded-[16px] p-2 md:p-4">
            {DONT_REMIND_OPTIONS.map((text, index) => (
              <label
                key={text}
                onClick={handleToast}
                className={`flex items-center gap-2 md:gap-4 p-3 md:p-6 cursor-pointer hover:bg-white/5 transition-colors
                  ${index !== DONT_REMIND_OPTIONS.length - 1 ? 'border-b border-gray-80' : ''}`}
              >
                <Checkbox />
                <span className="font-nunito text-secondary-light text-[14px] md:text-[20px] leading-[1.45]">
                  {text}
                </span>
              </label>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
};