// GardenFaucet.jsx
export default function GardenFaucet({ isOn }) {
  return (
    <svg viewBox="0 0 220 340" width={220} height={340}>

      {/* exterior wall */}
      <rect x="40" y="70" width="140" height="160" rx="12" fill="#c8e6c9" />

      {/* spigot base */}
      <rect x="90" y="120" width="40" height="20" fill="#8d6e63" />

      {/* valve handle */}
      <circle cx="110" cy="130" r="16" fill="#a1887f" />
      <line x1="110" y1="118" x2="110" y2="142" stroke="#5d4037" strokeWidth="4" />

      {/* hose */}
      <rect x="110" y="140" width="10" height="60" fill="#546e7a" />

      {/* strong jet stream */}
      {isOn && (
        <g>
          <rect
            x="112"
            y="200"
            width="6"
            height="90"
            fill="#29b6f6"
            opacity="0.9"
          />

          {/* splash drops */}
          <circle cx="118" cy="240" r="4" fill="#4dd0e1" opacity="0.8" />
          <circle cx="108" cy="260" r="3" fill="#80deea" opacity="0.7" />
          <circle cx="122" cy="280" r="2.5" fill="#b2ebf2" opacity="0.6" />
        </g>
      )}
    </svg>
  );
}