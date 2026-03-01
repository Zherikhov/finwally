import Link from "next/link";

export function Logo() {
  return (
    <Link href="/" className="flex items-center gap-2 group">
      <div className="relative flex size-8 items-center justify-center overflow-hidden rounded-lg bg-emerald-500 shadow-sm transition-colors group-hover:bg-emerald-600">
        <div className="absolute top-1 right-1 w-3 h-3 bg-white rounded-full opacity-20" />
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="white"
          strokeWidth="3"
          strokeLinecap="round"
          strokeLinejoin="round"
          aria-hidden="true"
        >
          <rect x="2" y="6" width="20" height="12" rx="2" />
          <path d="M12 12h.01" />
        </svg>
      </div>
      <span className="text-2xl font-bold tracking-tight text-slate-800 transition-colors group-hover:text-slate-950">
        FinWally
      </span>
    </Link>
  );
}
