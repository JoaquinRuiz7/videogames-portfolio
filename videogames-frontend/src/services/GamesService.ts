import { axiosInstance } from '../api/axios';
import type { GameDeal, GamesResponse } from '../types/game';

export const getErrorStatusCode = (error: unknown): number | null => {
  if (
    typeof error === 'object' &&
    error !== null &&
    'response' in error &&
    typeof error.response === 'object' &&
    error.response !== null &&
    'status' in error.response &&
    typeof error.response.status === 'number'
  ) {
    return error.response.status;
  }

  return null;
};

class GamesService {
  private readonly cache: Map<string, GamesResponse> = new Map<string, GamesResponse>();

  public async fetchGames(pageUrl?: string, name?: string): Promise<GamesResponse> {
    const endpoint: string = this.getFetchGamesEndpoint(pageUrl, name);

    if (this.cache.has(endpoint)) {
      return this.cache.get(endpoint) as GamesResponse;
    }

    const { data } = await axiosInstance.get(endpoint);
    this.cache.set(endpoint, data);
    return data;
  }

  private getFetchGamesEndpoint(page?: string, name?: string): string {
    if (page) {
      return `/games?page=${encodeURIComponent(page)}`;
    }

    if (name) {
      return `/games?search=${encodeURIComponent(name)}&exact=false`;
    }

    return '/games';
  }
  public async getGameDeal(name: string): Promise<GameDeal[]> {
    const { data } = await axiosInstance.get(`/games/${encodeURIComponent(name)}/deal`);
    return data;
  }
}

export const gamesService = new GamesService();
