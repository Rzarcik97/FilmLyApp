import React, { forwardRef } from 'react';
import { CheckIcon } from './icons/CheckIcon';

interface CheckboxProps extends React.InputHTMLAttributes<HTMLInputElement> { }

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(
  ({ ...props }, ref) => {
    return (
      <div className="relative flex items-center justify-center flex-shrink-0">
        <input
          type="checkbox"
          ref={ref}
          className="peer sr-only"
          {...props}
        />

        <div className="w-5 h-5 rounded-[5px] border-2 border-gray-70 
          bg-transparent transition-all duration-200
          peer-checked:border-gray-70
          peer-hover:border-gray-50" />

        <CheckIcon
          size={14}
          color="currentColor"
          className="absolute text-primary-0 opacity-0 transition-opacity duration-200 peer-checked:opacity-100 pointer-events-none"
        />
      </div>
    );
  }
);

Checkbox.displayName = 'Checkbox';