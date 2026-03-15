'use client';

import { Logo } from "./components/logo";
import { NavLinks } from "./components/nav-links";
import { AuthButtons } from "./components/auth-buttons";
import { UserAvatarMenu } from "./components/user-avatar-menu";
import { usePathname } from "next/navigation";

export function Toolbar() {
  const pathname = usePathname();
  const isHomePage = pathname === "/home";

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-[#F8F9FB] backdrop-blur-md">
      <div className="mx-auto w-full max-w-[1440px] px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between gap-6">
          <Logo />
          <div className="flex items-center gap-4 sm:gap-8">
            <nav aria-label="Primary navigation" className="hidden md:block">
              <NavLinks />
            </nav>
            <AuthButtons />
            {isHomePage && <UserAvatarMenu />}
          </div>
        </div>
      </div>
    </header>
  );
}
