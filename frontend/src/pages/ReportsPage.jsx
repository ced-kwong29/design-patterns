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
        <div style={{ padding: 16, background: "#f9f9f9", borderRadius: 8 }}>
          
          <h3>{report.type} Report</h3>

          <p><b>From:</b> {report.from}</p>
          <p><b>To:</b> {report.to}</p>

          <p><b>Total Litres:</b> {report.totalLitres}</p>
          <p><b>Anomalies:</b> {report.anomalyCount}</p>

          <hr />

          <p><b>Summary:</b></p>
          <p>{report.summary}</p>
        </div>
      )}
    </div>
  );
}
