import { useCallback, useEffect, useState } from 'react';
import './App.css';
import AppHeader from './components/AppHeader';
import ErrorPage from './components/ErrorPage';
import Home from './pages/Home';
import GameDetails from './pages/GameDetails';

const getCurrentPath = () => window.location.pathname;

function App() {
  const [currentPath, setCurrentPath] = useState(getCurrentPath);

  useEffect(() => {
    const handleNavigation = () => {
      setCurrentPath(getCurrentPath());
    };

    window.addEventListener('popstate', handleNavigation);

    return () => {
      window.removeEventListener('popstate', handleNavigation);
    };
  }, []);

  const navigateTo = useCallback((path: string) => {
    window.history.pushState({}, '', path);
    setCurrentPath(path);
  }, []);

  const openGame = useCallback(
    (name: string) => {
      navigateTo(`/game/${encodeURIComponent(name)}`);
    },
    [navigateTo],
  );

  const isHomePath = currentPath === '/';
  const isGameDetails = currentPath.startsWith('/game/') && currentPath.length > '/game/'.length;

  return (
    <div className="app-shell">
      <AppHeader onNavigateHome={() => navigateTo('/')} isHome={!isGameDetails} />

      {isGameDetails ? (
        <GameDetails
          key={decodeURIComponent(currentPath.replace('/game/', ''))}
          gameName={decodeURIComponent(currentPath.replace('/game/', ''))}
          onBack={() => navigateTo('/')}
        />
      ) : isHomePath ? (
        <Home onOpenGame={openGame} />
      ) : (
        <ErrorPage
          code="404"
          description="The page you are looking for does not exist."
          actionLabel="Go to home"
          onAction={() => navigateTo('/')}
        />
      )}
    </div>
  );
}

export default App;
