import { useState } from 'react';
import { authService } from '../../api/authService';
import { Modal } from './Modal';

export const PasswordChangeModal = ({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) => {
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleSubmit = async () => {
    if (newPassword !== confirmPassword) {
      alert("New passwords do not match!");
      return;
    }

    try {
      const code = await authService.changePassword(oldPassword, newPassword);
      console.log("Verification code sent:", code);
      onClose();
    } catch (err) {
      console.error("Password change failed", err);
    }
  };

  if (!isOpen) return null;

  return (
    <Modal title="Change Password" isOpen={isOpen} onClose={onClose}>
      <div className="space-y-4">
        <div>
          <label className="text-gray-50 text-sm block mb-2">Current Password</label>
          <input
            type="password"
            placeholder="********"
            className="w-full bg-gray-90 border border-gray-80 rounded-xl p-3 text-white focus:border-primary-0 outline-none"
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
          />
        </div>
        <div>
          <label className="text-gray-50 text-sm block mb-2">New Password</label>
          <input
            type="password"
            placeholder="********"
            className="w-full bg-gray-90 border border-gray-80 rounded-xl p-3 text-white focus:border-primary-0 outline-none"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </div>
        <div>
          <label className="text-gray-50 text-sm block mb-2">Confirm New Password</label>
          <input
            type="password"
            placeholder="********"
            className="w-full bg-gray-90 border border-gray-80 rounded-xl p-3 text-white focus:border-primary-0 outline-none"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </div>
        <button
          onClick={handleSubmit}
          className="w-full py-3 mt-4 rounded-full bg-primary-0 text-gray-dark font-bold hover:brightness-110 transition-all"
        >
          Update Password
        </button>
      </div>
    </Modal>
  );
};