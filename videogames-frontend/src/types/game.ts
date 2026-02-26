export type Game = {
  id: number;
  name: string;
  thumbnail: string;
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

export type GameDeal = {
  game: string;
  price: number;
  dealUrl: string;
};

export type GameDetails = {
  name: string;
  thumbnail: string;
  rating: number;
  metacritic: number | null;
  esrb: string | null;
  platforms: string[];
  released: string;
};
