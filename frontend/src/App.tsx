import { Apple, ArrowRight, Eye, EyeOff, LockKeyhole, Mail, PiggyBank, User } from "lucide-react";
import { FormEvent, useState } from "react";

type AuthMode = "login" | "register";

function App() {
  const [mode, setMode] = useState<AuthMode>("login");
  const [showPassword, setShowPassword] = useState(false);

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
  }

  const isLogin = mode === "login";

  return (
    <main className="auth-page">
      <section className="auth-card" aria-label="Authentication">
        <div className="brand">
          <span className="brand-mark">
            <PiggyBank size={24} strokeWidth={2.3} />
          </span>
          <span>myEasyBudget</span>
        </div>

        <div className="tabs" role="tablist" aria-label="Authentication mode">
          <button
            className={isLogin ? "active" : ""}
            type="button"
            role="tab"
            aria-selected={isLogin}
            onClick={() => setMode("login")}
          >
            Вход
          </button>
          <button
            className={!isLogin ? "active" : ""}
            type="button"
            role="tab"
            aria-selected={!isLogin}
            onClick={() => setMode("register")}
          >
            Регистрация
          </button>
        </div>

        <div className="heading">
          <h1>{isLogin ? "Войдите в аккаунт" : "Создайте аккаунт"}</h1>
          <p>
            {isLogin
              ? "Продолжите работу с бюджетом, счетами и финансовыми целями."
              : "Начните вести бюджет и отслеживать расходы в одном месте."}
          </p>
        </div>

        <div className="social-actions">
          <button type="button">
            <span className="google-icon">G</span>
            Google
          </button>
          <button type="button">
            <Apple size={20} aria-hidden="true" />
            Apple
          </button>
        </div>

        <div className="divider">
          <span>или используйте email</span>
        </div>

        <form className="auth-form" onSubmit={handleSubmit}>
          {!isLogin && (
            <label className="field">
              <span>Имя</span>
              <div className="input-wrap">
                <User size={18} aria-hidden="true" />
                <input type="text" placeholder="Ваше имя" autoComplete="name" required />
              </div>
            </label>
          )}

          <label className="field">
            <span>Email</span>
            <div className="input-wrap">
              <Mail size={18} aria-hidden="true" />
              <input type="email" placeholder="name@example.com" autoComplete="email" required />
            </div>
          </label>

          <label className="field">
            <span>Пароль</span>
            <div className="input-wrap">
              <LockKeyhole size={18} aria-hidden="true" />
              <input
                type={showPassword ? "text" : "password"}
                placeholder="Введите пароль"
                autoComplete={isLogin ? "current-password" : "new-password"}
                required
              />
              <button
                className="icon-button"
                type="button"
                aria-label={showPassword ? "Скрыть пароль" : "Показать пароль"}
                onClick={() => setShowPassword((visible) => !visible)}
              >
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </label>

          {isLogin ? (
            <div className="form-row">
              <label className="checkbox">
                <input type="checkbox" defaultChecked />
                <span>Запомнить меня</span>
              </label>
              <a href="#forgot">Забыли пароль?</a>
            </div>
          ) : (
            <label className="checkbox terms">
              <input type="checkbox" required />
              <span>Я принимаю условия сервиса и политику конфиденциальности.</span>
            </label>
          )}

          <button className="submit-button" type="submit">
            {isLogin ? "Войти" : "Зарегистрироваться"}
            <ArrowRight size={19} aria-hidden="true" />
          </button>
        </form>
      </section>
    </main>
  );
}

export default App;
