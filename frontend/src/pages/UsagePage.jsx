import { useEffect, useState, useCallback } from 'react';
import { getUsagePage, getUsageSummary, undoUsage } from '../api';
import FaucetWidget from '../components/FaucetWidget';
import useWebSocket from '../useWebSocket';
import { USER_ID } from '../constants';

export default function UsagePage() {
  const [entries, setEntries] = useState([]);
  const [summary, setSummary] = useState(null);
  const [error, setError]     = useState(null);
  const [pageNum, setPageNum] = useState(0);
  const [undoMsg, setUndoMsg] = useState(null);

  const pageSize = 10;

  const reload = useCallback(() => {
    Promise.all([
      getUsagePage(
        USER_ID, 
        new Date(Date.now() - 7 * 86400_000).toISOString().slice(0, 19), 
        new Date().toISOString().slice(0, 19), 
        pageNum, 
        pageSize
      ),
      getUsageSummary(USER_ID, 'week'),
    ])
      .then(([e, s]) => { setEntries(e); setSummary(s); })
      .catch(err => setError(err.message));
  }, [pageNum]);

  useEffect(reload, [reload]);

  // auto-reload when new usage is logged
  useWebSocket({ onUsage: reload });

  async function handleUndo() {
    setUndoMsg(null);
    try {
      const result = await undoUsage();
      if (result?.undone) {
        setUndoMsg(`Undone: ${result.description}`);
        reload();
      } else {
        setUndoMsg(result?.message || 'Nothing to undo');
      }
      setTimeout(() => setUndoMsg(null), 4000);
    } catch (err) {
      setUndoMsg(`Error: ${err.message}`);
      setTimeout(() => setUndoMsg(null), 4000);
    }
  }

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

      {/* Weekly summary (Visitor + Composite driven on backend) */}
      {summary && (
        <div style={{
          display: 'flex',
          gap: 16,
          flexWrap: 'wrap',
          marginBottom: 24,
        }}>
          <div style={{ flex: 1, minWidth: 130, padding: '14px 18px', background: '#f0f7ff', border: '1px solid #dceefb', borderRadius: 10 }}>
            <label style={{ fontSize: 12, color: '#78909c', marginBottom: 4 }}>Total This Week</label>
            <span style={{ display: 'block', fontSize: 22, fontWeight: 700, color: '#0277bd' }}>{`${(summary.totalLitres ?? 0).toFixed(1)} L`}</span>
          </div>

          <div style={{ flex: 1, minWidth: 130, padding: '14px 18px', background: '#f0f7ff', border: '1px solid #dceefb', borderRadius: 10 }}>
            <label style={{ fontSize: 12, color: '#78909c', marginBottom: 4 }}>Daily Average</label>
            <span style={{ display: 'block', fontSize: 22, fontWeight: 700, color: '#0277bd' }}>{`${(summary.avgLitresPerDay ?? 0).toFixed(1)} L`}</span>
          </div>
          
          <div style={{ flex: 1, minWidth: 130, padding: '14px 18px', background: '#f0f7ff', border: '1px solid #dceefb', borderRadius: 10 }}>
            <label style={{ fontSize: 12, color: '#78909c', marginBottom: 4 }}>Entries</label>
            <span style={{ display: 'block', fontSize: 22, fontWeight: 700, color: '#0277bd' }}>{summary.entryCount ?? entries.length}</span>
          </div>
        </div>
      )}

      {error && <p style={{ color: '#e53935' }}>{error}</p>}

      {/* Command pattern: Undo button */}
      <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 16 }}>
        <button
          onClick={handleUndo}
          style={{
            padding: '8px 16px',
            background: '#ff8a65',
            color: 'white',
            border: 'none',
            borderRadius: 7,
            cursor: 'pointer',
            fontWeight: 600,
            fontSize: 13,
          }}
        >
          ↩ Undo Last Entry
        </button>
        {undoMsg && (
          <span style={{ fontSize: 13, color: undoMsg.startsWith('Error') ? '#e53935' : '#43a047' }}>
            {undoMsg}
          </span>
        )}
      </div>

      {/* History table (Iterator pattern: paginated) */}
      <h3 style={{ color: '#37474f', marginBottom: 12 }}>Recent Entries</h3>
      {entries.length === 0 ? (
        <p style={{ color: '#90a4ae' }}>No entries on this page.</p>
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

      {/* Pagination controls */}
      <div style={{ display: 'flex', gap: 12, marginTop: 14, alignItems: 'center' }}>
        <button
          onClick={() => setPageNum(p => Math.max(0, p - 1))}
          disabled={pageNum === 0}
          style={pageBtnStyle(pageNum > 0)}
        >
          ← Previous
        </button>
        <span style={{ fontSize: 13, color: '#546e7a' }}>Page {pageNum + 1}</span>
        <button
          onClick={() => setPageNum(p => p + 1)}
          disabled={entries.length < pageSize}
          style={pageBtnStyle(entries.length >= pageSize)}
        >
          Next →
        </button>
      </div>
    </div>
  );
}

function pageBtnStyle(enabled) {
  return {
    padding: '6px 14px',
    borderRadius: 6,
    border: '1px solid #cfd8dc',
    background: enabled ? 'white' : '#f5f5f5',
    color: enabled ? '#0277bd' : '#b0bec5',
    cursor: enabled ? 'pointer' : 'default',
    fontSize: 13,
  };
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
