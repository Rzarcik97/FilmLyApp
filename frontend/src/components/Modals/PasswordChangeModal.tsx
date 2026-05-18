import { authService } from '../../api/authService';
import { Modal } from './Modal';
import { useForm } from 'react-hook-form';
import { type PasswordChangeFormData, passwordChangeSchema } from '../../utils/validationSchemas';
import { zodResolver } from '@hookform/resolvers/zod';

export const PasswordChangeModal = ({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset
  } = useForm<PasswordChangeFormData>({
    resolver: zodResolver(passwordChangeSchema),
    mode: 'onTouched',
  })

  const onFormSubmit = async (data: PasswordChangeFormData) => {
    try {
      const code = await authService.changePassword(data.oldPassword, data.newPassword);
      console.log("Verification code sent:", code);
      reset();
      onClose();
    } catch (error: any) {
      console.error('Password change failed', error);
    }
  };

  if (!isOpen) return null;

  return (
    <Modal
      title="Change Password"
      isOpen={isOpen}
      onClose={onClose}
      formId="password-change-form"
    >
      <form id="password-change-form" onSubmit={handleSubmit(onFormSubmit)} className="space-y-4">
        <div>
          <label className="text-gray-50 text-sm block mb-2">Current Password</label>
          <input
            {...register('oldPassword')}
            type="password"
            placeholder="••••••••••••"
            className={`w-full bg-gray-90 border rounded-xl p-3 text-white outline-none focus:border-primary-0 ${errors.oldPassword ? 'border-system-error' : 'border-gray-80'}`}
          />
          {errors.oldPassword && (
            <p className="text-system-error text-xs mt-1">{errors.oldPassword.message}</p>
          )}
        </div>
        <div>
          <label className="text-gray-50 text-sm block mb-2">New Password</label>
          <input
            {...register('newPassword')}
            type="password"
            placeholder="••••••••••••"
            className={`w-full bg-gray-90 border rounded-xl p-3 text-white outline-none focus:border-primary-0 ${errors.newPassword ? 'border-system-error' : 'border-gray-80'}`}
          />
          {errors.newPassword && (
            <p className="text-system-error text-xs mt-1">{errors.newPassword.message}</p>
          )}
        </div>
        <div>
          <label className="text-gray-50 text-sm block mb-2">Confirm New Password</label>
          <input
            {...register('confirmNewPassword')}
            type="password"
            placeholder="••••••••••••"
            className={`w-full bg-gray-90 border rounded-xl p-3 text-white outline-none focus:border-primary-0 ${errors.confirmNewPassword ? 'border-system-error' : 'border-gray-80'}`}
          />
          {errors.confirmNewPassword && (
            <p className="text-system-error text-xs mt-1">{errors.confirmNewPassword.message}</p>
          )}
        </div>
      </form>
    </Modal>
  );
};