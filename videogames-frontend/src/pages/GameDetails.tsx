import { useEffect, useRef, useState } from 'react';
import type { KeyboardEvent as ReactKeyboardEvent } from 'react';
import ErrorPage from '../components/ErrorPage';
import { gamesService, getErrorStatusCode } from '../services/GamesService';
import type { DealResponse, Game, GameDeal, GamesResponse } from '../types/game';
import DealDisplay from '../components/DealDisplay.tsx';

type GameDetailsProps = {
  gameName: string;
  onBack: () => void;
  onViewDetails: (game: Game) => void;
};

const formatValue = (value: string | number | null) => {
  if (value === null || value === '') {
    return 'N/A';
  }

  return value;
};

const GameDetails = ({ gameName, onBack, onViewDetails }: GameDetailsProps) => {
  const [gameResponse, setGameResponse] = useState<GamesResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [dealLoadingByRow, setDealLoadingByRow] = useState<Record<string, boolean>>({});
  const [expandedDealRowKey, setExpandedDealRowKey] = useState<string | null>(null);
  const [selectedDeal, setSelectedDeal] = useState<GameDeal | null>(null);
  const [errorStatus, setErrorStatus] = useState<number | null>(null);
  const [activeGallery, setActiveGallery] = useState<{ rowKey: string; index: number } | null>(
    null,
  );
  const galleryModalRef = useRef<HTMLDivElement | null>(null);

  const hasPCPlatform = (platforms: string[]) =>
    platforms.some((platform) => platform.toLowerCase().includes('pc'));

  const getGameDeal = async (rowKey: string, name: string) => {
    try {
      setDealLoadingByRow((currentState) => ({ ...currentState, [rowKey]: true }));
      const dealResponse: DealResponse = await gamesService.getGameDeal(name);
      setErrorStatus(null);
      setExpandedDealRowKey(rowKey);
      setSelectedDeal(dealResponse.deals[0] ?? null);
    } catch (error) {
      console.error(error);
      setErrorStatus(getErrorStatusCode(error));
      setExpandedDealRowKey(rowKey);
      setSelectedDeal(null);
    } finally {
      setDealLoadingByRow((currentState) => ({ ...currentState, [rowKey]: false }));
    }
  };

  useEffect(() => {
    let isMounted = true;

    setIsLoading(true);

    gamesService
      .fetchGames('', gameName)
      .then((response: GamesResponse) => {
        if (isMounted) {
          setGameResponse(response);
          setErrorStatus(null);
        }
      })
      .catch((error) => {
        console.error(error);
        if (isMounted) {
          setErrorStatus(getErrorStatusCode(error));
          setGameResponse(null);
        }
      })
      .finally(() => {
        if (isMounted) {
          setIsLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, [gameName]);

  const games = gameResponse?.games ?? [];
  const activeGame = games.find(
    (game) => `${game.name}-${game.released}` === activeGallery?.rowKey,
  );
  const activeScreenshots = activeGame?.screenshots ?? [];

  const closeGallery = () => {
    setActiveGallery(null);
  };

  const moveGallery = (direction: -1 | 1) => {
    if (!activeGallery || activeScreenshots.length === 0) {
      return;
    }

    const nextIndex =
      (activeGallery.index + direction + activeScreenshots.length) % activeScreenshots.length;

    setActiveGallery({
      rowKey: activeGallery.rowKey,
      index: nextIndex,
    });
  };

  useEffect(() => {
    if (activeGallery && activeScreenshots.length > 0) {
      galleryModalRef.current?.focus();
    }
  }, [activeGallery, activeScreenshots.length]);

  useEffect(() => {
    if (!activeGallery) {
      return;
    }

    const handleEscapeKey = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        event.preventDefault();
        closeGallery();
      }
    };

    window.addEventListener('keydown', handleEscapeKey);

    return () => {
      window.removeEventListener('keydown', handleEscapeKey);
    };
  }, [activeGallery]);

  const handleGalleryKeyDown = (event: ReactKeyboardEvent<HTMLDivElement>) => {
    if (event.key === 'ArrowLeft') {
      event.preventDefault();
      moveGallery(-1);
    }

    if (event.key === 'ArrowRight') {
      event.preventDefault();
      moveGallery(1);
    }

    if (event.key === 'Escape') {
      event.preventDefault();
      closeGallery();
    }
  };

  if (errorStatus === 500) {
    return (
      <ErrorPage
        code="500"
        description="The server had a problem processing your request. Please try again later."
        actionLabel="Try again"
        onAction={onBack}
      />
    );
  }

  if (errorStatus === 404) {
    return (
      <ErrorPage
        code="404"
        description="The requested game could not be found."
        actionLabel="Back to home"
        onAction={onBack}
      />
    );
  }

  return (
    <main className="game-details-page">
      <section className="game-details-card">
        <button type="button" className="back-button" onClick={onBack}>
          ← Back
        </button>

        <header className="game-details-header">
          <h1>
            Results for <strong>{gameName}</strong>
          </h1>{' '}
          {!isLoading && games.length > 0 ? (
            <p>
              <strong>{games.length}</strong> versions found.
            </p>
          ) : null}
        </header>

        {isLoading ? <p>Loading...</p> : null}

        {!isLoading && games.length === 0 ? <p>We could not find {gameName}.</p> : null}

        {games.length > 0 ? (
          <div className="game-versions-list">
            {games.map((game) => {
              const rowKey = `${game.name}-${game.released}`;
              const isExpanded = expandedDealRowKey === rowKey;

              return (
                <article className="game-details-content" key={rowKey}>
                  <img
                    src={game.thumbnail}
                    alt={`Cover art for ${game.name}`}
                    className="game-details-thumb"
                  />
                  <div className="game-details-info">
                    <h2>{game.name}</h2>
                    <p>
                      <strong>Metacritic:</strong> {formatValue(game.metacritic)}
                    </p>
                    <p>
                      <strong>ESRB:</strong> {formatValue(game.esrb)}
                    </p>
                    <div className="game-details-platforms">
                      <strong>Platforms:</strong>
                      <div className="platforms-cell">
                        {game.platforms.length > 0 ? (
                          game.platforms.map((platform) => (
                            <small key={`${rowKey}-${platform}`}>{platform}</small>
                          ))
                        ) : (
                          <small>N/A</small>
                        )}
                      </div>
                    </div>
                    <p>
                      <strong>Released:</strong> {formatValue(game.released)}
                    </p>
                    <div className="game-details-deal-row">
                      <button
                        type="button"
                        className="view-details-button"
                        onClick={() => onViewDetails(game)}
                      >
                        View details
                      </button>
                    </div>
                    <div className="game-details-screenshots">
                      <strong>Screenshots:</strong>
                      {game.screenshots.length > 0 ? (
                        <div className="screenshots-grid">
                          {game.screenshots.slice(0, 4).map((screenshot, index) => (
                            <button
                              key={`${rowKey}-${screenshot}`}
                              type="button"
                              className="screenshot-button"
                              onClick={() => setActiveGallery({ rowKey, index })}
                            >
                              <img
                                src={screenshot}
                                alt={`${game.name} screenshot ${index + 1}`}
                                className="screenshot-thumb"
                              />
                            </button>
                          ))}
                        </div>
                      ) : (
                        <p className="deal-empty">No screenshots available.</p>
                      )}
                    </div>
                    <div className="game-details-deal-row">
                      {hasPCPlatform(game.platforms) ? (
                        !isExpanded ? (
                          <button
                            type="button"
                            className="deal-button"
                            onClick={() => getGameDeal(rowKey, game.name)}
                            disabled={dealLoadingByRow[rowKey]}
                          >
                            {dealLoadingByRow[rowKey] ? 'Searching deal...' : 'Find best deal'}
                          </button>
                        ) : null
                      ) : (
                        <span className="deal-unavailable">PC deals only</span>
                      )}
                    </div>
                    {isExpanded && (
                      <div className="deal-expanded-content">
                        {selectedDeal ? (
                          <DealDisplay
                            dealGame={selectedDeal.dealGame}
                            dealPrice={selectedDeal.dealPrice}
                            dealUrl={selectedDeal.dealUrl}
                            store={selectedDeal.store}
                          />
                        ) : (
                          <p className="deal-empty">No deal available for this game right now.</p>
                        )}
                      </div>
                    )}
                  </div>
                </article>
              );
            })}
          </div>
        ) : null}
      </section>

      {activeGallery && activeScreenshots.length > 0 ? (
        <div
          className="gallery-modal"
          role="dialog"
          aria-modal="true"
          aria-label="Screenshot viewer"
          tabIndex={-1}
          ref={galleryModalRef}
          onKeyDown={handleGalleryKeyDown}
        >
          <button type="button" className="gallery-close" onClick={closeGallery}>
            ✕
          </button>
          <button
            type="button"
            className="gallery-nav gallery-nav-prev"
            onClick={() => moveGallery(-1)}
            aria-label="Previous screenshot"
          >
            ←
          </button>
          <img
            src={activeScreenshots[activeGallery.index]}
            alt={`${activeGame?.name ?? 'Game'} screenshot ${activeGallery.index + 1}`}
            className="gallery-main-image"
          />
          <button
            type="button"
            className="gallery-nav gallery-nav-next"
            onClick={() => moveGallery(1)}
            aria-label="Next screenshot"
          >
            →
          </button>
        </div>
      ) : null}
    </main>
  );
};

export default GameDetails;
