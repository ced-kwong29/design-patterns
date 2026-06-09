export default function DishwasherFaucet({ isOn, handleToggle }) {
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
              opacity: 0.9;
            }
            100% {
              transform: translateY(-50px);
              opacity: 0;
            }
          }

          .bubble1 {
            animation: bubbleRise 1.8s linear infinite;
          }

          .bubble2 {
            animation: bubbleRise 1.8s linear infinite 0.6s;
          }

          .bubble3 {
            animation: bubbleRise 1.8s linear infinite 1.2s;
          }

          @keyframes waterShimmer {
            0% { opacity: 0.7; }
            50% { opacity: 1; }
            100% { opacity: 0.7; }
          }

          .water {
            animation: waterShimmer 1.2s ease-in-out infinite;
          }
        `}</style>

        <linearGradient id="steel" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#f2f2f2" />
          <stop offset="100%" stopColor="#b0bec5" />
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

      {/* Dishwasher body */}
      <rect
        x="45"
        y="50"
        width="130"
        height="220"
        rx="10"
        fill="url(#steel)"
        filter="url(#shadow)"
      />

      {/* Control panel */}
      <rect
        x="45"
        y="50"
        width="130"
        height="35"
        rx="10"
        fill="#90a4ae"
      />

      {/* Buttons */}
      <circle cx="65" cy="68" r="4" fill="#81c784" />
      <circle cx="80" cy="68" r="4" fill="#ffb74d" />
      <circle cx="95" cy="68" r="4" fill="#e57373" />

      {/* Handle */}
      <rect
        x="95"
        y="95"
        width="30"
        height="5"
        rx="3"
        fill="#546e7a"
      />

      {/* Window */}
      <rect
        x="65"
        y="115"
        width="90"
        height="110"
        rx="8"
        fill="#dff3ff"
        stroke="#90a4ae"
        strokeWidth="3"
      />

      {/* Dish rack */}
      <line x1="75" y1="180" x2="145" y2="180" stroke="#78909c" strokeWidth="2" />

      {/* Plates */}
      <circle cx="90" cy="165" r="12" fill="#ffffff" stroke="#b0bec5" />
      <circle cx="110" cy="170" r="12" fill="#ffffff" stroke="#b0bec5" />
      <circle cx="130" cy="165" r="12" fill="#ffffff" stroke="#b0bec5" />

      {/* Running animation */}
      {isOn && (
        <>
          {/* Water level */}
          <rect
            className="water"
            x="67"
            y="185"
            width="86"
            height="38"
            rx="4"
            fill="#4fc3f7"
            opacity="0.65"
          />

          {/* Internal bubbles */}
          <circle cx="90" cy="205" r="4" fill="white" opacity="0.8" />
          <circle cx="115" cy="195" r="5" fill="white" opacity="0.8" />
          <circle cx="135" cy="210" r="3" fill="white" opacity="0.8" />

          {/* Rising bubbles */}
          <circle
            className="bubble1"
            cx="90"
            cy="130"
            r="6"
            fill="#e1f5fe"
          />

          <circle
            className="bubble2"
            cx="120"
            cy="140"
            r="8"
            fill="#b3e5fc"
          />

          <circle
            className="bubble3"
            cx="145"
            cy="135"
            r="5"
            fill="#81d4fa"
          />
        </>
      )}

      {!isOn && (
        <text
          x="110"
          y="310"
          textAnchor="middle"
          fill="#90a4ae"
          fontSize="12"
          fontFamily="sans-serif"
        >
          click dishwasher to start
        </text>
      )}
    </svg>
  );
}