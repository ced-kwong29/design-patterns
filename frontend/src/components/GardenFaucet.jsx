export default function GardenFaucet({ isOn, handleToggle }) {
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
          @keyframes drip {
            0% {
              transform: translateY(0px);
              opacity: 1;
            }
            100% {
              transform: translateY(70px);
              opacity: 0;
            }
          }

          .drop1 {
            animation: drip 0.8s linear infinite;
          }

          .drop2 {
            animation: drip 0.8s linear infinite 0.3s;
          }

          .drop3 {
            animation: drip 0.8s linear infinite 0.6s;
          }
        `}</style>
      </defs>

      {/* Reel */}
      <circle
        cx="110"
        cy="140"
        r="55"
        fill="#90a4ae"
      />

      {/* Hose coils */}
      <circle
        cx="110"
        cy="140"
        r="45"
        fill="none"
        stroke="#4caf50"
        strokeWidth="8"
      />

      <circle
        cx="110"
        cy="140"
        r="30"
        fill="none"
        stroke="#66bb6a"
        strokeWidth="8"
      />

      <circle
        cx="110"
        cy="140"
        r="15"
        fill="none"
        stroke="#81c784"
        strokeWidth="8"
      />

      {/* Hose extension */}
      <path
        d="M165 140 C190 140 190 180 175 200"
        fill="none"
        stroke="#4caf50"
        strokeWidth="8"
        strokeLinecap="round"
      />

      {/* Nozzle */}
      <rect
        x="165"
        y="195"
        width="20"
        height="35"
        rx="4"
        fill="#78909c"
      />

      {isOn && (
        <>
          <ellipse className="drop1" cx="175" cy="230" rx="4" ry="7" fill="#4fc3f7" />
          <ellipse className="drop2" cx="171" cy="245" rx="3" ry="6" fill="#81d4fa" />
          <ellipse className="drop3" cx="179" cy="260" rx="3" ry="5" fill="#b3e5fc" />
        </>
      )}

      {!isOn && (
        <text
          x="110"
          y="310"
          textAnchor="middle"
          fill="#90a4ae"
          fontSize="12"
        >
          click hose reel to start
        </text>
      )}
    </svg>
  );
}