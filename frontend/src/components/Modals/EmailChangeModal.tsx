import { useState } from 'react';
import { authService } from '../../api/authService';
import { Modal } from './Modal';
import { useForm } from 'react-hook-form';
import { type EmailChangeFormData, emailChangeSchema } from '../../utils/validationSchemas';
import { zodResolver } from '@hookform/resolvers/zod';

export const EmailChangeModal = ({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset
  } = useForm<EmailChangeFormData>({
    resolver: zodResolver(emailChangeSchema),
    mode: 'onTouched',
  });

  const onFormSubmit = async (data: EmailChangeFormData) => {
    try {
      const verificationCode = await authService.changeEmail(data.newEmail, data.currentPassword);
      console.log('Success! Code received!', verificationCode);
      reset();
      onClose();
    } catch (error: any) {
      console.error('Email change failed', error);
    }
  };

  if (!isOpen) return null;

  return (
    <Modal title="Change Email" isOpen={isOpen} onClose={onClose} formId="email-change-form">
      <form
        id="email-change-form"
        onSubmit={handleSubmit(onFormSubmit)}
        className="space-y-4"
      >
        <div>
          <label className="text-gray-50 text-sm block mb-2">New Email Address</label>
          <input
            {...register('newEmail')}
            type="email"
            className={`w-full bg-gray-90 border rounded-xl p-3 text-white outline-none focus:border-primary-0 ${errors.newEmail ? 'border-system-error' : 'border-gray-80'}`}
            placeholder="example@email.com"
          />
          {errors.newEmail && (
            <p className="text-system-error text-xs mt-1">{errors.newEmail.message}</p>
          )}
        </div>
        <div>
          <label className="text-gray-50 text-sm block mb-2">Confirm with Password</label>
          <input
            {...register('currentPassword')}
            type="password"
            className={`w-full bg-gray-90 border rounded-xl p-3 text-white outline-none focus:border-primary-0 ${errors.currentPassword ? 'border-system-error' : 'border-gray-80'}`}
            placeholder="••••••••"
          />
          {errors.currentPassword && (
            <p className="text-system-error text-xs mt-1">{errors.currentPassword.message}</p>
          )}
        </div>
      </form>
    </Modal>
  );
};