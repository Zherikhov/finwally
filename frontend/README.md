# FinWally Frontend

Frontend of FinWally built with Next.js (App Router), TypeScript and Tailwind CSS v4.

## Scripts

- `npm run dev` - start local development server
- `npm run build` - production build
- `npm run start` - run built app
- `npm run lint` - run ESLint
- `npm run lint:fix` - auto-fix ESLint issues
- `npm run typecheck` - TypeScript type check
- `npm run check` - run lint + typecheck

## Structure

- `src/app` - app router pages/layout and global styles
- `src/features` - feature modules
- `src/components/ui` - shared UI primitives
- `src/lib` - utilities

## Notes

- Navigation and toolbar were refactored to remove legacy Next.js patterns.
- Unused assets and dead provider layer were removed.
- Project quality checks are aligned with Next.js 16 (`eslint` + `tsc`).
