// DishwasherFaucet.jsx
export default function DishwasherFaucet({ isOn, handleToggle }) {
  return (
    <svg viewBox="0 0 220 340" width={220} height={340} onClick={handleToggle}>
      
      {/* under-sink wall */}
      <rect x="60" y="80" width="100" height="120" rx="10" fill="#eceff1" />

      {/* dual valve */}
      <circle cx="95" cy="140" r="14" fill="#90a4ae" />
      <circle cx="125" cy="140" r="14" fill="#90a4ae" />

      <circle cx="95" cy="140" r="6" fill="#546e7a" />
      <circle cx="125" cy="140" r="6" fill="#546e7a" />

      {/* hose line */}
      <rect x="118" y="154" width="6" height="80" fill="#78909c" />

      {/* flow (subtle) */}
      {isOn && (
        <rect
          x="118"
          y="234"
          width="6"
          height="70"
          fill="#4fc3f7"
          opacity="0.8"
        />
      )}
    </svg>
  );
}