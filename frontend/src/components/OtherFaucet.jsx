export default function OtherFaucet({ isOn, handleAngle, handleToggle }) {
  return (
    <svg
        viewBox="0 0 220 340"
        width={220}
        height={340}
        onClick={handleToggle}
        style={{ cursor: 'pointer', userSelect: 'none', display: 'block' }}
    >
        <defs>
        <style>{`
            @keyframes streamFall {
            0%   { transform: translateY(0px);  opacity: 0.95; }
            100% { transform: translateY(70px); opacity: 0;    }
            }
            @keyframes dropFall {
            0%   { transform: translateY(0px);  opacity: 0.85; }
            80%  { opacity: 0.5; }
            100% { transform: translateY(85px); opacity: 0;    }
            }
            .stream      { animation: streamFall 0.55s linear infinite; }
            .drop-a      { animation: dropFall   0.85s ease-in infinite 0s;    }
            .drop-b      { animation: dropFall   0.85s ease-in infinite 0.28s; }
            .drop-c      { animation: dropFall   0.85s ease-in infinite 0.56s; }
        `}</style>

        <linearGradient id="metalH" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%"   stopColor="#e8e8e8"/>
            <stop offset="45%"  stopColor="#c2c2c2"/>
            <stop offset="100%" stopColor="#9a9a9a"/>
        </linearGradient>
        <linearGradient id="knobG" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%"   stopColor={isOn ? '#81d4fa' : '#f0f0f0'}/>
            <stop offset="100%" stopColor={isOn ? '#0288d1' : '#b0b0b0'}/>
        </linearGradient>
        <filter id="dropshadow" x="-20%" y="-20%" width="140%" height="140%">
            <feDropShadow dx="2" dy="3" stdDeviation="3" floodOpacity="0.22"/>
        </filter>
        </defs>

        {/* Wall plate */}
        <rect x="0" y="88" width="24" height="76" rx="5"
            fill="#78909c" filter="url(#dropshadow)"/>
        <rect x="0" y="88" width="9"  height="76" rx="4"
            fill="#90a4ae" opacity="0.55"/>

        {/* Horizontal pipe body */}
        <rect x="24" y="96" width="116" height="48" rx="11"
            fill="url(#metalH)" filter="url(#dropshadow)"/>
        <rect x="30" y="99" width="106" height="13" rx="6"
            fill="white" opacity="0.22"/>

        {/* Pipe-to-spout connector */}
        <rect x="114" y="142" width="30" height="12" rx="4" fill="#b0b0b0"/>

        {/* Vertical spout neck */}
        <rect x="112" y="152" width="30" height="95" rx="9"
            fill="url(#metalH)" filter="url(#dropshadow)"/>
        <rect x="114" y="154" width="9" height="91" rx="4"
            fill="white" opacity="0.18"/>

        {/* Aerator cap */}
        <ellipse cx="127" cy="248" rx="18" ry="8"  fill="#607d8b"/>
        <ellipse cx="127" cy="244" rx="14" ry="5.5" fill="#78909c"/>
        <ellipse cx="127" cy="242" rx="10" ry="3.5" fill="#90a4ae"/>

        {/* Water stream (when on) */}
        {isOn && (
        <g>
            <rect className="stream"
                x="121" y="255" width="12" height="45" rx="6"
                fill="#4fc3f7" opacity="0.88"/>
            <ellipse className="drop-a" cx="127" cy="268" rx="8" ry="4.5"
                    fill="#29b6f6" opacity="0.82"/>
            <ellipse className="drop-b" cx="124" cy="285" rx="6" ry="3.5"
                    fill="#4dd0e1" opacity="0.70"/>
            <ellipse className="drop-c" cx="130" cy="298" rx="5" ry="3"
                    fill="#80deea" opacity="0.58"/>
        </g>
        )}

        {/* Handle stem */}
        <rect x="78" y="68" width="24" height="30" rx="5" fill="#9e9e9e"/>
        <rect x="80" y="68" width="9"  height="30" fill="#bdbdbd" opacity="0.45"/>

        {/* Rotatable handle knob – centred at (90, 52) */}
        <g style={{
        transform: `translate(90px, 52px) rotate(${handleAngle}deg)`,
        transition: 'transform 0.35s cubic-bezier(0.4,0,0.2,1)',
        }}>
        <circle cx="0" cy="0" r="26"
                fill="url(#knobG)" stroke="#757575" strokeWidth="2.5"
                filter="url(#dropshadow)"/>
        {/* Cross grip */}
        <line x1="0" y1="-18" x2="0"  y2="18"
                stroke={isOn ? '#01579b' : '#757575'} strokeWidth="5" strokeLinecap="round"/>
        <line x1="-18" y1="0" x2="18" y2="0"
                stroke={isOn ? '#01579b' : '#757575'} strokeWidth="5" strokeLinecap="round"/>
        <circle cx="0" cy="0" r="5.5" fill={isOn ? '#01579b' : '#616161'}/>
        </g>

        {/* "click to turn on" hint */}
        {!isOn && (
        <text x="110" y="330" textAnchor="middle"
                fill="#90a4ae" fontSize="12" fontFamily="sans-serif">
            click faucet to turn on
        </text>
        )}
    </svg>
  );
}