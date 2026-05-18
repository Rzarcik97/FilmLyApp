import privacy from '/profile/privacy.svg';
import faq from '/profile/faq.svg';
import help from '/profile/help.svg';
import about from '/profile/about.svg';
import { ChevronRight } from 'lucide-react';
import { useState } from 'react';
import { Toast } from './Toast';

export const Preferences = () => {
  const [showToast, setShowToast] = useState(false);

  const handleToast = () => {
    setShowToast(true);
    setTimeout(() => setShowToast(false), 2000);
  };

  const menuItems = [
    { icon: privacy, label: 'Privacy policy' },
    { icon: faq, label: 'FAQ' },
    { icon: help, label: 'Help Center' },
    { icon: about, label: 'About' },
  ];

  return (
    <section className="pt-5 md:pt-10">
      <h2 className="font-bold text-white text-[18px] md:text-[32px] leading-[1.3] pb-2 md:pb-4">
        Preferences
      </h2>

      <div className="bg-gray-dark rounded-[16px] p-2 md:p-4">
        {menuItems.map((item, index) => (
          <div
            key={item.label}
            onClick={handleToast}
            className={`flex justify-between items-center p-3 md:p-6 cursor-pointer hover:bg-white/5 transition-colors
              ${index !== menuItems.length - 1 ? 'border-b border-gray-80' : ''}`}
          >
            <div className="flex gap-2 md:gap-4 items-center">
              <img src={item.icon} alt="" className="w-4 h-4 md:w-6 md:h-6" />
              <span className="font-nunito text-secondary-light text-[14px] md:text-[20px] leading-[1.45]">
                {item.label}
              </span>
            </div>
            <ChevronRight className="text-gray-70 w-5 h-5 md:w-6 md:h-6" />
          </div>
        ))}
      </div>

      <Toast isVisible={showToast} />
    </section>
  );
};