import { useEffect, useState } from 'react';
import SearchBar from '../components/SearchBar';
import GamesTable from '../components/GamesTable';
import ErrorPage from '../components/ErrorPage';
import { gamesService, getErrorStatusCode } from '../services/GamesService';
import type { GamesResponse } from '../types/game';

type HomeProps = {
  onOpenGame: (name: string) => void;
};

const Home = ({ onOpenGame }: HomeProps) => {
  const [gamesResponse, setGamesResponse] = useState<GamesResponse>({
    next: null,
    previous: null,
    games: [],
  });
  const [search, setSearch] = useState('');
  const [isLoadingGames, setIsLoadingGames] = useState(false);
  const [errorStatus, setErrorStatus] = useState<number | null>(null);

  const loadGames = async (pageUrl?: string) => {
    try {
      setIsLoadingGames(true);
      const response = await gamesService.fetchGames(pageUrl);
      setErrorStatus(null);
      setGamesResponse(response as GamesResponse);
    } catch (e) {
      console.error(e);
      setErrorStatus(getErrorStatusCode(e));
    } finally {
      setIsLoadingGames(false);
    }
  };

  useEffect(() => {
    let isMounted = true;

    setIsLoadingGames(true);

    gamesService
      .fetchGames()
      .then((response) => {
        if (isMounted) {
          setGamesResponse(response as GamesResponse);
          setErrorStatus(null);
        }
      })
      .catch((error) => {
        console.error(error);
        if (isMounted) {
          setErrorStatus(getErrorStatusCode(error));
        }
      })
      .finally(() => {
        if (isMounted) {
          setIsLoadingGames(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, []);

  if (errorStatus === 500) {
    return (
      <ErrorPage
        code="500"
        description="The server had a problem processing your request. Please try again later."
        actionLabel="Try again"
        onAction={() => loadGames()}
      />
    );
  }

  if (errorStatus === 404) {
    return (
      <ErrorPage
        code="404"
        description="The content you requested could not be found."
        actionLabel="Go to home"
        onAction={() => window.location.assign('/')}
      />
    );
  }

  return (
    <main className="home-page">
      <header className="hero">
        <div className="hero-overlay">
          <p className="hero-kicker">GAMESCOPE · FIND YOUR NEXT QUEST</p>
          <SearchBar search={search} onSearchChange={setSearch} onGameSelect={onOpenGame} />
        </div>
      </header>

      <section className="home-content">
        <h1>
          GameScope <span>PC gamer deals</span>
        </h1>
        <p className="breadcrumbs">
          Home {'>'} Video Games {'>'} Main Listing
        </p>
        <GamesTable
          gamesResponse={gamesResponse}
          isLoading={isLoadingGames}
          onOpenGame={onOpenGame}
          onNextPage={() => loadGames(gamesResponse.next as string)}
          onPreviousPage={() => loadGames(gamesResponse.previous as string)}
        />
      </section>
    </main>
  );
};

export default Home;
