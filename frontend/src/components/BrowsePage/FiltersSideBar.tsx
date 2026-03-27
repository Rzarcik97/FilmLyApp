import { useState } from 'react'
import { FilterSection } from '../../types/enums';
import { ListFilter, ChevronDown, ChevronUp } from 'lucide-react';
import * as Slider from '@radix-ui/react-slider';
import type { RangeState } from '../../types';

interface FiltersSideBarProps {
  title: string;
}

export const FiltersSideBar = ({ title }: FiltersSideBarProps) => {
  const [openedSection, setOpenedSection] = useState<string[]>([]);
  const [imdbRange, setImdbRange] = useState<RangeState>({ min: 7.7, max: 10 });
  const [voteRange, setVoteRange] = useState<RangeState>({ min: 4.3, max: 5.9 });

  const isSectionOpened = (section: FilterSection) => openedSection.includes(section);

  const toggleSection = (sectionName: string) => {
    setOpenedSection((prev) =>
      prev.includes(sectionName)
        ? prev.filter(name => name !== sectionName)
        : [...prev, sectionName]
    );
  }

  const handleRangeChange = (
    val: number[],
    setter: React.Dispatch<React.SetStateAction<RangeState>>
  ) => {
    const [min = 0, max = 10] = val;
    setter({ min, max });
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    type: 'min' | 'max',
    setter: React.Dispatch<React.SetStateAction<RangeState>>
  ) => {
    let val = parseFloat(e.target.value);
    if (isNaN(val)) val = 0;
    if (val < 0) val = 0;
    if (val > 10) val = 10;

    setter(prev => ({ ...prev, [type]: val }));
  };

  return (
    <div className='w-[328px] py-12 px-6 flex flex-col bg-light-background'>
      <h2 className="text-[24px] font-semibold h-18 pb-12">{title}</h2>
      <div className="h-21 flex justify-between items-center border-b border-primary-background w-full">
        <p className="text-[24px] font-semibold">Filters</p>
        <span className="bg-primary-background w-[27px] h-[27px] rounded-full flex justify-center items-center">
          <ListFilter size={18} />
        </span>
      </div>

      <div className="flex flex-col gap-6 py-6">
        <div className="flex justify-between items-center px-2 border border-light-border-2 h-14">
          <p>{FilterSection.HideWatched}</p>
          <label className="relative inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              className="sr-only peer"
            // onChange
            />

            <div className="w-[30px] h-[16px] bg-primary-background rounded-full peer border border-primary-background
              peer-checked:bg-white 
              after:content-[''] after:absolute after:top-[2px] after:left-[2px] 
              after:bg-toggle-button after:rounded-full after:h-[12px] after:w-[12px] 
              after:transition-all peer-checked:after:translate-x-[14px]">
            </div>
          </label>
        </div>
        <div className="flex justify-between items-center px-2 h-14">
          <p>{FilterSection.SortBy}</p>
          <button className="cursor-pointer">
            <ChevronDown size={24} />
          </button>
        </div>
        <div className="flex justify-between items-center px-2 h-14">
          <p>{FilterSection.Genres}</p>
          <button className="cursor-pointer">
            <ChevronDown size={24} />
          </button>
        </div>
        <div className="flex justify-between items-center px-2 h-14">
          <p>{FilterSection.Country}</p>
          <button className="cursor-pointer">
            <ChevronDown size={24} />
          </button>
        </div>
        <div className="flex justify-between items-center px-2 h-14">
          <p>{FilterSection.Year}</p>
          <button className="cursor-pointer">
            <ChevronDown size={24} />
          </button>
        </div>
        <div className="flex justify-between items-center px-2 h-14">
          <p>{FilterSection.Cast}</p>
          <button className="cursor-pointer">
            <ChevronDown size={24} />
          </button>
        </div>
        <div className="flex justify-between items-center px-2 h-14">
          <p>{FilterSection.Rating}</p>
          <button
            className="cursor-pointer"
            onClick={() => toggleSection(FilterSection.Rating)}
          >
            {isSectionOpened(FilterSection.Rating) ? (
              <ChevronUp size={24} />
            ) : (
              <ChevronDown size={24} />
            )}
          </button>
        </div>
        {isSectionOpened(FilterSection.Rating) && (
          <div>
            <div className="border-b border-b-2 border-primary-background pb-4">
              <div className="flex justify-between items-center px-4 pb-[27px]">
                <span className="text-[18px] font-medium">IMDB</span>
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
                  value={imdbRange.min}
                  onChange={(e) => handleInputChange(e, 'min', setImdbRange)}
                  className="flex-1 min-w-0 h-[34px] border border-input-border bg-transparent text-left py-[5px] px-[10px] focus:outline-none focus:border-check-button focus:border-2"
                />
                <div className="h-[1px] w-[35px] bg-input-border"></div>
                <input
                  type="number"
                  placeholder="10"
                  min="0"
                  max="10"
                  step="0.1"
                  value={imdbRange.max}
                  onChange={(e) => handleInputChange(e, 'max', setImdbRange)}
                  className="flex-1 min-w-0 h-[34px] border border-input-border bg-transparent text-left py-[5px] px-[10px] focus:outline-none focus:border-check-button focus:border-2"
                />
              </div>

              <Slider.Root
                className="relative flex items-center select-none touch-none w-full h-8"
                value={[imdbRange.min, imdbRange.max]}
                max={10}
                step={0.1}
                onValueChange={(vals) => handleRangeChange(vals, setImdbRange)}
              >
                <Slider.Track className="bg-input-range relative grow rounded-full h-[6px]">
                  <Slider.Range className="absolute bg-check-button rounded-full h-full" />
                </Slider.Track>

                <Slider.Thumb className="block w-5 h-5 bg-check-button border-2 border-input-border-2 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />

                <Slider.Thumb className="block w-5 h-5 bg-check-button border-2 border-input-border-2 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />
              </Slider.Root>
            </div>

            <div className="border-b border-b-2 border-primary-background py-4">
              <div className="flex justify-between items-center px-4 pb-[27px]">
                <span className="text-[18px] font-medium">Vote Rating</span>
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
                  value={voteRange.min}
                  onChange={(e) => handleInputChange(e, 'min', setVoteRange)}
                  className="flex-1 min-w-0 h-[34px] border border-input-border bg-transparent text-left py-[5px] px-[10px] focus:outline-none focus:border-check-button focus:border-2"
                />
                <div className="h-[1px] w-[35px] bg-input-border"></div>
                <input
                  type="number"
                  placeholder="10"
                  min="0"
                  max="10"
                  step="0.1"
                  value={voteRange.max}
                  onChange={(e) => handleInputChange(e, 'max', setVoteRange)}
                  className="flex-1 min-w-0 h-[34px] border border-input-border bg-transparent text-left py-[5px] px-[10px] focus:outline-none focus:border-check-button focus:border-2"
                />
              </div>

              <Slider.Root
                className="relative flex items-center select-none touch-none w-full h-8"
                value={[voteRange.min, voteRange.max]}
                max={10}
                step={0.1}
                onValueChange={(vals) => handleRangeChange(vals, setVoteRange)}
              >
                <Slider.Track className="bg-input-range relative grow rounded-full h-[6px]">
                  <Slider.Range className="absolute bg-check-button rounded-full h-full" />
                </Slider.Track>

                <Slider.Thumb className="block w-5 h-5 bg-check-button border-2 border-input-border-2 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />

                <Slider.Thumb className="block w-5 h-5 bg-check-button border-2 border-input-border-2 rounded-full cursor-pointer hover:scale-110 transition-transform focus:outline-none shadow-sm" />
              </Slider.Root>
            </div>
          </div>          
        )}
      </div>
    </div>
  )
}