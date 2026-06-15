import { LogOut, PiggyBank } from "lucide-react";
import { User } from "../api/auth";
import { useAuth } from "../auth/AuthContext";

function Dashboard({ user }: { user: User }) {
  const { logout } = useAuth();
  const greetingName = user.displayName?.trim() || user.email;

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div className="brand">
          <span className="brand-mark">
            <PiggyBank size={24} strokeWidth={2.3} />
          </span>
          <span>myEasyBudget</span>
        </div>
        <button type="button" className="ghost-button" onClick={logout}>
          <LogOut size={18} aria-hidden="true" />
          Sign out
        </button>
      </header>

      <main className="dashboard-main">
        <section className="welcome-card">
          <h1>Hello, {greetingName}!</h1>
          <p>
            You're signed in. Your accounts, budgets, and spending analytics will appear
            here soon — we're still building these sections.
          </p>

          <dl className="profile-grid">
            <div>
              <dt>Email</dt>
              <dd>{user.email}</dd>
            </div>
            <div>
              <dt>Default currency</dt>
              <dd>{user.defaultCurrencyCode}</dd>
            </div>
            <div>
              <dt>Language</dt>
              <dd>{user.locale}</dd>
            </div>
            <div>
              <dt>Timezone</dt>
              <dd>{user.timezone}</dd>
            </div>
          </dl>
        </section>
      </main>
    </div>
  );
}

export default Dashboard;
