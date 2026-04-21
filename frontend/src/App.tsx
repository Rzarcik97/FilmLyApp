import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { Header } from './components/Header/Header';
import { MainPage } from './components/MainPage/MainPage';
import './styles/App.css';
import { OverviewPage } from './components/OverviewPage/OverviewPage';
import { useEffect, useState } from 'react';
import { BrowsePage } from './components/BrowsePage/BrowsePage';
import { StepEnum } from './types/enums';
import { Footer } from './components/MainPage/Footer';
import { DiscoverPage } from './components/DiscoverPage/DiscoverPage';
import { ActorsPage } from './components/BrowsePage/ActorsBrowse';
import { SignUpPage } from './components/SignUp/SignUpPage';
import { CreatePasswordPage } from './components/SignUp/CreatePasswordPage';
import { LoginPage } from './components/SignUp/LoginPage';

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
  const [isAuthOpen, setIsAuthOpen] = useState(false);
  const [step, setStep] = useState(StepEnum.Email);
  const [userData, setUserData] = useState({ email: '', password: '' });

  const handleAuthOpen = () => {
    setStep(StepEnum.Email);
    setIsAuthOpen(true);
  };

  const handleNext = (email: string) => {
    setUserData(prev => ({ ...prev, email }));
    setStep(StepEnum.Password);
  };

  return (
    <BrowserRouter>
      <div className="app-container relative">
        <ScrollToTop />

        <Header />

        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/sign-up" element={<SignUpPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/sign-up/password" element={<CreatePasswordPage />} />
          <Route path="/:type/:id" element={<OverviewPage />} />
          <Route path="/browse" element={<BrowsePage />} />
          <Route path="/discover/:type" element={<DiscoverPage />} />
          <Route path="/actors" element={<ActorsPage />} />
        </Routes>

        <Footer />
      </div>
    </BrowserRouter>
  )
}

export default App;