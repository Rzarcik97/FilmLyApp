import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Header } from './components/MainPage/Header';
import { MainPage } from './components/MainPage/MainPage';
import './styles/App.css';
import { OverviewPage } from './components/OverviewPage/OverviewPage';

function App() {
  return (
    <BrowserRouter>
      <div className="app-container">
        <Header />
        <Routes>
          <Route path="/" element={<MainPage /> }/>
          <Route path="/overview/:id" element={<OverviewPage />}/>
        </Routes>
        
      </div>
    </BrowserRouter>    
  )
}

export default App;