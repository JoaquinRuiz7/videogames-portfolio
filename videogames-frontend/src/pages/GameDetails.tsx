import { useEffect, useState } from 'react';
import ErrorPage from '../components/ErrorPage';
import { gamesService, getErrorStatusCode } from '../services/GamesService';
import type { GameDeal, GamesResponse } from '../types/game';

type GameDetailsProps = {
  gameName: string;
  onBack: () => void;
};

const formatValue = (value: string | number | null) => {
  if (value === null || value === '') {
    return 'N/A';
  }

  return value;
};

const GameDetails = ({ gameName, onBack }: GameDetailsProps) => {
  const [gameResponse, setGameResponse] = useState<GamesResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [dealLoadingByRow, setDealLoadingByRow] = useState<Record<string, boolean>>({});
  const [expandedDealRowKey, setExpandedDealRowKey] = useState<string | null>(null);
  const [selectedDeal, setSelectedDeal] = useState<GameDeal | null>(null);
  const [errorStatus, setErrorStatus] = useState<number | null>(null);

  const hasPCPlatform = (platforms: string[]) =>
    platforms.some((platform) => platform.toLowerCase().includes('pc'));

  const getGameDeal = async (rowKey: string, name: string) => {
    try {
      setDealLoadingByRow((currentState) => ({ ...currentState, [rowKey]: true }));
      const dealResponse: GameDeal[] = await gamesService.getGameDeal(name);
      setErrorStatus(null);
      setExpandedDealRowKey(rowKey);
      setSelectedDeal(dealResponse[0] ?? null);
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
        console.log('Hola hago el use effect')
        if (isMounted) {
          setIsLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, [gameName]);

  const games = gameResponse?.games ?? [];

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
                      {hasPCPlatform(game.platforms) ? (
                        <button
                          type="button"
                          className="deal-button"
                          onClick={() => getGameDeal(rowKey, game.name)}
                          disabled={dealLoadingByRow[rowKey]}
                        >
                          {dealLoadingByRow[rowKey] ? 'Searching deal...' : 'Find best deal'}
                        </button>
                      ) : (
                        <span className="deal-unavailable">PC deals only</span>
                      )}
                    </div>

                    {isExpanded ? (
                      <div className="deal-expanded-content">
                        {selectedDeal ? (
                          <>
                            <p className="deal-result-label">
                              Best deal found for {selectedDeal.game}
                            </p>
                            <p>
                              Deal price: <strong>${selectedDeal.price.toFixed(2)}</strong>
                            </p>
                            <div className="deal-expanded-actions">
                              <a
                                href={selectedDeal.dealUrl}
                                target="_blank"
                                rel="noreferrer"
                                className="deal-result-link"
                              >
                                Go to store offer
                              </a>
                            </div>
                          </>
                        ) : (
                          <p className="deal-empty">No deal available for this game right now.</p>
                        )}
                      </div>
                    ) : null}
                  </div>
                </article>
              );
            })}
          </div>
        ) : null}
      </section>
    </main>
  );
};

export default GameDetails;
