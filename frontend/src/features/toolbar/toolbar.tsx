import { Logo } from "./components/logo";
import { NavLinks } from "./components/nav-links";
import { AuthButtons } from "./components/auth-buttons";

const containerClassName = "mx-auto w-full max-w-6xl px-4 sm:px-6 lg:px-8";

export function Toolbar() {
  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/90 backdrop-blur-md">
      <div className={containerClassName}>
        <div className="flex h-16 items-center justify-between gap-6">
          <Logo />
          <div className="flex items-center gap-4 sm:gap-8">
            <nav aria-label="Primary navigation" className="hidden md:block">
              <NavLinks />
            </nav>
            <AuthButtons />
          </div>
        </div>
      </div>
    </header>
  );
}
