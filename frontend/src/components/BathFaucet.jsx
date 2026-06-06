export default function BathFaucet({ isOn, handleToggle }) {
  return (
    <svg viewBox="0 0 220 340" width={220} height={340} onClick={handleToggle}>

      {/* wall connection */}
      <rect x="50" y="110" width="120" height="40" rx="10" fill="#cfd8dc" />

      {/* tub spout (wide + horizontal) */}
      <rect x="85" y="150" width="90" height="18" rx="8" fill="#90a4ae" />

      {/* faucet base */}
      <circle cx="95" cy="140" r="10" fill="#78909c" />
      <circle cx="145" cy="140" r="10" fill="#78909c" />

      {/* handles */}
      <circle cx="95" cy="140" r="4" fill="#546e7a" />
      <circle cx="145" cy="140" r="4" fill="#546e7a" />

      {/* heavy bath flow */}
      {isOn && (
        <g>
          <rect
            x="125"
            y="168"
            width="14"
            height="120"
            fill="#4fc3f7"
            opacity="0.85"
          />

          {/* splash hint */}
          <circle cx="132" cy="250" r="5" fill="#81d4fa" />
          <circle cx="128" cy="270" r="3" fill="#b3e5fc" />
        </g>
      )}
    </svg>
  );
}