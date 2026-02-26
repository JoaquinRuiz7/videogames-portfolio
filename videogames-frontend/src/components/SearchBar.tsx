import { useEffect, useState, type ChangeEvent, type KeyboardEvent } from 'react';
import { gamesService } from '../services/GamesService';
import type { Game, GamesResponse } from '../types/game';

type SearchBarProps = {
  search: string;
  onSearchChange: (value: string) => void;
  onGameSelect: (gameName: string) => void;
};

const SearchBar = ({ search, onSearchChange, onGameSelect }: SearchBarProps) => {
  const [suggestions, setSuggestions] = useState<Game[]>([]);
  const [isSearching, setIsSearching] = useState(false);
  const [highlightedIndex, setHighlightedIndex] = useState(-1);

  const onSearchInput = (event: ChangeEvent<HTMLInputElement>) => {
    onSearchChange(event.target.value);
  };

  const executeSearch = () => {
    const searchTerm = search.trim();

    if (!searchTerm) {
      return;
    }

    setSuggestions([]);
    setHighlightedIndex(-1);
    onGameSelect(searchTerm);
  };

  const onSearchKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'ArrowDown') {
      if (suggestions.length === 0) {
        return;
      }

      event.preventDefault();
      setHighlightedIndex((currentIndex) =>
        currentIndex < suggestions.length - 1 ? currentIndex + 1 : 0,
      );
      return;
    }

    if (event.key === 'ArrowUp') {
      if (suggestions.length === 0) {
        return;
      }

      event.preventDefault();
      setHighlightedIndex((currentIndex) =>
        currentIndex <= 0 ? suggestions.length - 1 : currentIndex - 1,
      );
      return;
    }

    if (event.key === 'Enter') {
      event.preventDefault();

      if (highlightedIndex >= 0 && suggestions[highlightedIndex]) {
        selectSuggestedGame(suggestions[highlightedIndex].name);
        return;
      }

      executeSearch();
    }
  };

  useEffect(() => {
    if (search.trim().length < 3) {
      setSuggestions([]);
      setIsSearching(false);
      setHighlightedIndex(-1);
      return;
    }

    const timeoutId = setTimeout(async () => {
      try {
        setIsSearching(true);
        const response: GamesResponse | null = await gamesService.fetchGames('', search.trim());
        const games = response ? response.games : [];
        setSuggestions(games);
        setHighlightedIndex(games.length > 0 ? 0 : -1);
      } catch (error) {
        console.error(error);
        setSuggestions([]);
        setHighlightedIndex(-1);
      } finally {
        setIsSearching(false);
      }
    }, 280);
    return () => {
      clearTimeout(timeoutId);
    };
  }, [search]);

  const selectSuggestedGame = (gameName: string) => {
    onSearchChange(gameName);
    setSuggestions([]);
    setHighlightedIndex(-1);
    onGameSelect(gameName);
  };

  return (
    <section className="hero-search" aria-label="Filtros de videojuegos">
      <div className="search-field-wrapper">
        <input
          type="search"
          value={search}
          onChange={onSearchInput}
          onKeyDown={onSearchKeyDown}
          placeholder="Buscar videojuego"
        />
        {search.length >= 3 ? (
          <div
            className="search-suggestions"
            role="listbox"
            aria-label="Sugerencias de videojuegos"
          >
            {isSearching ? <p className="suggestions-info">Buscando...</p> : null}
            {!isSearching && suggestions.length === 0 ? (
              <p className="suggestions-info">No se encontraron resultados.</p>
            ) : null}
            {suggestions?.map((suggestion: Game, index) => (
              <button
                type="button"
                key={suggestion.id ?? index}
                className={`suggestion-item ${highlightedIndex === index ? 'is-highlighted' : ''}`}
                onClick={() => selectSuggestedGame(suggestion.name)}
              >
                {suggestion.name}
              </button>
            ))}
          </div>
        ) : null}
      </div>
      <button
        type="button"
        aria-label="Buscar videojuegos"
        className="search-button"
        onClick={executeSearch}
      >
        <svg viewBox="0 0 24 24" aria-hidden="true">
          <path d="M10.5 3a7.5 7.5 0 1 1 4.84 13.23l4.21 4.21-1.41 1.41-4.21-4.21A7.5 7.5 0 0 1 10.5 3Zm0 2a5.5 5.5 0 1 0 0 11 5.5 5.5 0 0 0 0-11Z" />
        </svg>
      </button>
    </section>
  );
};

export default SearchBar;
