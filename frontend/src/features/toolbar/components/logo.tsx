import Link from "next/link";

export function Logo() {
  return (
    <Link href="/" className="flex items-center gap-2 group">
      <img src="/logo.png" alt="FinWally Logo" className="size-12 object-contain" />
      <span className="text-2xl font-bold tracking-tight text-slate-800 transition-colors group-hover:text-slate-950">
        FinWally
      </span>
    </Link>
  );
}
