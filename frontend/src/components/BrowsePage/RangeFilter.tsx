import * as Slider from '@radix-ui/react-slider';
import type { RangeState } from '../../types';

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
    <div>
      <div className="border-b border-b-2 border-primary-background pb-4">
        <div className="flex justify-between items-center px-4 pb-[27px]">
          <span className="text-[18px] font-medium">{label}</span>
          <label className="flex items-center gap-2 cursor-pointer group">
            <span className="text-[16px]">Sort by</span>
            <input
              type="checkbox"
              className="w-4 h-4 border-1 border-black cursor-pointer accent-check-button"
            />
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
            className="flex-1 min-w-0 h-[34px] border border-input-border bg-transparent text-left py-[5px] px-[10px] focus:outline-none focus:border-check-button focus:border-2"
          />
          <div className="h-[1px] w-[35px] bg-input-border"></div>
          <input
            type="number"
            placeholder="10"
            min="0"
            max="10"
            step="0.1"
            value={range.max}
            onChange={(e) => handleInputChange(e, 'max')}
            className="flex-1 min-w-0 h-[34px] border border-input-border bg-transparent text-left py-[5px] px-[10px] focus:outline-none focus:border-check-button focus:border-2"
          />
        </div>

        <Slider.Root
          className="relative flex items-center select-none touch-none w-full h-8"
          value={[range.min, range.max]}
          max={10}
          step={0.1}
          onValueChange={handleRangeChange}
        >
          <Slider.Track className="bg-input-range relative grow rounded-full h-[6px]">
            <Slider.Range className="absolute bg-check-button rounded-full h-full" />
          </Slider.Track>

          <Slider.Thumb className="block w-5 h-5 bg-check-button border-2 border-input-border-2 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />

          <Slider.Thumb className="block w-5 h-5 bg-check-button border-2 border-input-border-2 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />
        </Slider.Root>
      </div>
    </div>
  )
}