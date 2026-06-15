import { Loader2 } from "lucide-react";
import { useAuth } from "./auth/AuthContext";
import Dashboard from "./components/Dashboard";
import LandingPage from "./components/LandingPage";

function App() {
  const { user, initializing } = useAuth();

  if (initializing) {
    return (
      <div className="app-loading" role="status" aria-live="polite">
        <Loader2 size={28} className="spin" aria-hidden="true" />
        <span>Loading…</span>
      </div>
    );
  }

  return user ? <Dashboard user={user} /> : <LandingPage />;
}

export default App;
