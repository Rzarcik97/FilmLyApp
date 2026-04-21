import * as Slider from '@radix-ui/react-slider';
import type { RangeState } from '../../types';
import { CheckIcon } from 'lucide-react';

interface RangeFilterProps {
  label: string;
  range: RangeState;
  setRange: React.Dispatch<React.SetStateAction<RangeState>>;
}

export const RangeFilter = ({ label, range, setRange }: RangeFilterProps) => {
  const handleRangeChange = (val: number[]) => {
    const [min = 0, max = 10] = val;
    setRange({ min, max });
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    type: 'min' | 'max',
  ) => {
    let val = parseFloat(e.target.value);
    if (isNaN(val)) val = 0;
    val = Math.max(0, Math.min(10, val));

    setRange(prev => ({ ...prev, [type]: val }));
  };

  return (
    <div className="border border-gray-90 mb-2 bg-secondary-dark rounded-[8px] w-70 h-[195px]">
      <div className="flex flex-col gap-7">
        <div className="flex justify-between items-center px-2 py-4 gap-7">
          <span className="text-[16px] text-gray-0 leading-[1.35] font-bold font-nunito">{label}</span>
          <label className="flex items-center gap-2 cursor-pointer group">
            <span className="text-[16px] text-gray-70 leading-[1.35] font-nunito">Sort by</span>
            <div className="relative flex items-center justify-center">
              <input
                type="checkbox"
                className="peer sr-only"
              />

              <div className="w-5 h-5 rounded-[5px] border-2 border-gray-70 
                bg-transparent transition-all duration-200
                peer-checked:border-gray-70
                group-hover:border-gray-50
              ">
              </div>

              <CheckIcon
                size={14}
                color="currentColor"
                className="absolute text-primary-0 opacity-0 transition-opacity duration-200 peer-checked:opacity-100 text-primary-0"
              />
            </div>
          </label>
        </div>

        <div className="flex items-center gap-0 px-[7px]">
          <input
            type="number"
            placeholder="7.7"
            min="0"
            max="10"
            step="0.1"
            value={range.min}
            onChange={(e) => handleInputChange(e, 'min')}
            className="flex-1 min-w-0 h-[34px] rounded-[8px] border border-gray-30 bg-transparent text-left text-[16px] text-gray-30 leading-[1.5] font-nunito py-[5px] px-[10px] focus:outline-none focus:border-primary-0 focus:border-2"
          />
          <div className="h-[1px] w-[35px] bg-gray-30"></div>
          <input
            type="number"
            placeholder="10"
            min="0"
            max="10"
            step="0.1"
            value={range.max}
            onChange={(e) => handleInputChange(e, 'max')}
            className="flex-1 min-w-0 h-[34px] rounded-[8px] border border-gray-30 bg-transparent text-left text-[16px] text-gray-30 leading-[1.5] font-nunito py-[5px] px-[10px] focus:outline-none focus:border-primary-0 focus:border-2"
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

          <Slider.Thumb className="block w-5 h-5 bg-secondary-light border-2 border-primary-0 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />

          <Slider.Thumb className="block w-5 h-5 bg-primary-0 border-2 border-primary-0 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />
        </Slider.Root>
      </div>
    </div>
  )
}