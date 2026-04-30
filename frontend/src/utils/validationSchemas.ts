import { z } from 'zod';

export const signUpSchema = z.object({
  name: z
    .string()
    .min(2, 'Name must be at least 2 characters'),
  email: z
    .string()
    .min(1, 'Email is required')
    .email({ message: 'Please enter a valid email address (e.g., name@domain.com)' }),
  isAgreedToTerms: z.literal(true, {
    error: 'You must accept the Terms and Privacy Policy',
  }),
  isAgreedToPromotions: z.boolean().optional(),
});

export type SignUpFormData = z.infer<typeof signUpSchema>;

export const createPasswordSchema = z.object({
  password: z
    .string()
    .min(8, 'Password must be at least 8 characters'),
  // .regex(/[A-Z]/, 'Must contain at least one uppercase letter')
  // .regex(/[0-9]/, 'Must contain at least one number'),
  // maybe later I will add those strict rules for the password
  confirmPassword: z.string().min(1, 'Please confirm your password'),
})
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Passwords do not match',
    path: ['confirmPassword'],
  });

export type CreatePasswordFormData = z.infer<typeof createPasswordSchema>;

const emailValidation = z
  .string()
  .min(1, 'Email is required')
  .email('Invalid email format');

export const loginSchema = z.object({
  email: emailValidation,
  password: z.string().min(1, 'Password is required'),
});

export const emailChangeSchema = z.object({
  newEmail: emailValidation,
  currentPassword: z.string().min(1, 'Password is required to confirm the change'),
});

export type LoginFormData = z.infer<typeof loginSchema>;
export type EmailChangeFormData = z.infer<typeof emailChangeSchema>;

export const passwordChangeSchema = z.object({
  oldPassword: z
    .string()
    .min(1, 'Current password is required'),
  newPassword: z
    .string()
    .min(8, 'New password must be at least 8 characters'),
  confirmNewPassword: z
    .string()
    .min(1, 'Please confirm your new password'),
})
  .refine((data) => data.newPassword === data.confirmNewPassword, {
    message: 'Passwords do not match',
    path: ['confirmNewPassword'],
  });

export type PasswordChangeFormData = z.infer<typeof passwordChangeSchema>;