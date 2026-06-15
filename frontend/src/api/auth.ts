import { apiRequest } from "../lib/apiClient";

export type UserStatus = "ACTIVE" | "SUSPENDED" | "DELETED";

/** Mirrors the backend UserSummary record. */
export interface User {
  id: string;
  email: string;
  displayName: string | null;
  defaultCurrencyCode: string;
  locale: string;
  timezone: string;
  status: UserStatus;
}

/** Mirrors the backend AuthController.AuthResponse record. */
export interface AuthResponse {
  tokenType: string;
  accessToken: string;
  expiresAt: string;
  user: User;
}

export interface RegisterPayload {
  email: string;
  password: string;
  displayName?: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export function register(payload: RegisterPayload): Promise<AuthResponse> {
  return apiRequest<AuthResponse>("/auth/register", { method: "POST", body: payload });
}

export function login(payload: LoginPayload): Promise<AuthResponse> {
  return apiRequest<AuthResponse>("/auth/login", { method: "POST", body: payload });
}

export function fetchCurrentUser(token: string): Promise<User> {
  return apiRequest<User>("/auth/me", { token });
}
