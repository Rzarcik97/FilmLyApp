import { z } from 'zod';

export const signUpSchema = z.object({
  name: z
    .string()
    .min(2, 'Name must be at least 2 characters'),
  email: z
    .string()
    .min(1, 'Email is required')
    .email({ message: 'Please enter a valid email address (e.g., name@domain.com)'}),
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

export const loginSchema = z.object({
  email: z
    .string()
    .min(1, 'Email is required')
    .email({ message: 'Please enter a valid email address' }),
  password: z
    .string()
    .min(1, 'Password is required')
    // .min(8, 'Password length should be at least 8 characters'),
});

export type LoginFormData = z.infer<typeof loginSchema>;