// Persists the JWT access token in localStorage so the session survives reloads.
// The token is stateless (1h expiry) and revalidated against GET /auth/me on boot.
const TOKEN_KEY = "meb.auth.token";

export function readToken(): string | null {
  try {
    return localStorage.getItem(TOKEN_KEY);
  } catch {
    return null;
  }
}

export function writeToken(token: string): void {
  try {
    localStorage.setItem(TOKEN_KEY, token);
  } catch {
    // Storage may be unavailable (private mode, blocked) — degrade to in-memory only.
  }
}

export function clearToken(): void {
  try {
    localStorage.removeItem(TOKEN_KEY);
  } catch {
    // Ignore — nothing we can do if storage is unavailable.
  }
}
