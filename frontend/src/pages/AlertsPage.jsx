import { useEffect, useState } from 'react';
import { getAlerts } from '../api';

export default function AlertsPage() {
  const [alerts, setAlerts] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    getAlerts().then(setAlerts).catch((err) => setError(err.message));
  }, []);

  if (error) return <p style={{ color: 'red' }}>{error}</p>;

  return (
    <div>
      <h2>Alerts</h2>
      {alerts.length === 0 ? (
        <p>No alerts.</p>
      ) : (
        <table border="1" cellPadding="6" style={{ borderCollapse: 'collapse', width: '100%' }}>
          <thead>
            <tr><th>ID</th><th>Type</th><th>Message</th><th>Created At</th></tr>
          </thead>
          <tbody>
            {alerts.map((a) => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{a.type}</td>
                <td>{a.message}</td>
                <td>{a.createdAt}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
