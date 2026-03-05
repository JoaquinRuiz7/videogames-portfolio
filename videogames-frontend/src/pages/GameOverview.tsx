import { useEffect, useRef, useState } from 'react';
import type { KeyboardEvent as ReactKeyboardEvent } from 'react';
import type { GameDetail } from '../types/GameDetail';
import type { DealResponse, Game, GameDeal } from '../types/game';
import { gamesService } from '../services/GamesService';
import DealDisplay from '../components/DealDisplay.tsx';

type GameOverviewProps = {
  game: Game;
  onBack: () => void;
};

const formatValue = (value: string | number | null) => {
  if (value === null || value === '') {
    return 'N/A';
  }

  return value;
};

const removeHtmlTags = (value: string) => value.replace(/<[^>]+>/g, '').trim();

const decodeHtmlEntities = (value: string) => {
  const parser = new DOMParser();
  const parsedDocument = parser.parseFromString(value, 'text/html');
  return parsedDocument.documentElement.textContent ?? '';
};

const formatDescription = (value: string) => decodeHtmlEntities(removeHtmlTags(value));

const GameOverview = ({ game, onBack }: GameOverviewProps) => {
  const screenshots = game.screenshots.length > 0 ? game.screenshots : [game.thumbnail];
  const [activeIndex, setActiveIndex] = useState(0);
  const [activeGalleryIndex, setActiveGalleryIndex] = useState<number | null>(null);
  const [isDealLoading, setIsDealLoading] = useState(false);
  const [selectedDeal, setSelectedDeal] = useState<GameDeal | null>(null);
  const [dealWasSearched, setDealWasSearched] = useState(false);
  const [gameDetail, setGameDetail] = useState<GameDetail | null>(null);
  const [isGameDetailLoading, setIsGameDetailLoading] = useState(true);
  const galleryModalRef = useRef<HTMLDivElement | null>(null);

  const hasPCPlatform = game.platforms.some((platform) => platform.toLowerCase().includes('pc'));

  const moveActiveScreenshot = (direction: -1 | 1) => {
    setActiveIndex(
      (currentIndex) => (currentIndex + direction + screenshots.length) % screenshots.length,
    );
  };

  const moveGalleryScreenshot = (direction: -1 | 1) => {
    setActiveGalleryIndex((currentIndex) => {
      if (currentIndex === null) {
        return 0;
      }

      return (currentIndex + direction + screenshots.length) % screenshots.length;
    });
  };

  const closeGallery = () => {
    setActiveGalleryIndex(null);
  };

  const getGameDeal = async () => {
    try {
      setIsDealLoading(true);
      const dealResponse: DealResponse = await gamesService.getGameDeal(game.name);
      setSelectedDeal(dealResponse.deals[0] ?? null);
      setDealWasSearched(true);
    } catch (error) {
      console.error(error);
      setSelectedDeal(null);
      setDealWasSearched(true);
    } finally {
      setIsDealLoading(false);
    }
  };

  useEffect(() => {
    let isMounted = true;

    setIsGameDetailLoading(true);

    gamesService
      .getGameDetails(game.id)
      .then((response) => {
        if (isMounted) {
          setGameDetail(response);
        }
      })
      .catch((error) => {
        console.error(error);
        if (isMounted) {
          setGameDetail(null);
        }
      })
      .finally(() => {
        if (isMounted) {
          setIsGameDetailLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, [game.id]);

  useEffect(() => {
    if (activeGalleryIndex !== null) {
      galleryModalRef.current?.focus();
    }
  }, [activeGalleryIndex]);

  useEffect(() => {
    if (activeGalleryIndex === null) {
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
  }, [activeGalleryIndex]);

  const handleGalleryKeyDown = (event: ReactKeyboardEvent<HTMLDivElement>) => {
    if (event.key === 'ArrowLeft') {
      event.preventDefault();
      moveGalleryScreenshot(-1);
    }

    if (event.key === 'ArrowRight') {
      event.preventDefault();
      moveGalleryScreenshot(1);
    }

    if (event.key === 'Escape') {
      event.preventDefault();
      closeGallery();
    }
  };

  return (
    <main className="game-overview-page">
      <section className="game-overview-card">
        <button type="button" className="back-button" onClick={onBack}>
          ← Back
        </button>

        <div className="game-overview-layout">
          <article className="game-overview-info-panel">
            <h1>{game.name}</h1>
            <div className="overview-stats-grid">
              <p>
                <strong>Metacritic:</strong> {formatValue(game.metacritic)}
              </p>
              <p>
                <strong>ESRB:</strong> {formatValue(game.esrb)}
              </p>
              <p>
                <strong>Released:</strong> {formatValue(game.released)}
              </p>
              <p>
                <strong>Playtime:</strong>{' '}
                {isGameDetailLoading
                  ? 'Loading details...'
                  : gameDetail
                    ? `${gameDetail.playtime}h`
                    : 'N/A'}
              </p>
              <p>
                <strong>Publishers:</strong>{' '}
                {isGameDetailLoading
                  ? 'Loading details...'
                  : formatValue(gameDetail?.publishers.join(', ') ?? null)}
              </p>
              <p>
                <strong>Review:</strong>{' '}
                {isGameDetailLoading ? (
                  'Loading details...'
                ) : gameDetail?.reviewUrl ? (
                  <a
                    href={gameDetail.reviewUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="review-link"
                  >
                    Metacritic
                  </a>
                ) : (
                  'N/A'
                )}
              </p>
            </div>
            <div className="overview-description-card">
              <h2>Description</h2>
              <p>
                {isGameDetailLoading
                  ? 'Loading details...'
                  : gameDetail?.description
                    ? formatDescription(gameDetail.description)
                    : 'N/A'}
              </p>
            </div>
            <div className="game-details-platforms">
              <strong>Platforms:</strong>
              <div className="platforms-cell">
                {game.platforms.length > 0 ? (
                  game.platforms.map((platform) => (
                    <small key={`${game.id}-${platform}`}>{platform}</small>
                  ))
                ) : (
                  <small>N/A</small>
                )}
              </div>
            </div>
            <div className="game-details-deal-row">
              {hasPCPlatform ? (
                !dealWasSearched ? (
                  <button
                    type="button"
                    className="deal-button"
                    onClick={getGameDeal}
                    disabled={isDealLoading}
                  >
                    {isDealLoading ? 'Searching deal...' : 'Find best deal'}
                  </button>
                ) : null
              ) : (
                <span className="deal-unavailable">PC deals only</span>
              )}
            </div>
            {dealWasSearched &&
              (selectedDeal ? (
                <DealDisplay
                  dealGame={selectedDeal.dealGame}
                  dealPrice={selectedDeal.dealPrice}
                  dealUrl={selectedDeal.dealUrl}
                  store={selectedDeal.store}
                />
              ) : (
                <p className="deal-empty">No deal available for this game right now.</p>
              ))}
          </article>

          <aside>
            <div className="game-overview-main-shot-wrapper">
              <button
                type="button"
                className="overview-carousel-nav"
                aria-label="Previous screenshot"
                onClick={() => moveActiveScreenshot(-1)}
              >
                ←
              </button>
              <button
                type="button"
                className="overview-main-shot-button"
                onClick={() => setActiveGalleryIndex(activeIndex)}
                aria-label="Open screenshot"
              >
                <img
                  src={screenshots[activeIndex]}
                  alt={`Main screenshot of ${game.name}`}
                  className="game-overview-main-shot"
                />
              </button>
              <button
                type="button"
                className="overview-carousel-nav"
                aria-label="Next screenshot"
                onClick={() => moveActiveScreenshot(1)}
              >
                →
              </button>
            </div>

            <div className="overview-thumbnails">
              {screenshots.map((shot, index) => (
                <button
                  key={`${shot}-${index}`}
                  type="button"
                  className={`overview-shot-button ${activeIndex === index ? 'is-active' : ''}`}
                  onClick={() => setActiveIndex(index)}
                >
                  <img src={shot} alt={`${game.name} screenshot ${index + 1}`} />
                </button>
              ))}
            </div>
          </aside>
        </div>
      </section>

      {activeGalleryIndex !== null ? (
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
            onClick={() => moveGalleryScreenshot(-1)}
            aria-label="Previous screenshot"
          >
            ←
          </button>
          <img
            src={screenshots[activeGalleryIndex]}
            alt={`${game.name} screenshot ${activeGalleryIndex + 1}`}
            className="gallery-main-image"
          />
          <button
            type="button"
            className="gallery-nav gallery-nav-next"
            onClick={() => moveGalleryScreenshot(1)}
            aria-label="Next screenshot"
          >
            →
          </button>
        </div>
      ) : null}
    </main>
  );
};

export default GameOverview;
