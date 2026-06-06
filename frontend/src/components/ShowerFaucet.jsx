export default function ShowerFaucet({ isOn, handleToggle }) {
  return (
    <svg viewBox="0 0 220 340" width={220} height={340} onClick={handleToggle}>


      {/* same pipe base OR simplified version */}

      {/* shower head */}
      <circle cx="127" cy="120" r="28" fill="#90a4ae" />

      {/* rain streams */}
      {isOn && (
        <g>
          {Array.from({ length: 12 }).map((_, i) => (
            <rect
              key={i}
              x={110 + i * 3}
              y={150}
              width="2"
              height="60"
              fill="#4fc3f7"
              opacity="0.7"
            />
          ))}
        </g>
      )}
    </svg>
  );
}