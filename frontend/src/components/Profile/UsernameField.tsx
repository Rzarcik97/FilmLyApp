import { useState } from 'react';
import { authService } from '../../api/authService';
import type { UserProfile } from '../../types/auth';

export const UsernameField = ({ initialName, user }: { initialName: string, user: UserProfile }) => {
  const [name, setName] = useState(initialName);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const isChanged = name !== initialName && name.trim() !== '';

  const handleSave = async () => {
    setIsLoading(true);
    setError(null);

    try {
      await authService.updateUserName(name, user);
      console.log("Username updated!");
    } catch (error: any) {
      setError(error.response?.data?.message || 'Failed to update username');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative w-full group">
      <input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="Enter username"
        className="w-full px-4 py-1.5 md:py-3 bg-gray-90 rounded-[8px] text-gray-0 
                   placeholder:text-gray-80 text-[16px] md:text-[20px] outline-none 
                   transition-all duration-200 border border-transparent focus:border-primary-0 pr-20"
      />

      {isChanged && (
        <button
          onClick={handleSave}
          disabled={isLoading}
          className="absolute right-2 top-1/2 -translate-y-1/2 bg-primary-0 text-gray-dark 
                     px-4 py-1 rounded-md text-sm font-bold hover:brightness-110 
                     transition-all animate-in fade-in zoom-in duration-200"
        >
          {isLoading ? '...' : 'Save'}
        </button>
      )}
    </div>
  );
};