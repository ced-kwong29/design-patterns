import { useEffect, useState } from 'react';
import { getUsage, getUsageSummary, logUsage } from '../api';

export default function UsagePage() {
  const [entries, setEntries] = useState([]);
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState(null);

  // Form state
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('');

  useEffect(() => {
    Promise.all([getUsage(), getUsageSummary()])
      .then(([e, s]) => { setEntries(e); setSummary(s); })
      .catch((err) => setError(err.message));
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await logUsage({ amount: parseFloat(amount), category });
      const [e2, s2] = await Promise.all([getUsage(), getUsageSummary()]);
      setEntries(e2);
      setSummary(s2);
      setAmount('');
      setCategory('');
    } catch (err) {
      setError(err.message);
    }
  }

  if (error) return <p style={{ color: 'red' }}>{error}</p>;

  return (
    <div>
      <h2>Usage</h2>

      {summary && (
        <pre style={{ background: '#f4f4f4', padding: 12 }}>
          {JSON.stringify(summary, null, 2)}
        </pre>
      )}

      <form onSubmit={handleSubmit} style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
        <input
          placeholder="Amount (L)"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          required
          type="number"
          min="0"
          step="any"
        />
        <input
          placeholder="Category"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          required
        />
        <button type="submit">Log</button>
      </form>

      <table border="1" cellPadding="6" style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr><th>ID</th><th>Amount</th><th>Category</th><th>Recorded At</th></tr>
        </thead>
        <tbody>
          {entries.map((entry) => (
            <tr key={entry.id}>
              <td>{entry.id}</td>
              <td>{entry.amount}</td>
              <td>{entry.category}</td>
              <td>{entry.recordedAt}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
