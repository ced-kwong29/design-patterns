export default function ShowerFaucet({ isOn, handleToggle }) {
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
          @keyframes waterFall {
            0%   { opacity: 0.9; transform: translateY(0px); }
            100% { opacity: 0.2; transform: translateY(40px); }
          }

          .water-stream {
            animation: waterFall 0.6s linear infinite;
          }
        `}</style>

        <linearGradient id="metal" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#ececec" />
          <stop offset="50%" stopColor="#c5c5c5" />
          <stop offset="100%" stopColor="#8f8f8f" />
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

      {/* Wall mount */}
      <rect
        x="20"
        y="70"
        width="18"
        height="70"
        rx="4"
        fill="#78909c"
        filter="url(#shadow)"
      />

      {/* Arm */}
      <rect
        x="35"
        y="90"
        width="90"
        height="18"
        rx="8"
        fill="url(#metal)"
        filter="url(#shadow)"
      />

      {/* Bend */}
      <rect
        x="115"
        y="90"
        width="18"
        height="45"
        rx="8"
        fill="url(#metal)"
      />

      {/* Shower head */}
      <ellipse
        cx="124"
        cy="145"
        rx="34"
        ry="16"
        fill="url(#metal)"
        filter="url(#shadow)"
      />

      {/* Face of shower head */}
      <ellipse
        cx="124"
        cy="145"
        rx="28"
        ry="11"
        fill="#90a4ae"
      />

      {/* Spray holes */}
      {[104, 112, 120, 128, 136, 144].map(x => (
        <circle
          key={x}
          cx={x}
          cy={145}
          r="1.8"
          fill="#546e7a"
        />
      ))}

      {/* Water */}
      {isOn && (
        <g>
          {[104, 112, 120, 128, 136, 144].map((x, i) => (
            <rect
              key={i}
              className="water-stream"
              x={x - 1}
              y={150}
              width="2"
              height="90"
              rx="1"
              fill="#4fc3f7"
              opacity="0.85"
            />
          ))}
        </g>
      )}

      {!isOn && (
        <text
          x="110"
          y="300"
          textAnchor="middle"
          fill="#90a4ae"
          fontSize="12"
          fontFamily="sans-serif"
        >
          click shower to turn on
        </text>
      )}
    </svg>
  );
}