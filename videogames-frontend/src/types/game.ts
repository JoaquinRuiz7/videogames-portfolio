export type Game = {
  id: number;
  name: string;
  thumbnail: string;
  screenshots: string[];
  metacritic: number;
  esrb: string;
  platforms: string[];
  released: string;
};

export type GamesResponse = {
  next: string | null;
  previous: string | null;
  games: Game[];
};

export type DealResponse = {
  deals: GameDeal[];
};

export type GameDeal = {
  dealGame: string;
  dealPrice: number;
  dealUrl: string;
  store?: Store;
};

export type Store = {
  name: string;
  logo: string;
};
export type GameDetails = {
  name: string;
  thumbnail: string;
  screenshots: string[];
  rating: number;
  metacritic: number | null;
  esrb: string | null;
  platforms: string[];
  released: string;
};
