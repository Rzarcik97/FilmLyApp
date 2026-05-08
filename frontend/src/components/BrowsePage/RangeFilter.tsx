import * as Slider from '@radix-ui/react-slider';
import type { RangeState } from '../../types';
import { Checkbox } from '../../assets/CheckBox';
import { useRef, useState } from 'react';

interface RangeFilterProps {
  label: string;
  range: RangeState;
  setRange: React.Dispatch<React.SetStateAction<RangeState>>;
  isActive?: boolean;
  onToggle?: () => void;
}

export const RangeFilter = ({ label, range, setRange, isActive, onToggle }: RangeFilterProps) => {
  const minInputRef = useRef<HTMLInputElement>(null);
  const maxInputRef = useRef<HTMLInputElement>(null);

  const [activeSide, setActiveSide] = useState<'min' | 'max' | null>(null);

  const handleRangeChange = (val: number[]) => {
    const [newMin, newMax] = val;

    if (newMin !== range.min) setActiveSide('min');
    else if (newMax !== range.max) setActiveSide('max');

    setRange({ min: newMin ?? 0, max: newMax ?? 10 });
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    type: 'min' | 'max',
  ) => {
    let val = parseFloat(e.target.value);
    if (isNaN(val)) val = 0;
    val = Math.max(0, Math.min(10, val));

    setActiveSide(type);
    setRange(prev => ({ ...prev, [type]: val }));
  };

  return (
    <div className="border border-gray-90 mb-2 bg-secondary-dark rounded-[8px] w-70 h-[195px]">
      <div className="flex flex-col gap-7">
        <div className="flex justify-between items-center px-2 py-4 gap-7">
          <span className="text-[16px] text-gray-0 leading-[1.35] font-bold font-nunito">{label}</span>
          <label className="flex items-center gap-2 cursor-pointer group">
            <span className="text-[16px] text-gray-70 leading-[1.35] font-nunito">Sort by</span>
            <Checkbox
              checked={isActive}
              onChange={onToggle}
            />
          </label>
        </div>

        <div className="flex items-center gap-0 px-[7px]">
          <input
            ref={minInputRef}
            type="number"
            min="0"
            max="10"
            step="0.1"
            value={range.min}
            onChange={(e) => handleInputChange(e, 'min')}
            onFocus={() => setActiveSide('min')}
            className={`flex-1 min-w-0 h-[34px] rounded-[8px] border bg-transparent text-left text-[16px] leading-[1.5] font-nunito py-[5px] px-[10px] focus:outline-none transition-colors ${activeSide === 'min' ? 'border-primary-0 border-2 text-white' : 'border-gray-30 text-gray-30'
              }`}
          />
          <div className="h-[1px] w-[35px] bg-gray-30"></div>
          <input
            ref={maxInputRef}
            type="number"
            min="0"
            max="10"
            step="0.1"
            value={range.max}
            onChange={(e) => handleInputChange(e, 'max')}
            onFocus={() => setActiveSide('max')}
            className={`flex-1 min-w-0 h-[34px] rounded-[8px] border bg-transparent text-left text-[16px] leading-[1.5] font-nunito py-[5px] px-[10px] focus:outline-none transition-colors ${activeSide === 'max' ? 'border-primary-0 border-2 text-white' : 'border-gray-30 text-gray-30'
              }`}
          />
        </div>

        <Slider.Root
          className="relative flex items-center select-none touch-none w-full h-8 px-1"
          value={[range.min, range.max]}
          max={10}
          step={0.1}
          onValueChange={handleRangeChange}
        >
          <Slider.Track className="bg-secondary-light relative grow rounded-full h-[6px]">
            <Slider.Range className="absolute bg-primary-0 rounded-full h-full" />
          </Slider.Track>

          <Slider.Thumb
            onPointerDown={() => minInputRef.current?.focus()}
            className={`block w-5 h-5 border-2 border-primary-0 rounded-full cursor-pointer hover:scale-110 transition-all focus:outline-none shadow-sm ${activeSide === 'min' ? 'bg-secondary-light' : 'bg-primary-0'
              }`}
          />

          <Slider.Thumb
            onPointerDown={() => maxInputRef.current?.focus()}
            className={`block w-5 h-5 border-2 border-primary-0 rounded-full cursor-pointer hover:scale-110 transition-all focus:outline-none shadow-sm ${activeSide === 'max' ? 'bg-secondary-light' : 'bg-primary-0'
              }`}
          />
        </Slider.Root>
      </div>
    </div>
  )
}