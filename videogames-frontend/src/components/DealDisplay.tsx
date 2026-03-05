import SellingStore from './SellingStore.tsx';
import type { GameDeal } from '../types/game.ts';

const DealDisplay = (selectedDeal: GameDeal) => {
  return (
    <div className="deal-expanded-content">
      <p className="deal-result-label">Best deal found for {selectedDeal.dealGame}</p>
      <p>
        Deal price: <strong>${selectedDeal.dealPrice.toFixed(2)}</strong>
      </p>
      {selectedDeal.store && (
        <SellingStore logo={selectedDeal.store.logo} name={selectedDeal.store.name} />
      )}
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
    </div>
  );
};

export default DealDisplay;
