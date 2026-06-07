import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

export interface FiltersState {
  selectedGenreIds: number[];
  selectedCountries: string[];
  dateSort: 'default' | 'newest' | 'oldest';
  titleSort: 'default' | 'asc' | 'desc';
  imdbRange: [number, number];
  isImdbActive: boolean;
  hideWatched: boolean;
}

const initialState: FiltersState = {
  selectedGenreIds: [],
  selectedCountries: [],
  dateSort: 'default',
  titleSort: 'default',
  imdbRange: [0, 10],
  isImdbActive: false,
  hideWatched: false,
};

const filtersSlice = createSlice({
  name: 'filters',
  initialState,
  reducers: {
    toggleGenre: (state, action: PayloadAction<number>) => {
      const id = action.payload;
      const index = state.selectedGenreIds.indexOf(id);
      if (index > -1) {
        state.selectedGenreIds.splice(index, 1);
      } else {
        state.selectedGenreIds.push(id);
      }
    },
    toggleCountry: (state, action: PayloadAction<string>) => {
      const country = action.payload;
      const index = state.selectedCountries.indexOf(country);
      if (index > -1) {
        state.selectedCountries.splice(index, 1);
      } else {
        state.selectedCountries.push(country);
      }
    },
    setDateSort: (state, action: PayloadAction<FiltersState['dateSort']>) => {
      state.dateSort = action.payload;
    },
    setTitleSort: (state, action: PayloadAction<FiltersState['titleSort']>) => {
      state.titleSort = action.payload;
    },
    setImdbRange: (state, action: PayloadAction<[number, number]>) => {
      state.imdbRange = action.payload;
    },
    toggleImdbActive: (state) => {
      state.isImdbActive = !state.isImdbActive;
    },
    setHideWatched: (state, action: PayloadAction<boolean>) => {
      state.hideWatched = action.payload;
    },
    resetFilters: () => initialState,
  },
});

export const {
  toggleGenre,
  toggleCountry,
  setDateSort,
  setTitleSort,
  setImdbRange,
  toggleImdbActive,
  setHideWatched,
  resetFilters
} = filtersSlice.actions;

export default filtersSlice.reducer;