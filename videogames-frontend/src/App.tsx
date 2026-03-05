import { useCallback, useEffect, useState } from 'react';
import './App.css';
import AppHeader from './components/AppHeader';
import ErrorPage from './components/ErrorPage';
import Home from './pages/Home';
import GameDetails from './pages/GameDetails';
import type { Game } from './types/game';
import GameOverview from './pages/GameOverview';

type RouteState = {
  game?: Game;
};

const getCurrentPath = () => window.location.pathname;
const getCurrentState = () => (window.history.state as RouteState | null) ?? {};

function App() {
  const [currentPath, setCurrentPath] = useState(getCurrentPath);
  const [routeState, setRouteState] = useState<RouteState>(getCurrentState);

  useEffect(() => {
    const handleNavigation = () => {
      setCurrentPath(getCurrentPath());
      setRouteState(getCurrentState());
    };

    window.addEventListener('popstate', handleNavigation);

    return () => {
      window.removeEventListener('popstate', handleNavigation);
    };
  }, []);

  const navigateTo = useCallback((path: string, state: RouteState = {}) => {
    window.history.pushState(state, '', path);
    setCurrentPath(path);
    setRouteState(state);
  }, []);

  const openGame = useCallback(
    (name: string) => {
      navigateTo(`/game/${encodeURIComponent(name)}`);
    },
    [navigateTo],
  );

  const openGameDetails = useCallback(
    (game: Game) => {
      navigateTo(`/details/${game.id}-${encodeURIComponent(game.name)}`, { game });
    },
    [navigateTo],
  );

  const isHomePath = currentPath === '/';
  const isGameDetails = currentPath.startsWith('/game/') && currentPath.length > '/game/'.length;
  const isGameOverview =
    currentPath.startsWith('/details/') && currentPath.length > '/details/'.length;

  return (
    <div className="app-shell">
      <AppHeader onNavigateHome={() => navigateTo('/')} isHome={isHomePath} />

      {isGameOverview ? (
        routeState.game ? (
          <GameOverview game={routeState.game} onBack={() => navigateTo('/')} />
        ) : (
          <ErrorPage
            code="404"
            description="No pudimos cargar los detalles del juego. Volvé al listado e intenta nuevamente."
            actionLabel="Go to home"
            onAction={() => navigateTo('/')}
          />
        )
      ) : isGameDetails ? (
        <GameDetails
          key={decodeURIComponent(currentPath.replace('/game/', ''))}
          gameName={decodeURIComponent(currentPath.replace('/game/', ''))}
          onBack={() => navigateTo('/')}
          onViewDetails={openGameDetails}
        />
      ) : isHomePath ? (
        <Home onOpenGame={openGame} onViewDetails={openGameDetails} />
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
