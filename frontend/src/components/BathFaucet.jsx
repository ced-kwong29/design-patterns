export function BathFaucet({ isOn }) {
  return (
    <svg viewBox="0 0 220 340">
      {/* short spout */}
      <rect x="80" y="150" width="70" height="20" rx="8" fill="#b0b0b0" />

      {isOn && (
        <rect x="110" y="170" width="10" height="80" fill="#64b5f6" />
      )}
    </svg>
  );
}