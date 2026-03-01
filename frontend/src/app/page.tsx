import Link from "next/link";

import { Button } from "@/components/ui/button";

const keyBenefits = [
  {
    title: "Expense Tracking",
    description: "Monitor daily spending and instantly see where your money goes.",
  },
  {
    title: "Budget Planning",
    description: "Set monthly limits by category and stay aligned with your goals.",
  },
  {
    title: "Goal Progress",
    description: "Track savings milestones with clear visual progress updates.",
  },
] as const;

const faqItems = [
  {
    question: "Is FinWally free to start?",
    answer: "Yes. You can create an account and begin tracking your finances at no cost.",
  },
  {
    question: "Can I use it from my phone?",
    answer: "Yes. The interface is optimized for both mobile and desktop.",
  },
  {
    question: "Do I need accounting knowledge?",
    answer: "No. The core workflows are designed for everyday users.",
  },
] as const;

export default function HomePage() {
  return (
    <div className="mx-auto w-full max-w-6xl px-4 py-16 sm:px-6 lg:px-8">
      <section className="mx-auto max-w-3xl text-center">
        <h1 className="text-4xl font-extrabold tracking-tight text-slate-900 sm:text-5xl">
          Take control of your finances with less effort
        </h1>
        <p className="mt-6 text-lg text-slate-600">
          Track spending, build better habits, and move toward your goals with confidence.
        </p>
        <div className="mt-10 flex flex-wrap items-center justify-center gap-3">
          <Button asChild className="rounded-md bg-emerald-500 px-6 font-semibold text-white hover:bg-emerald-600">
            <Link href="/signup">Start for free</Link>
          </Button>
          <Button asChild variant="outline">
            <Link href="#features">View features</Link>
          </Button>
        </div>
      </section>

      <section id="features" className="mt-20 grid gap-4 md:grid-cols-3">
        {keyBenefits.map((item) => (
          <article key={item.title} className="rounded-xl border bg-card p-6">
            <h2 className="text-xl font-semibold text-slate-900">{item.title}</h2>
            <p className="mt-3 text-sm leading-6 text-slate-600">{item.description}</p>
          </article>
        ))}
      </section>

      <section id="about" className="mt-20 rounded-xl border bg-muted/40 p-8">
        <h2 className="text-2xl font-bold tracking-tight text-slate-900">About FinWally</h2>
        <p className="mt-4 max-w-3xl text-slate-600">
          FinWally is focused on simple money management: fewer clicks, clearer insights, and practical actions
          that help you stay in control every month.
        </p>
      </section>

      <section id="faq" className="mt-20">
        <h2 className="text-2xl font-bold tracking-tight text-slate-900">FAQ</h2>
        <div className="mt-6 grid gap-4 md:grid-cols-3">
          {faqItems.map((item) => (
            <article key={item.question} className="rounded-xl border bg-card p-6">
              <h3 className="font-semibold text-slate-900">{item.question}</h3>
              <p className="mt-3 text-sm leading-6 text-slate-600">{item.answer}</p>
            </article>
          ))}
        </div>
      </section>

      <section className="mt-20 rounded-xl border bg-emerald-50 p-8 text-center">
        <h2 className="text-2xl font-bold tracking-tight text-slate-900">Ready to start?</h2>
        <p className="mt-3 text-slate-700">Create your account and build a healthier financial routine.</p>
        <Button asChild className="mt-6 rounded-md bg-emerald-500 px-6 font-semibold text-white hover:bg-emerald-600">
          <Link href="/signup">Create account</Link>
        </Button>
      </section>
    </div>
  );
}
