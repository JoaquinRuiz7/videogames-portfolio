type AppHeaderProps = {
  onNavigateHome: () => void;
  isHome: boolean;
};

const AppHeader = ({ onNavigateHome, isHome }: AppHeaderProps) => {
  return (
    <header className="app-header">
      <div className="app-header-inner">
        <button type="button" className="brand-link" onClick={onNavigateHome}>
          <img src="/gamescope-logo.svg" alt="GameScope logo" className="brand-logo" />
          <span className="brand-text" aria-label="GameScope">
            <span>Game</span>
            <span>Scope</span>
          </span>
        </button>

        <nav className="app-nav" aria-label="Primary navigation">
          <button
            type="button"
            className={`nav-item ${isHome ? 'is-active' : ''}`}
            onClick={onNavigateHome}
          >
            Home
          </button>
        </nav>
      </div>
    </header>
  );
};

export default AppHeader;
