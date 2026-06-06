export default function LaundryFaucet({ isOn, handleToggle }) {
  return (
    <svg viewBox="0 0 220 340" width={220} height={340} onClick={handleToggle}>

      {/* utility wall panel */}
      <rect x="70" y="90" width="80" height="140" rx="10" fill="#eceff1" />

      {/* pipe */}
      <rect x="108" y="120" width="6" height="100" fill="#90a4ae" />

      {/* valve wheel */}
      <circle cx="111" cy="115" r="18" fill="#78909c" />
      <circle cx="111" cy="115" r="6" fill="#546e7a" />

      {/* valve spokes */}
      <line x1="111" y1="97" x2="111" y2="133" stroke="#455a64" strokeWidth="2" />
      <line x1="93" y1="115" x2="129" y2="115" stroke="#455a64" strokeWidth="2" />

      {/* outlet hose */}
      <rect x="108" y="220" width="6" height="60" fill="#616161" />

      {/* thin controlled flow */}
      {isOn && (
        <rect
          x="109"
          y="280"
          width="4"
          height="50"
          fill="#4fc3f7"
          opacity="0.8"
        />
      )}
    </svg>
  );
}