// DrinkingFaucet.jsx
export default function DrinkingFaucet({ isOn, handleToggle }) {
  return (
    <svg viewBox="0 0 220 340" width={220} height={340} onClick={handleToggle}>

      {/* sleek counter */}
      <rect x="50" y="180" width="120" height="40" rx="8" fill="#eceff1" />

      {/* thin faucet neck */}
      <rect x="105" y="120" width="10" height="60" fill="#90a4ae" />

      {/* curved spout */}
      <path
        d="M110 120 C110 90, 140 90, 140 120"
        fill="none"
        stroke="#78909c"
        strokeWidth="6"
        strokeLinecap="round"
      />

      {/* clean filtered stream */}
      {isOn && (
        <g>
          <rect
            x="136"
            y="120"
            width="3"
            height="110"
            fill="#4fc3f7"
            opacity="0.85"
          />

          {/* tiny clean droplets */}
          <circle cx="138" cy="150" r="2.5" fill="#81d4fa" />
          <circle cx="135" cy="175" r="2" fill="#b3e5fc" />
          <circle cx="140" cy="200" r="2.5" fill="#81d4fa" />
        </g>
      )}
    </svg>
  );
}