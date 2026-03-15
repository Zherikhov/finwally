'use client';

import { useAuth } from "@/lib/auth-context";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { cn } from "@/lib/utils";

const tabs = [
  { id: 'overview', label: 'Overview' },
  { id: 'wallets', label: 'Wallets' },
  { id: 'budgets', label: 'Budgets' },
  { id: 'goals', label: 'Goals' },
  { id: 'insights', label: 'Insights' },
  { id: 'settings', label: 'Settings' },
];

export default function HomePage() {
  const { user, isLoading } = useAuth();
  const router = useRouter();
  const [activeTab, setActiveTab] = useState('overview');

  useEffect(() => {
    if (!isLoading && !user) {
      router.push('/login');
    }
  }, [user, isLoading, router]);

  if (isLoading || !user) {
    return (
      <div className="flex min-h-[calc(100vh-64px)] items-center justify-center">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  const activeTabLabel = tabs.find(tab => tab.id === activeTab)?.label;

  return (
    <div className="w-full py-8 px-4 sm:px-6 lg:px-8 bg-[#F8F9FB] min-h-[calc(100vh-64px)]">
      <div className="max-w-[1440px] mx-auto flex gap-8">
        {/* Sidebar with Tabs */}
        <aside className="w-64 flex-shrink-0">
          <div className="bg-white rounded-[20px] p-4 shadow-sm border border-slate-100 min-h-[500px]">
            <nav className="space-y-1">
              {tabs.map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={cn(
                    "w-full text-left px-6 py-3 rounded-xl text-sm font-medium transition-colors",
                    activeTab === tab.id
                      ? "bg-[#F3F4F6] text-slate-900"
                      : "text-slate-500 hover:bg-slate-50 hover:text-slate-700"
                  )}
                >
                  {tab.label}
                </button>
              ))}
            </nav>
          </div>
        </aside>

        {/* Workspace Area */}
        <main className="flex-grow">
          <div className="bg-transparent">
            <h1 className="text-[40px] font-bold text-slate-900 leading-tight">
              {activeTabLabel}
            </h1>
          </div>
        </main>
      </div>
    </div>
  );
}
