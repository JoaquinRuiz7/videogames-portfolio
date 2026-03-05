import { axiosInstance } from '../api/axios';
import type { DealResponse, GamesResponse } from '../types/game';
import type { GameDetail } from '../types/GameDetail.ts';

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
  private readonly detailsCache: Map<number, GameDetail> = new Map<number, GameDetail>();

  public async fetchGames(pageUrl?: string, name?: string): Promise<GamesResponse> {
    const endpoint: string = this.getFetchGamesEndpoint(pageUrl, name);

    if (this.cache.has(endpoint)) {
      return this.cache.get(endpoint) as GamesResponse;
    }

    const { data } = await axiosInstance.get(endpoint);
    this.cache.set(endpoint, data);

    return data;
  }

  public async getGameDeal(name: string): Promise<DealResponse> {
    const { data } = await axiosInstance.get(`/games/${encodeURIComponent(name)}/deals`);
    return data;
  }

  public async getGameDetails(gameId: number): Promise<GameDetail> {
    if (this.detailsCache.has(gameId)) {
      return this.detailsCache.get(gameId) as GameDetail;
    }

    const { data } = await axiosInstance.get(`/games/${gameId}`);
    this.detailsCache.set(gameId, data);

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
}

export const gamesService = new GamesService();
