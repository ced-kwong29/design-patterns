export default function FaucetBase({
  isOn,
  handleAngle,
  children,
  handleToggle
}) {
  return (
    <svg
      viewBox="0 0 220 340"
      width={220}
      height={340}
      onClick={handleToggle}
      style={{ cursor: 'pointer', userSelect: 'none' }}
    >

      <defs>
        <linearGradient id="metalH" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#e8e8e8" />
          <stop offset="45%" stopColor="#c2c2c2" />
          <stop offset="100%" stopColor="#9a9a9a" />
        </linearGradient>
      </defs>

      {/* ── ORIGINAL BASE (unchanged look) ── */}
      <rect x="0" y="88" width="24" height="76" rx="5" fill="#78909c" />
      <rect x="24" y="96" width="116" height="48" rx="11" fill="url(#metalH)" />
      <rect x="112" y="152" width="30" height="95" rx="9" fill="url(#metalH)" />

      {/* handle (keep original) */}
      <g
        style={{
          transform: `translate(90px, 52px) rotate(${handleAngle}deg)`,
          transition: 'transform 0.35s ease'
        }}
      >
        <circle cx="0" cy="0" r="26" fill="#b0bec5" />
      </g>

      {/* ── CATEGORY-SPECIFIC OUTLET ── */}
      {children}

    </svg>
  );
}