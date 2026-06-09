import { useState, useEffect, useRef } from 'react';
import { logUsage } from '../api';
import OtherFaucet from './OtherFaucet';
import ShowerFaucet from './ShowerFaucet';
import BathFaucet from './BathFaucet';
import LaundryFaucet from './LaundryFaucet';
import DishwasherFaucet from './DishwasherFaucet';
import GardenFaucet from './GardenFaucet';
import DrinkingFaucet from './DrinkingFaucet';

const CATEGORIES = ['SHOWER', 'BATH', 'LAUNDRY', 'DISHWASHER', 'GARDEN', 'DRINKING', 'OTHER'];
const FLOW_RATES = { slow: 4, medium: 8, fast: 16 }; // litres per minute

const USER_ID = 1;

const FaucetMap = {
  SHOWER: ShowerFaucet,
  BATH: BathFaucet,
  LAUNDRY: LaundryFaucet,
  DISHWASHER: DishwasherFaucet,
  GARDEN: GardenFaucet,
  DRINKING: DrinkingFaucet,
  OTHER: OtherFaucet,
};

export default function FaucetWidget({ onLogged }) {
  const [isOn, setIsOn]           = useState(false);
  const [category, setCategory]   = useState('SHOWER');
  const [flowKey, setFlowKey]     = useState('medium');
  const [currentL, setCurrentL]   = useState(0);
  const [sessionTotal, setSessionTotal] = useState(0);
  const [status, setStatus]       = useState(null); // null | 'logging' | {ok,litres} | {ok:false,error}

  const timerRef = useRef(null);
  const startRef = useRef(null);

  const FaucetComponent = FaucetMap[category] || DefaultFaucet;
  const handleAngle = isOn ? 45 : 0;

  async function turnOff() {
    setIsOn(false);
    clearInterval(timerRef.current);

    const mins   = (Date.now() - startRef.current) / 60000;
    const litres = parseFloat((mins * FLOW_RATES[flowKey]).toFixed(3));
    setCurrentL(0);

    if (litres < 0.01) return;

    setStatus('logging');
    try {
      await logUsage({
        userId: USER_ID,
        category,
        litres,
        durationMinutes: Math.max(1, Math.round(mins)),
        loggedAt: new Date().toISOString().slice(0, 19),
        notes: `Faucet widget – ${flowKey} flow`,
      });
      setSessionTotal(prev => prev + litres);
      setStatus({ ok: true, litres });
      onLogged?.();
      setTimeout(() => setStatus(null), 3500);
    } catch (err) {
      setStatus({ ok: false, error: err.message });
      setTimeout(() => setStatus(null), 4000);
    }
  }

  function turnOn() {
    setIsOn(true);
    setCurrentL(0);
    startRef.current = Date.now();
    timerRef.current = setInterval(() => {
      const mins = (Date.now() - startRef.current) / 60000;
      setCurrentL(parseFloat((mins * FLOW_RATES[flowKey]).toFixed(2)));
    }, 150);
  }

  function handleToggle() {
    if (isOn) turnOff(); else turnOn();
  }

  useEffect(() => () => clearInterval(timerRef.current), []);

  return (
    <div style={{ display: 'flex', gap: 40, alignItems: 'flex-start', flexWrap: 'wrap' }}>

      {/* ── SVG Faucet ── */}
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <FaucetComponent isOn={isOn} handleAngle={handleAngle} handleToggle={handleToggle} />

        {/* Live litre counter */}
        {isOn && (
          <div style={{ textAlign: 'center', marginTop: 6 }}>
            <span style={{ fontFamily: 'monospace', fontSize: 28, fontWeight: 700, color: '#0288d1' }}>
              {currentL.toFixed(2)}
            </span>
            <span style={{ fontSize: 14, color: '#78909c', marginLeft: 4 }}>L flowing…</span>
          </div>
        )}
      </div>

      {/* ── Controls ── */}
      <div style={{ flex: 1, minWidth: 210, paddingTop: 8 }}>
        <h3 style={{ marginTop: 0, marginBottom: 18, color: '#37474f' }}>Faucet Settings</h3>

        <label style={labelStyle}>Category</label>
        <select
          value={category}
          onChange={e => setCategory(e.target.value)}
          disabled={isOn}
          style={selectStyle}
        >
          {CATEGORIES.map(c => (
            <option key={c} value={c}>
              {c.charAt(0) + c.slice(1).toLowerCase()}
            </option>
          ))}
        </select>

        <label style={labelStyle}>Flow Rate</label>
        <div style={{ display: 'flex', gap: 8, marginBottom: 22 }}>
          {Object.entries(FLOW_RATES).map(([key, lpm]) => (
            <button
              key={key}
              onClick={() => !isOn && setFlowKey(key)}
              disabled={isOn}
              style={{
                ...flowBtnBase,
                borderColor:  flowKey === key ? '#0288d1' : '#cfd8dc',
                background:   flowKey === key ? '#e1f5fe' : 'white',
                color:        flowKey === key ? '#0288d1' : '#546e7a',
                fontWeight:   flowKey === key ? 700 : 400,
                cursor:       isOn ? 'default' : 'pointer',
              }}
            >
              <div style={{ fontSize: 13 }}>{key.charAt(0).toUpperCase() + key.slice(1)}</div>
              <div style={{ fontSize: 11 }}>{lpm} L/min</div>
            </button>
          ))}
        </div>

        {/* Session total */}
        <div style={cardStyle}>
          <div style={{ fontSize: 12, color: '#78909c', marginBottom: 2 }}>Session Total</div>
          <div style={{ fontSize: 26, fontWeight: 700, color: '#0277bd' }}>
            {sessionTotal.toFixed(2)} <span style={{ fontSize: 14, fontWeight: 400 }}>L</span>
          </div>
        </div>

        {/* Status */}
        {status === 'logging' && (
          <p style={{ color: '#0288d1', fontSize: 13, marginTop: 10 }}>Saving…</p>
        )}
        {status?.ok === true && (
          <p style={{ color: '#43a047', fontSize: 13, marginTop: 10 }}>
            ✓ Logged {status.litres.toFixed(2)} L of {category.toLowerCase()}
          </p>
        )}
        {status?.ok === false && (
          <p style={{ color: '#e53935', fontSize: 13, marginTop: 10 }}>
            Error: {status.error}
          </p>
        )}

        {/* Big toggle button */}
        <button
          onClick={handleToggle}
          style={{
            marginTop: 16,
            width: '100%',
            padding: '13px 0',
            background: isOn ? '#e53935' : '#0288d1',
            color: 'white',
            border: 'none',
            borderRadius: 10,
            fontSize: 16,
            fontWeight: 700,
            cursor: 'pointer',
            transition: 'background 0.2s',
            boxShadow: '0 2px 6px rgba(0,0,0,0.18)',
          }}
        >
          {isOn ? 'Turn Off' : 'Turn On'}
        </button>
      </div>
    </div>
  );
}

const labelStyle = {
  display: 'block',
  marginBottom: 6,
  fontSize: 13,
  color: '#546e7a',
  fontWeight: 600,
};

const selectStyle = {
  width: '100%',
  padding: '8px 12px',
  marginBottom: 18,
  borderRadius: 7,
  border: '1px solid #cfd8dc',
  fontSize: 14,
  background: 'white',
};

const flowBtnBase = {
  flex: 1,
  padding: '9px 0',
  border: '2px solid',
  borderRadius: 8,
  textAlign: 'center',
};

const cardStyle = {
  padding: '14px 16px',
  background: '#f0f7ff',
  borderRadius: 9,
  border: '1px solid #dceefb',
};
