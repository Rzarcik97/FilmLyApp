import './styles/App.css';
import { Header } from './components/Header/Header.js';
import { MainPage } from './components/MainPage/MainPage.js';

function App() {
  return (
    <div className="app-container">
      <Header />
      <MainPage />
    </div>
  )
}

export default App;