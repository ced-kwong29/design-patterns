import { useEffect, useState, useCallback } from 'react';
import { getUsage, getUsageSummary } from '../api';
import FaucetWidget from '../components/FaucetWidget';
import { USER_ID } from '../constants';

function now()  { return new Date().toISOString().slice(0, 19); }
function week() { return new Date(Date.now() - 7 * 86400_000).toISOString().slice(0, 19); }

export default function UsagePage() {
  const [entries, setEntries] = useState([]);
  const [summary, setSummary] = useState(null);
  const [error,   setError]   = useState(null);

  const reload = useCallback(() => {
    Promise.all([
      getUsage(USER_ID, week(), now()),
      getUsageSummary(USER_ID, 'week'),
    ])
      .then(([e, s]) => { setEntries(e); setSummary(s); })
      .catch(err => setError(err.message));
  }, []);

  useEffect(reload, [reload]);

  return (
    <div>
      <h2 style={{ color: '#263238', marginBottom: 24 }}>Water Usage</h2>

      {/* Interactive faucet */}
      <div style={{
        background: 'white',
        border: '1px solid #e0e7ef',
        borderRadius: 14,
        padding: '28px 32px',
        marginBottom: 32,
        boxShadow: '0 2px 10px rgba(0,0,0,0.06)',
      }}>
        <FaucetWidget onLogged={reload} />
      </div>

      {/* Weekly summary */}
      {summary && (
        <div style={{
          display: 'flex',
          gap: 16,
          flexWrap: 'wrap',
          marginBottom: 24,
        }}>
          <StatCard label="Total This Week" value={`${(summary.totalLitres ?? 0).toFixed(1)} L`} />
          <StatCard label="Daily Average"   value={`${(summary.avgLitresPerDay ?? 0).toFixed(1)} L`} />
          <StatCard label="Entries"         value={summary.entryCount ?? entries.length} />
        </div>
      )}

      {error && <p style={{ color: '#e53935' }}>{error}</p>}

      {/* History table */}
      <h3 style={{ color: '#37474f', marginBottom: 12 }}>Recent Entries</h3>
      {entries.length === 0 ? (
        <p style={{ color: '#90a4ae' }}>No entries in the past 7 days.</p>
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 14 }}>
          <thead>
            <tr style={{ background: '#f0f7ff' }}>
              {['Category', 'Litres', 'Duration', 'Logged At', 'Notes'].map(h => (
                <th key={h} style={thStyle}>{h}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {entries.map((e, i) => (
              <tr key={e.id} style={{ background: i % 2 === 0 ? 'white' : '#fafcff' }}>
                <td style={tdStyle}>{e.category}</td>
                <td style={tdStyle}>{e.litres?.toFixed(2)}</td>
                <td style={tdStyle}>{e.durationMinutes != null ? `${e.durationMinutes} min` : '—'}</td>
                <td style={tdStyle}>{e.loggedAt ? e.loggedAt.replace('T', ' ') : '—'}</td>
                <td style={{ ...tdStyle, color: '#78909c' }}>{e.notes || '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function StatCard({ label, value }) {
  return (
    <div style={{
      flex: 1,
      minWidth: 130,
      padding: '14px 18px',
      background: '#f0f7ff',
      border: '1px solid #dceefb',
      borderRadius: 10,
    }}>
      <div style={{ fontSize: 12, color: '#78909c', marginBottom: 4 }}>{label}</div>
      <div style={{ fontSize: 22, fontWeight: 700, color: '#0277bd' }}>{value}</div>
    </div>
  );
}

const thStyle = {
  padding: '10px 14px',
  textAlign: 'left',
  color: '#546e7a',
  fontWeight: 600,
  borderBottom: '2px solid #dceefb',
};

const tdStyle = {
  padding: '9px 14px',
  borderBottom: '1px solid #eef2f7',
  color: '#37474f',
};
