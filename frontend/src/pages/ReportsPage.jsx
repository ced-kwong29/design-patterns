import { useState } from 'react';
import { getReport } from '../api';
import { USER_ID } from '../constants';

export default function ReportsPage() {
  const [report, setReport] = useState(null);
  const [error, setError] = useState(null);

  async function load(fn) {
    setReport(null);
    setError(null);
    try {
      setReport(await fn());
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h2>Reports</h2>
      <div style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
        <button onClick={() => load(() => getReport("weekly", USER_ID))}>Weekly</button>
        <button onClick={() => load(() => getReport("monthly", USER_ID))}>Monthly</button>
      </div>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {report && (
        <pre style={{ background: '#f4f4f4', padding: 12 }}>
          {JSON.stringify(report, null, 2)}
        </pre>
      )}
    </div>
  );
}
