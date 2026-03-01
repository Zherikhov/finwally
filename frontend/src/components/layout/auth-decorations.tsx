import React from 'react';

export const TopDecorations = () => {
  return (
    <div className="absolute top-0 left-0 w-full overflow-hidden leading-none pointer-events-none">
      {/* Wave Layers - Top (Matches bottom waves) */}
      <div className="relative h-48 sm:h-56 md:h-64 lg:h-72">
        <svg
          viewBox="0 0 1440 250"
          className="absolute top-0 left-0 w-full h-full"
          preserveAspectRatio="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <defs>
            <linearGradient id="top-wave-gradient" x1="0%" y1="100%" x2="0%" y2="0%">
              <stop offset="0%" stopColor="#D6EFFF" />
              <stop offset="100%" stopColor="#A5D8FF" />
            </linearGradient>
          </defs>
          
          {/* Background Wave - Inverted */}
          <path
            fill="url(#top-wave-gradient)"
            fillOpacity="0.4"
            d="M0,167L60,175C120,183,240,199,360,195C480,191,600,167,720,163C840,159,960,175,1080,195C1200,215,1320,239,1380,251L1440,263L1440,0L1380,0C1320,0,1200,0,1080,0C960,0,840,0,720,0C600,0,480,0,360,0C240,0,120,0,60,0L0,0Z"
          ></path>

          {/* Middle Wave - Inverted */}
          <path
            fill="url(#top-wave-gradient)"
            fillOpacity="0.7"
            d="M0,143L80,155C160,167,320,191,480,187C640,183,800,151,960,155C1120,159,1280,199,1360,219L1440,239L1440,0L1360,0C1280,0,1120,0,960,0C800,0,640,0,480,0C320,0,160,0,80,0L0,0Z"
          ></path>

          {/* Front Wave - Inverted */}
          <path
            fill="#89CFF0"
            fillOpacity="0.5"
            d="M0,119L120,135C240,151,480,183,720,179C960,175,1200,135,1320,115L1440,95L1440,0L1320,0C1200,0,960,0,720,0C480,0,240,0,120,0L0,0Z"
          ></path>
        </svg>
      </div>
    </div>
  );
};

export const BottomDecorations = () => {
  return (
    <div className="absolute bottom-0 left-0 w-full overflow-hidden leading-none pointer-events-none">
      <svg
        viewBox="0 0 1440 320"
        className="relative block w-full h-48 sm:h-64 md:h-80"
        preserveAspectRatio="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <defs>
          <linearGradient id="wave-gradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stopColor="#D6EFFF" />
            <stop offset="100%" stopColor="#A5D8FF" />
          </linearGradient>
        </defs>
        
        {/* Background Wave */}
        <path
          fill="url(#wave-gradient)"
          fillOpacity="0.4"
          d="M0,184L60,173.3C120,163,240,141,360,146.7C480,152,600,184,720,189.3C840,195,960,173,1080,146.7C1200,120,1320,88,1380,72L1440,56L1440,320L1380,320C1320,320,1200,320,1080,320C960,320,840,320,720,320C600,320,480,320,360,320C240,320,120,320,60,320L0,320Z"
        ></path>

        {/* Middle Wave */}
        <path
          fill="url(#wave-gradient)"
          fillOpacity="0.7"
          d="M0,216L80,200C160,184,320,152,480,157.3C640,163,800,205,960,200C1120,195,1280,141,1360,114.7L1440,88L1440,320L1360,320C1280,320,1120,320,960,320C800,320,640,320,480,320C320,320,160,320,80,320L0,320Z"
        ></path>

        {/* Front Wave */}
        <path
          fill="#89CFF0"
          fillOpacity="0.5"
          d="M0,248L120,226.7C240,205,480,163,720,168C960,173,1200,227,1320,253.3L1440,280L1440,320L1320,320C1200,320,960,320,720,320C480,320,240,320,120,320L0,320Z"
        ></path>
      </svg>
    </div>
  );
};
