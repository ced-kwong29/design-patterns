import { useEffect, useState, useCallback } from 'react';
import { getDashboard } from '../api';
import useWebSocket from '../useWebSocket';
import { USER_ID } from '../constants';

/**
 * Facade pattern on the frontend - a single API call fetches the entire
 * dashboard view (usage summary, active goals, recent alerts, latest report).
 *
 * Also demonstrates WebSocket integration: the page auto-refreshes when
 * real-time events arrive from the backend Observer pattern.
 */
export default function DashboardPage() {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  const load = useCallback(() => {
    getDashboard(USER_ID)
      .then(setData)
      .catch((err) => setError(err.message));
  }, []);

  useEffect(load, [load]);

  // WebSocket: auto-reload when any event fires
  useWebSocket({
    onUsage: load,
    onAlert: load,
    onGoal: load,
  });

  if (error) return <p style={{ color: '#e53935' }}>{error}</p>;
  if (!data) return <p style={{ color: '#90a4ae' }}>Loading dashboard…</p>;

  const { usageSummary, activeGoals, recentAlerts, latestReport } = data;

  return (
    <div>
      <h2 style={{ color: '#263238', marginBottom: 24 }}>Dashboard</h2>

      {/* Usage summary cards */}
      {usageSummary && (
        <section style={{ marginBottom: 28 }}>
          <h3 style={sectionTitle}>Usage Summary ({usageSummary.period})</h3>
          <div style={{ display: 'flex', gap: 14, flexWrap: 'wrap' }}>
            <div style={{ minWidth: 110, padding: '12px 16px', background: '#f0f7ff', border: '1px solid #dceefb', borderRadius: 10 }}>
              <label style={{ fontSize: 11, color: '#78909c', marginBottom: 3 }}>Total</label>
              <span style={{ display: 'block', fontSize: 20, fontWeight: 700, color: '#0277bd' }}>{`${(usageSummary.totalLitres ?? 0).toFixed(1)} L`}</span>
            </div>

            <div style={{ minWidth: 110, padding: '12px 16px', background: '#f0f7ff', border: '1px solid #dceefb', borderRadius: 10 }}>
              <label style={{ fontSize: 11, color: '#78909c', marginBottom: 3 }}>Entries</label>
              <span style={{ display: 'block', fontSize: 20, fontWeight: 700, color: '#0277bd' }}>{usageSummary.entryCount ?? 0}</span>
            </div>
            
            {usageSummary.litresByCategory && Object.entries(usageSummary.litresByCategory).map(([cat, litres]) => (
              <div key={cat} style={{ minWidth: 110, padding: '12px 16px', background: '#f0f7ff', border: '1px solid #dceefb', borderRadius: 10 }}>
                <label style={{ fontSize: 11, color: '#78909c', marginBottom: 3 }}>{cat}</label>
                <span style={{ display: 'block', fontSize: 20, fontWeight: 700, color: '#0277bd' }}>{`${litres.toFixed(1)} L`}</span>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* Active goals */}
      <section style={{ marginBottom: 28 }}>
        <h3 style={sectionTitle}>Active Goals</h3>
        {activeGoals?.length === 0 ? (
          <p style={{ color: '#90a4ae' }}>No active goals.</p>
        ) : (
          <table style={tableStyle}>
            <thead>
              <tr style={{ background: '#f0f7ff' }}>
                <th style={thStyle}>Category</th>
                <th style={thStyle}>Target</th>
                <th style={thStyle}>Period</th>
                <th style={thStyle}>State</th>
                <th style={thStyle}>Ends</th>
              </tr>
            </thead>
            <tbody>
              {activeGoals?.map((g) => (
                <tr key={g.id}>
                  <td style={tdStyle}>{g.category || 'All'}</td>
                  <td style={tdStyle}>{g.targetLitres} L</td>
                  <td style={tdStyle}>{g.period}</td>
                  <td style={tdStyle}>{g.state}</td>
                  <td style={tdStyle}>{g.endsAt}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>

      {/* Recent alerts */}
      <section style={{ marginBottom: 28 }}>
        <h3 style={sectionTitle}>Recent Alerts</h3>
        {recentAlerts?.length === 0 ? (
          <p style={{ color: '#90a4ae' }}>No recent alerts.</p>
        ) : (
          <ul style={{ paddingLeft: 18 }}>
            {recentAlerts?.map((a) => (
              <li key={a.id} style={{ marginBottom: 6, color: '#37474f' }}>
                <strong>{a.type}</strong> — {a.message}
                <span style={{ color: '#90a4ae', fontSize: 12, marginLeft: 8 }}>
                  {a.createdAt?.replace('T', ' ')}
                </span>
              </li>
            ))}
          </ul>
        )}
      </section>

      {/* Latest report */}
      {latestReport && (
        <section>
          <h3 style={sectionTitle}>Latest Report ({latestReport.type})</h3>
          <div style={{ padding: 16, background: '#f9fafb', borderRadius: 8, border: '1px solid #e0e7ef' }}>
            <p><strong>Period:</strong> {latestReport.from} → {latestReport.to}</p>
            <p><strong>Total:</strong> {latestReport.totalLitres?.toFixed(1)} L</p>
            <p><strong>Anomalies:</strong> {latestReport.anomalyCount}</p>
            <p style={{ color: '#546e7a', fontStyle: 'italic' }}>{latestReport.summary}</p>
          </div>
        </section>
      )}
    </div>
  );
}

const sectionTitle = { color: '#37474f', fontSize: 16, marginBottom: 10 };
const tableStyle = { width: '100%', borderCollapse: 'collapse', fontSize: 14 };
const thStyle = { padding: '9px 12px', textAlign: 'left', color: '#546e7a', fontWeight: 600, borderBottom: '2px solid #dceefb' };
const tdStyle = { padding: '8px 12px', borderBottom: '1px solid #eef2f7', color: '#37474f' };
