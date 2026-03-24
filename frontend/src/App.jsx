import './styles/App.css';
import { Header } from './components/Header/Header';
import { MainPage } from './components/MainPage/MainPage';

function App() {
  return (
    <div className="app-container">
      <Header />
      <MainPage />
    </div>
  )
}

export default App;