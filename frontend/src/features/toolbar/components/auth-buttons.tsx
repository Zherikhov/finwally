'use client';

import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import Link from "next/link";

export function AuthButtons() {
  const { user } = useAuth();

  if (user) {
    return null;
  }

  return (
    <div className="flex items-center gap-3">
      <Button asChild variant="ghost" className="text-muted-foreground hover:text-foreground">
        <Link href="/login">Log in</Link>
      </Button>
      <Button asChild className="rounded-md bg-emerald-500 px-6 font-semibold text-white shadow-sm hover:bg-emerald-600">
        <Link href="/signup">
          Sign Up
        </Link>
      </Button>
    </div>
  );
}
