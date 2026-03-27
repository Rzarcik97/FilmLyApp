import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { Header } from './components/MainPage/Header';
import { MainPage } from './components/MainPage/MainPage';
import './styles/App.css';
import { OverviewPage } from './components/OverviewPage/OverviewPage';
import { useEffect } from 'react';
import { BrowsePage } from './components/BrowsePage/BrowsePage';

const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth'
    });
  }, [pathname]);

  return null;
};

function App() {
  return (
    <BrowserRouter>
      <div className="app-container">
        <ScrollToTop />

        <Header />
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/movie/:id" element={<OverviewPage />} />
          <Route path="/test-browse" element={<BrowsePage />} />
        </Routes>

      </div>
    </BrowserRouter>
  )
}

export default App;