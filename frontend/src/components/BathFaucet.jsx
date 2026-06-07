export default function BathFaucet({ isOn, handleToggle }) {
  return (
    <svg
      viewBox="0 0 220 340"
      width={220}
      height={340}
      onClick={handleToggle}
      style={{
        cursor: 'pointer',
        userSelect: 'none',
        display: 'block',
      }}
    >
      <defs>
        <style>{`
          @keyframes bathFlow {
            0%   { opacity: 0.95; transform: translateY(0px); }
            100% { opacity: 0.75; transform: translateY(8px); }
          }

          .bath-water {
            animation: bathFlow 0.4s ease-in-out infinite alternate;
          }
        `}</style>

        <linearGradient id="metal" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#ececec" />
          <stop offset="45%" stopColor="#c5c5c5" />
          <stop offset="100%" stopColor="#8f8f8f" />
        </linearGradient>

        <linearGradient id="water" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#81d4fa" />
          <stop offset="100%" stopColor="#29b6f6" />
        </linearGradient>

        <filter id="shadow">
          <feDropShadow
            dx="2"
            dy="3"
            stdDeviation="3"
            floodOpacity="0.25"
          />
        </filter>
      </defs>

      {/* Wall mount plate */}
      <rect
        x="10"
        y="100"
        width="22"
        height="60"
        rx="4"
        fill="#78909c"
        filter="url(#shadow)"
      />

      {/* Main body */}
      <rect
        x="30"
        y="110"
        width="110"
        height="40"
        rx="10"
        fill="url(#metal)"
        filter="url(#shadow)"
      />

      {/* Highlight */}
      <rect
        x="35"
        y="114"
        width="100"
        height="10"
        rx="5"
        fill="white"
        opacity="0.25"
      />

      {/* Downturned tub spout */}
      <path
        d="M140 120 H175 V155 H155 V140 H140 Z"
        fill="url(#metal)"
        filter="url(#shadow)"
      />

      {/* Aerator */}
      <ellipse
        cx="165"
        cy="155"
        rx="10"
        ry="4"
        fill="#607d8b"
      />

      {/* Water stream */}
      {isOn && (
        <g>
          <rect
            className="bath-water"
            x="157"
            y="155"
            width="16"
            height="95"
            rx="8"
            fill="url(#water)"
            opacity="0.9"
          />

          {/* splash */}
          <ellipse
            cx="165"
            cy="250"
            rx="18"
            ry="5"
            fill="#81d4fa"
            opacity="0.7"
          />

          <circle
            cx="157"
            cy="243"
            r="3"
            fill="#4fc3f7"
          />

          <circle
            cx="173"
            cy="240"
            r="2.5"
            fill="#4fc3f7"
          />
        </g>
      )}

      {!isOn && (
        <text
          x="110"
          y="325"
          textAnchor="middle"
          fill="#90a4ae"
          fontSize="12"
          fontFamily="sans-serif"
        >
          click bath faucet to turn on
        </text>
      )}
    </svg>
  );
}