export default function LaundryFaucet({ isOn, handleToggle }) {
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
          @keyframes bubbleRise {
            0% {
              transform: translateY(0px);
              opacity: 0.8;
            }
            100% {
              transform: translateY(-80px);
              opacity: 0;
            }
          }

          .bubble1 {
            animation: bubbleRise 2s linear infinite;
          }

          .bubble2 {
            animation: bubbleRise 2s linear infinite 0.6s;
          }

          .bubble3 {
            animation: bubbleRise 2s linear infinite 1.2s;
          }
        `}</style>

        <linearGradient id="machineBody" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#f5f5f5" />
          <stop offset="100%" stopColor="#cfd8dc" />
        </linearGradient>

        <linearGradient id="glass" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#b3e5fc" />
          <stop offset="100%" stopColor="#4fc3f7" />
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

      {/* Machine body */}
      <rect
        x="40"
        y="60"
        width="140"
        height="180"
        rx="12"
        fill="url(#machineBody)"
        filter="url(#shadow)"
      />

      {/* Top control panel */}
      <rect
        x="40"
        y="60"
        width="140"
        height="40"
        rx="12"
        fill="#b0bec5"
      />

      {/* Knob */}
      <circle
        cx="75"
        cy="80"
        r="12"
        fill="#eceff1"
        stroke="#90a4ae"
        strokeWidth="2"
      />

      {/* Indicator lights */}
      <circle cx="130" cy="80" r="4" fill="#81c784" />
      <circle cx="145" cy="80" r="4" fill="#ffb74d" />
      <circle cx="160" cy="80" r="4" fill="#e57373" />

      {/* Door frame */}
      <circle
        cx="110"
        cy="165"
        r="52"
        fill="#90a4ae"
      />

      {/* Glass */}
      <circle
        cx="110"
        cy="165"
        r="40"
        fill="url(#glass)"
      />

      {/* Water inside drum */}
      {isOn && (
        <>
          {/* Bubbles inside drum */}
          <circle cx="95" cy="175" r="4" fill="white" opacity="0.7" />
          <circle cx="120" cy="185" r="3" fill="white" opacity="0.7" />
          <circle cx="130" cy="170" r="5" fill="white" opacity="0.7" />
        </>
      )}

      {/* Rising bubbles */}
      {isOn && (
        <g>
          <circle
            className="bubble1"
            cx="95"
            cy="105"
            r="6"
            fill="#b3e5fc"
            opacity="0.8"
          />

          <circle
            className="bubble2"
            cx="120"
            cy="110"
            r="8"
            fill="#81d4fa"
            opacity="0.8"
          />

          <circle
            className="bubble3"
            cx="145"
            cy="100"
            r="5"
            fill="#e1f5fe"
            opacity="0.8"
          />
        </g>
      )}

      {!isOn && (
        <text
          x="110"
          y="295"
          textAnchor="middle"
          fill="#90a4ae"
          fontSize="12"
          fontFamily="sans-serif"
        >
          click washer to start
        </text>
      )}
    </svg>
  );
}