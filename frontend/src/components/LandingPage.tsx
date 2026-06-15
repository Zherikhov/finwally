import { BarChart3, Bell, PiggyBank, Sparkles, Target, Wallet } from "lucide-react";
import AuthPanel from "./AuthPanel";

const FEATURES = [
  {
    icon: Wallet,
    title: "All your accounts in one place",
    text: "Cash, cards, savings, and investments — a single view of your finances.",
  },
  {
    icon: BarChart3,
    title: "Clear spending",
    text: "Categories and visual analytics show where your money goes.",
  },
  {
    icon: Target,
    title: "Budgets and goals",
    text: "Plan your monthly budget and save for what matters, stress-free.",
  },
  {
    icon: Bell,
    title: "Stay on top of bills",
    text: "Track recurring payments and never miss a due date.",
  },
];

function LandingPage() {
  return (
    <div className="landing">
      <header className="landing-header">
        <div className="brand">
          <span className="brand-mark">
            <PiggyBank size={24} strokeWidth={2.3} />
          </span>
          <span>myEasyBudget</span>
        </div>
      </header>

      <main className="landing-main">
        <section className="hero" aria-labelledby="hero-title">
          <span className="hero-badge">
            <Sparkles size={15} aria-hidden="true" />
            Personal finance for the whole household
          </span>
          <h1 id="hero-title">Budget with ease and keep your spending under control</h1>
          <p className="hero-lead">
            myEasyBudget helps households bring their accounts together, plan budgets,
            stay on top of bills, and understand their spending — all in one simple app.
          </p>

          <ul className="feature-list">
            {FEATURES.map(({ icon: Icon, title, text }) => (
              <li key={title} className="feature">
                <span className="feature-icon">
                  <Icon size={20} aria-hidden="true" />
                </span>
                <div>
                  <h3>{title}</h3>
                  <p>{text}</p>
                </div>
              </li>
            ))}
          </ul>
        </section>

        <aside className="auth-slot" aria-label="Sign in and sign up">
          <AuthPanel />
        </aside>
      </main>

      <footer className="landing-footer">
        <span>© {new Date().getFullYear()} myEasyBudget</span>
        <span>my-easy-budget.com</span>
      </footer>
    </div>
  );
}

export default LandingPage;
