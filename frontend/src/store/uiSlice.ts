import { createSlice } from '@reduxjs/toolkit';

export interface UIState {
  isAuthModalOpen: boolean;
  isLogoutModalOpen: boolean;
}

const initialState: UIState = {
  isAuthModalOpen: false,
  isLogoutModalOpen: false,
};

const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    openAuthModal: (state) => {
      state.isAuthModalOpen = true;
    },
    closeAuthModal: (state) => {
      state.isAuthModalOpen = false;
    },
    openLogoutModal: (state) => {
      state.isLogoutModalOpen = true;
    },
    closeLogoutModal: (state) => {
      state.isLogoutModalOpen = false;
    },
  },
});

export const { openAuthModal, closeAuthModal, openLogoutModal, closeLogoutModal } = uiSlice.actions;
export default uiSlice.reducer;