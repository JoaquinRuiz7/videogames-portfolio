import { Fragment, useMemo, useState } from 'react';
import type { DealResponse, Game, GameDeal, GamesResponse } from '../types/game';
import { gamesService } from '../services/GamesService.ts';
import DealDisplay from './DealDisplay.tsx';

type GamesTableProps = {
  gamesResponse: GamesResponse;
  onNextPage: () => void;
  onPreviousPage: () => void;
  isLoading: boolean;
  onOpenGame: (name: string) => void;
  onViewDetails: (game: Game) => void;
};

const buildOptimizedThumbnail = (thumbnail: string) => {
  try {
    const parsedUrl = new URL(thumbnail);

    parsedUrl.searchParams.set('w', '104');
    parsedUrl.searchParams.set('h', '136');
    parsedUrl.searchParams.set('fit', 'crop');
    parsedUrl.searchParams.set('auto', 'format');

    return parsedUrl.toString();
  } catch {
    return thumbnail;
  }
};

const ThumbnailCell = ({ thumbnail, name }: { thumbnail: string; name: string }) => {
  const [isImageLoaded, setIsImageLoaded] = useState(false);
  const optimizedThumbnail = useMemo(() => buildOptimizedThumbnail(thumbnail), [thumbnail]);

  return (
    <div className="thumbnail-wrapper" data-loaded={isImageLoaded}>
      {!isImageLoaded ? <span className="thumb-loader" aria-hidden="true" /> : null}
      <img
        className="cover-thumb"
        src={optimizedThumbnail}
        alt={`Cover of ${name}`}
        loading="lazy"
        decoding="async"
        onLoad={() => setIsImageLoaded(true)}
      />
    </div>
  );
};

const GamesTable = ({
  gamesResponse,
  onNextPage,
  onPreviousPage,
  isLoading,
  onOpenGame,
  onViewDetails,
}: GamesTableProps) => {
  const { games, next, previous } = gamesResponse;
  const [dealLoadingByRow, setDealLoadingByRow] = useState<Record<string, boolean>>({});
  const [expandedDealRowKey, setExpandedDealRowKey] = useState<string | null>(null);
  const [selectedDeal, setSelectedDeal] = useState<GameDeal | null>(null);

  const hasPCPlatform = (platforms: string[]) =>
    platforms.some((platform) => platform.toLowerCase().includes('pc'));

  const getGameDeal = async (rowKey: string, name: string) => {
    try {
      setDealLoadingByRow((currentState) => ({ ...currentState, [rowKey]: true }));
      const dealResponse: DealResponse = await gamesService.getGameDeal(name);
      setExpandedDealRowKey(rowKey);
      setSelectedDeal(dealResponse.deals[0] ?? null);
    } catch (error) {
      console.error(error);
      setExpandedDealRowKey(rowKey);
      setSelectedDeal(null);
    } finally {
      setDealLoadingByRow((currentState) => ({ ...currentState, [rowKey]: false }));
    }
  };

  return (
    <section className="games-list-wrapper">
      <p className="results-count">
        Showing <strong>{games.length}</strong> videogames (Deals are only available for PC)
      </p>
      {isLoading ? (
        <div className="table-loader" role="status" aria-live="polite">
          <span className="spinner" aria-hidden="true" /> loading videogames...
        </div>
      ) : null}
      <div className="games-table" role="table" aria-label="Listado de videojuegos">
        <div className="table-header" role="row">
          <span>Cover</span>
          <span>Release date</span>
          <span>Videogame</span>
          <span>Rated</span>
          <span>Platform</span>
          <span>Score</span>
          <span>Deal</span>
        </div>

        {games.length === 0 ? (
          <p className="empty-state">No games found. 🎮</p>
        ) : (
          games.map((game, index) => {
            const rowKey = `${game.id}-${game.name}-${game.released}-${index}`;
            const isExpanded = expandedDealRowKey === rowKey;

            return (
              <Fragment key={rowKey}>
                <div className="table-row" role="row">
                  <span>
                    <ThumbnailCell thumbnail={game.thumbnail} name={game.name} />
                  </span>
                  <span>{game.released}</span>
                  <span className="title-cell">
                    <button
                      type="button"
                      className="title-link"
                      onClick={() => onOpenGame(game.name)}
                    >
                      {game.name}
                    </button>
                    <button
                      type="button"
                      className="view-details-button"
                      onClick={() => onViewDetails(game)}
                    >
                      View details
                    </button>
                  </span>
                  <span>{game.esrb}</span>
                  <span className="platforms-cell">
                    {game.platforms.map((platform) => (
                      <small key={`${rowKey}-${platform}`}>{platform}</small>
                    ))}
                  </span>
                  <span className="score-cell">{game.metacritic}</span>
                  <span className="deal-cell">
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
                      <span className="deal-unavailable">Not available on PC</span>
                    )}
                  </span>
                </div>

                {isExpanded && (
                  <div className="table-row-expanded" role="row">
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
              </Fragment>
            );
          })
        )}
      </div>

      <div className="pagination-controls">
        <button type="button" onClick={onPreviousPage} disabled={!previous || isLoading}>
          ← Previous
        </button>
        {isLoading ? (
          <span className="pagination-loader" role="status" aria-live="polite">
            <span className="spinner" aria-hidden="true" /> Loading page...
          </span>
        ) : null}
        <button type="button" onClick={onNextPage} disabled={!next || isLoading}>
          Next →
        </button>
      </div>
    </section>
  );
};

export default GamesTable;
