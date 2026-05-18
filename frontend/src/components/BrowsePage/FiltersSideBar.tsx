import React, { useEffect, useState } from 'react'
import { FilterSection } from '../../types/enums';
import { ChevronDown, ChevronUp, ChevronRight } from 'lucide-react';
import { RangeFilter } from './RangeFilter';
import type { Genre, RangeState } from '../../types';
import filter from '/icons/filter.png';
import { getGenres } from '../../api/movieService';
import { Checkbox } from '../../assets/CheckBox';
import X from '/icons/mobile/x.svg';
import { useDispatch, useSelector } from 'react-redux';
import { type AppDispatch, type RootState } from '../../store';
import { setDateSort, setHideWatched, setImdbRange, setTitleSort, toggleCountry, toggleGenre, toggleImdbActive } from '../../store/filtersSlice';
import { Toast } from '../Profile/Toast';

interface FiltersSideBarProps {
  title: string;
  setIsSidebarOpen: (open: boolean) => void;
}

export const toggleItemInArray = <T,>(array: T[], item: T): T[] => {
  return array.includes(item)
    ? array.filter((i) => i !== item)
    : [...array, item];
};

const COUNTRY_SHORT_NAMES: Record<string, string> = {
  'United States of America': 'USA',
  'United Kingdom': 'UK',
};

export const getListLabel = <T,>(
  selectedItems: T[],
  displayValueGetter: (item: T) => string,
  defaultLabel = 'All'
): string => {
  if (selectedItems.length === 0) return defaultLabel;

  const firstItemName = displayValueGetter(selectedItems[0] as T);

  return selectedItems.length > 1
    ? `${firstItemName}, ...`
    : firstItemName;
};

const dateSortOptions = [
  { label: 'Default', value: 'default', display: 'Default' },
  { label: 'Newest to Oldest', value: 'newest', display: 'By newest' },
  { label: 'Oldest to Newest', value: 'oldest', display: 'By oldest' },
] as const;

const titleSortOptions = [
  { label: 'Default', value: 'default', display: 'Default' },
  { label: 'Title (A-Z)', value: 'asc', display: 'A-Z' },
  { label: 'Title (Z-A)', value: 'desc', display: 'Z-A' },
] as const;

export const FiltersSideBar = ({
  title,
  setIsSidebarOpen
}: FiltersSideBarProps) => {
  const dispatch = useDispatch<AppDispatch>();
  const filters = useSelector((state: RootState) => state.filters);

  const [openedSection, setOpenedSection] = useState<FilterSection[]>([]);
  const [voteRange, setVoteRange] = useState<RangeState>({ min: 0, max: 10 });
  const [allGenres, setAllGenres] = useState<Genre[]>([]);

  const allCountries = ['United States of America', 'South Korea', 'India', 'China', 'United Kingdom', 'France', 'Japan', 'Spain', 'Germany', 'Mexico'];

  const isSectionOpened = (section: FilterSection) => openedSection.includes(section);

  const toggleSection = (sectionName: FilterSection) => {
    setOpenedSection((prev) =>
      prev.includes(sectionName)
        ? prev.filter(name => name !== sectionName)
        : [...prev, sectionName]
    );
  };

  useEffect(() => {
    const fetchGenres = async () => {
      const genres = await getGenres();
      setAllGenres(genres);
    };

    fetchGenres();
  }, []);

  const getGenresLabel = () => {
    const selectedGenres = allGenres.filter(g => filters.selectedGenreIds.includes(g.id));

    return getListLabel(
      selectedGenres,
      (genre) => genre.name
    );
  };

  const getCountriesLabel = () => {
    return getListLabel(
      filters.selectedCountries,
      (country) => COUNTRY_SHORT_NAMES[country] || country
    );
  };

  const getDateLabel = () => {
    const activeOption = dateSortOptions.find(opt => opt.value === filters.dateSort);
    return activeOption ? activeOption.display : 'Default';
  };

  const getTitleLabel = () => {
    const active = titleSortOptions.find(opt => opt.value === filters.titleSort);
    return active ? active.display : 'Default';
  };

  const sections = [
    FilterSection.Genres,
    FilterSection.Country,
    FilterSection.Year,
    FilterSection.Title,
  ];

  const sectionLabels: Record<string, string> = {
    [FilterSection.Genres]: getGenresLabel() as string,
    [FilterSection.Country]: getCountriesLabel(),
    [FilterSection.Year]: getDateLabel(),
    [FilterSection.Title]: getTitleLabel(),
  };

  return (
    <div className="w-[328px] py-2 px-6 flex flex-col bg-gray-100/90 rounded-r-[16px]
          border border-gray-70/10 backdrop-blur-[2px]
          before:content-[''] before:absolute before:inset-0
          before:rounded-r-[16px] before:border before:border-gray-70/20
          before:pointer-events-none
          cursor-pointer z-100 backdrop-saturate-[150%]
          max-h-[90vh] overflow-y-auto md:max-h-none md:overflow-visible
    ">
      <div className="md:hidden block text-right pb-6 pt-4">
        <button onClick={() => setIsSidebarOpen(false)}>
          <img src={X} alt="Close the filtration menu" className="w-8 h-8" />
        </button>
      </div>
      <h2 className="text-[32px] leading-[1.2] text-gray-0 font-semibold h-18 pb-12">{title}</h2>
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
              checked={filters.hideWatched}
              onChange={(e) => dispatch(setHideWatched(e.target.checked))}
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

        <div className="flex flex-col gap-6">
          {sections.map((section) => (
            <React.Fragment key={section}>
              <div className="">
                <div className="flex justify-between items-center px-2 h-14 border-b border-gray-90">
                  <p className="text-gray-0 text-[16px] leading-[1.35] font-bold font-nunito">{section}</p>
                  <button
                    className="cursor-pointer text-gray-70"
                    onClick={() => toggleSection(section)}
                  >
                    {isSectionOpened(section) ? (
                      <ChevronUp size={24} />
                    ) : (
                      <div className="flex gap-2 text-gray-70 text-[16px] leading-[1.35] items-center">
                        <span>{sectionLabels[section] || 'All'}</span>
                        <ChevronRight size={24} />
                      </div>
                    )}
                  </button>
                </div>
              </div>

              {section === FilterSection.Genres && isSectionOpened(section) && (
                <div className="flex flex-col py-4 gap-4 animate-in fade-in slide-in-from-top-2">
                  {allGenres.map((genre) => {
                    const isSelected = filters.selectedGenreIds.includes(genre.id);

                    return (
                      <label
                        key={genre.id}
                        className="flex justify-between items-center px-4 py-1 hover:bg-gray-90/50 transition-colors group cursor-pointer"
                      >
                        <span className={`text-[15px] font-nunito transition-colors ${isSelected ? 'text-primary-20' : 'text-gray-30'}`}>
                          {genre.name}
                        </span>
                        <Checkbox
                          checked={isSelected}
                          onChange={() => dispatch(toggleGenre(genre.id))}
                        />
                      </label>
                    );
                  })}
                </div>
              )}

              {section === FilterSection.Country && isSectionOpened(section) && (
                <div className="flex flex-col py-4 gap-4 animate-in fade-in slide-in-from-top-2">
                  {allCountries.map((country) => {
                    const isSelected = filters.selectedCountries.includes(country);

                    return (
                      <label
                        key={country}
                        className="flex justify-between items-center px-4 py-1 hover:bg-gray-90/50 transition-colors group cursor-pointer"
                      >
                        <span className={`text-[15px] font-nunito transition-colors ${isSelected ? 'text-primary-20' : 'text-gray-30'}`}>
                          {country}
                        </span>
                        <Checkbox
                          checked={isSelected}
                          onChange={() => dispatch(toggleCountry(country))}
                        />
                      </label>
                    );
                  })}
                </div>
              )}

              {section === FilterSection.Year && isSectionOpened(section) && (
                <div className="flex flex-col py-4 gap-4 animate-in fade-in slide-in-from-top-2">
                  {dateSortOptions.map((option) => {
                    const isSelected = filters.dateSort === option.value;

                    return (
                      <label
                        key={option.value}
                        className="flex justify-between items-center px-4 py-1 hover:bg-gray-90/50 transition-colors group cursor-pointer"
                      >
                        <span className={`text-[15px] font-nunito transition-colors ${isSelected ? 'text-primary-20' : 'text-gray-30'}`}>
                          {option.label}
                        </span>
                        <Checkbox
                          checked={isSelected}
                          onChange={() => dispatch(setDateSort(option.value))}
                        />
                      </label>
                    );
                  })}
                </div>
              )}

              {section === FilterSection.Title && isSectionOpened(section) && (
                <div className="flex flex-col py-4 gap-4 animate-in fade-in slide-in-from-top-2">
                  {titleSortOptions.map((option) => {
                    const isSelected = filters.titleSort === option.value;

                    return (
                      <label
                        key={option.value}
                        className="flex justify-between items-center px-4 py-1 hover:bg-gray-90/50 transition-colors group cursor-pointer"
                      >
                        <span className={`text-[15px] font-nunito transition-colors ${isSelected ? 'text-primary-20' : 'text-gray-30'}`}>
                          {option.label}
                        </span>
                        <Checkbox
                          checked={isSelected}
                          onChange={() => dispatch(setTitleSort(option.value))}
                        />
                      </label>
                    );
                  })}
                </div>
              )}
            </React.Fragment>
          ))}
        </div>

        <div className="flex justify-between items-center px-2 h-14 text-[16px] text-gray-0 leading-[1.35] font-bold font-nunito">
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
              range={{ min: filters.imdbRange[0] ?? 0, max: filters.imdbRange[1] ?? 0 }}
              setRange={(val: React.SetStateAction<RangeState>) => {
                const newRange = typeof val === 'function'
                  ? val({ min: filters.imdbRange[0] ?? 0, max: filters.imdbRange[1] ?? 10 })
                  : val;

                dispatch(setImdbRange([newRange.min, newRange.max]));
              }}
              isActive={filters.isImdbActive}
              onToggle={() => dispatch(toggleImdbActive())}
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