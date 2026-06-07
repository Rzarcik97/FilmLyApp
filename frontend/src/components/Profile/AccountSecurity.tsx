import { Mail, Lock, ChevronRight } from 'lucide-react';
import { useState } from 'react';
import { EmailChangeModal } from '../Modals/EmailChangeModal';
import { PasswordChangeModal } from '../Modals/PasswordChangeModal';

export const AccountSecurity = ({ email }: { email: string }) => {
  const [isEmailModalOpen, setIsEmailModalOpen] = useState(false);
  const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);

  return (
    <section className="pt-5 md:pt-10 pb-5 md:pb-10">
      <h2 className="font-bold text-white text-[18px] md:text-[32px] leading-[1.3] pb-2 md:pb-4">
        Account Security
      </h2>

      <div className="bg-gray-dark rounded-[16px] p-2 md:p-4">
        <button
          onClick={() => setIsEmailModalOpen(true)}
          className="w-full flex justify-between items-center p-3 md:p-6 border-b border-gray-80 hover:bg-white/5 transition-colors cursor-pointer group"
        >
          <div className="flex gap-2 md:gap-4 items-center">
            <Mail className="text-gray-70 group-hover:text-primary-0 transition-colors w-4 h-4 md:w-6 md:h-6" />
            <div className="flex flex-col items-start">
              <span className="font-nunito text-secondary-light text-[14px] md:text-[20px]">Email</span>
              <span className="text-gray-50 text-[12px] md:text-[16px]">{email}</span>
            </div>
          </div>
          <ChevronRight className="text-gray-70" />
        </button>

        <EmailChangeModal
          isOpen={isEmailModalOpen}
          onClose={() => setIsEmailModalOpen(false)}
        />

        <button
          onClick={() => setIsPasswordModalOpen(true)}
          className="w-full flex justify-between items-center p-3 md:p-6 hover:bg-white/5 transition-colors cursor-pointer group"
        >
          <div className="flex gap-2 md:gap-4 items-center">
            <Lock className="text-gray-70 group-hover:text-primary-0 transition-colors w-4 h-4 md:w-6 md:h-6" />
            <div className="flex flex-col items-start">
              <span className="font-nunito text-secondary-light text-[14px] md:text-[20px]">Password</span>
              <span className="text-gray-50 text-[12px] md:text-[16px]">••••••••••••</span>
            </div>
          </div>
          <ChevronRight className="text-gray-70" />
        </button>

        <PasswordChangeModal
          isOpen={isPasswordModalOpen}
          onClose={() => setIsPasswordModalOpen(false)}
        />
      </div>
    </section>
  );
};