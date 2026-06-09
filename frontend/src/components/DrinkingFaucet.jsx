export default function DrinkingFaucet({
  isOn,
  handleToggle,
}) {
  const handleRotation = isOn ? 90 : 0;

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
          @keyframes streamFall {
            0% {
              opacity: 0.9;
              transform: translateY(0px);
            }
            100% {
              opacity: 0.5;
              transform: translateY(8px);
            }
          }

          .stream {
            animation: streamFall 0.5s ease-in-out infinite alternate;
          }
        `}</style>

        <linearGradient id="metal" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#ececec" />
          <stop offset="100%" stopColor="#9e9e9e" />
        </linearGradient>
      </defs>

      {/* Base */}
      <rect
        x="70"
        y="230"
        width="80"
        height="20"
        rx="8"
        fill="url(#metal)"
      />

      {/* Left handle */}
      <g transform="translate(75 210)">
        <circle r="16" fill="#bdbdbd" />
        <line
          x1="-10"
          y1="0"
          x2="10"
          y2="0"
          stroke="#616161"
          strokeWidth="3"
        />
        <line
          x1="0"
          y1="-10"
          x2="0"
          y2="10"
          stroke="#616161"
          strokeWidth="3"
        />
      </g>

      {/* Right handle rotates */}
      <g
        style={{
          transform: `translate(145px, 210px) rotate(${handleRotation}deg)`,
          transition: 'transform 0.35s cubic-bezier(0.4,0,0.2,1)',
          transformOrigin: '0px 0px',
        }}
      >
        <circle
          cx="0"
          cy="0"
          r="16"
          fill="#bdbdbd"
          stroke="#757575"
          strokeWidth="2"
        />

        <line
          x1="0"
          y1="-10"
          x2="0"
          y2="10"
          stroke="#0288d1"
          strokeWidth="3"
          strokeLinecap="round"
        />
        <line
          x1="-10"
          y1="0"
          x2="10"
          y2="0"
          stroke="#0288d1"
          strokeWidth="3"
          strokeLinecap="round"
        />
      </g>

      {/* Faucet body */}
      <rect
        x="100"
        y="120"
        width="20"
        height="110"
        rx="10"
        fill="url(#metal)"
      />

      {/* Curved neck */}
      <path
        d="M110 120
           C110 80,
             165 80,
             165 120
           L165 155"
        fill="none"
        stroke="#b0b0b0"
        strokeWidth="18"
        strokeLinecap="round"
      />

      {/* Spout */}
      <rect
        x="155"
        y="150"
        width="20"
        height="12"
        rx="4"
        fill="#9e9e9e"
      />

      {/* Water */}
      {isOn && (
        <rect
          className="stream"
          x="161"
          y="162"
          width="8"
          height="90"
          rx="4"
          fill="#4fc3f7"
        />
      )}

      {!isOn && (
        <text
          x="110"
          y="310"
          textAnchor="middle"
          fill="#90a4ae"
          fontSize="12"
        >
          click faucet to turn on
        </text>
      )}
    </svg>
  );
}