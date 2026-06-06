export function LaundryFaucet({ isOn }) {
  return (
    <svg viewBox="0 0 220 340">
      <rect x="90" y="120" width="40" height="40" fill="#757575" />
      <circle cx="110" cy="140" r="18" fill="#bdbdbd" />

      {isOn && (
        <rect x="108" y="160" width="4" height="100" fill="#4fc3f7" />
      )}
    </svg>
  );
}