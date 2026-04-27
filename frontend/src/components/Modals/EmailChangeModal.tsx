import { useState } from 'react';
import { authService } from '../../api/authService';
import { Modal } from './Modal';

export const EmailChangeModal = ({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) => {
  const [newEmail, setNewEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async () => {
    try {
      const verificationCode = await authService.changeEmail(newEmail, password);
      console.log("Success! Code received:", verificationCode);
      onClose();
    } catch (err) {
      alert("Something went wrong. Check the console for CORS or Auth errors.");
    }
  };

  if (!isOpen) return null;

  return (
    <Modal title="Change Email" isOpen={isOpen} onClose={onClose}>
      <div className="space-y-4">
        <div>
          <label className="text-gray-50 text-sm block mb-2">New Email Address</label>
          <input
            type="email"
            className="w-full bg-gray-90 border border-gray-80 rounded-xl p-3 text-white focus:border-primary-0 outline-none"
            placeholder="example@email.com"
            value={newEmail}
            onChange={(e) => setNewEmail(e.target.value)}
          />
        </div>
        <div>
          <label className="text-gray-50 text-sm block mb-2">Confirm with Password</label>
          <input
            type="password"
            className="w-full bg-gray-90 border border-gray-80 rounded-xl p-3 text-white focus:border-primary-0 outline-none"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
      </div>
    </Modal>
  );
};