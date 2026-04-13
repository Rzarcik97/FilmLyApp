import { useState } from 'react'
import { FilterSection } from '../../types/enums';
import { ListFilter, ChevronDown, ChevronUp } from 'lucide-react';
import { RangeFilter } from './RangeFilter';
import type { RangeState } from '../../types';
import filter from '../../../public/icons/filter.png';

interface FiltersSideBarProps {
  title: string;
}

export const FiltersSideBar = ({ title }: FiltersSideBarProps) => {
  const [openedSection, setOpenedSection] = useState<FilterSection[]>([]);
  const [imdbRange, setImdbRange] = useState<RangeState>({ min: 7.7, max: 10 });
  const [voteRange, setVoteRange] = useState<RangeState>({ min: 4.3, max: 5.9 });

  const isSectionOpened = (section: FilterSection) => openedSection.includes(section);

  const toggleSection = (sectionName: FilterSection) => {
    setOpenedSection((prev) =>
      prev.includes(sectionName)
        ? prev.filter(name => name !== sectionName)
        : [...prev, sectionName]
    );
  };

  const sections = [
    FilterSection.SortBy,
    FilterSection.Genres,
    FilterSection.Country,
    FilterSection.Year,
    FilterSection.Cast
  ]

  return (
    <div className="w-[328px] py-12 px-6 flex flex-col bg-gray-100 rounded-[16px]
          border border-gray-30/10 backdrop-blur-[2px]
          before:content-[''] before:absolute before:inset-0
          before:rounded-[16px] before:border before:border-gray-80/20
          before:pointer-events-none
          cursor-pointer
    ">
      <h2 className="text-[36px] leading-[1.2] text-gray-0 font-semibold h-18 pb-12">{title}</h2>
      <div className="h-14 flex justify-between items-center border-b border-gray-90 w-full">
        <p className="text-[20px] leading-[1.45] text-gray-0 font-semibold font-nunito">Sort by</p>
        <img src={filter} alt="Filters" className="w-6 h-6" />
      </div>

      <div className="flex flex-col gap-6 py-6 font-nunito">
        <div className="flex justify-between items-center px-2 h-14 bg-secondary-dark rounded-[8px] font-bold text-gray-0 text-[16px] leading-[1
        35]">
          <p>{FilterSection.HideWatched}</p>
          <label className="relative inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              className="sr-only peer"
            // onChange
            />

            <div className="
              w-[30px] h-[16px] bg-gray-50 rounded-full peer border border-gray-50
              peer-checked:bg-primary-0 relative transition-colors duration-300

              after:content-[''] after:absolute after:top-[1px] after:left-[1px] 
              after:h-[12px] after:w-[12px] after:rounded-full
  
              after:bg-secondary-light after:backdrop-blur-[2px] 
              after:shadow-[0_0_0_0.5px_rgba(21,20,18,0.2),0_1px_2px_rgba(0,0,0,0.3)]
  
              after:transition-all after:duration-300 after:ease-in-out
              peer-checked:after:translate-x-[14px]
            ">
            </div>
          </label>
        </div>

        {sections.map((section) => (
          <div key={section} className="flex flex-col border-b border-gray-90">
            <div className="flex justify-between items-center px-2 h-14">
              <p className="text-gray-0 text-[16px] leading-[1.35] font-bold font-nunito">{section}</p>
              <button
                className="cursor-pointer text-gray-70"
                onClick={() => toggleSection(section)}
              >
                {isSectionOpened(section) ? (
                  <ChevronUp size={24} />
                ) : (
                  <ChevronDown size={24} />
                )}
              </button>
            </div>
          </div>
        ))}
        <div className="flex justify-between items-center px-2 mt-2 h-14 border-t border-gray-70 text-[16px] text-gray-0 leading-[1.35] font-bold font-nunito">
          <p>{FilterSection.Rating}</p>
          <button
            className="cursor-pointer text-gray-70"
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
            <RangeFilter 
              label="IMDB"
              range={imdbRange}
              setRange={setImdbRange}
            />
            <RangeFilter 
              label="Vote Rating"
              range={voteRange}
              setRange={setVoteRange}
            />
          </div>          
        )}
      </div>
    </div>
  )
}