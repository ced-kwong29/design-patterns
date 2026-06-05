import { useEffect, useState } from 'react';
import { getGoals, createGoal } from '../api';
import { USER_ID } from '../constants';

export default function GoalsPage() {
  const [goals, setGoals] = useState([]);
  const [error, setError] = useState(null);

  // Form state
  const [targetAmount, setTargetAmount] = useState('');
  const [period, setPeriod] = useState('');

  useEffect(() => {
    getGoals(USER_ID).then(setGoals).catch((err) => setError(err.message));
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await createGoal({ targetAmount: parseFloat(targetAmount), period });
      const updated = await getGoals(USER_ID);
      setGoals(updated);
      setTargetAmount('');
      setPeriod('');
    } catch (err) {
      setError(err.message);
    }
  }

  if (error) return <p style={{ color: 'red' }}>{error}</p>;

  return (
    <div>
      <h2>Goals</h2>

      <form onSubmit={handleSubmit} style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
        <input
          placeholder="Target (L)"
          value={targetAmount}
          onChange={(e) => setTargetAmount(e.target.value)}
          required
          type="number"
          min="0"
          step="any"
        />
        <input
          placeholder="Period (WEEKLY / MONTHLY)"
          value={period}
          onChange={(e) => setPeriod(e.target.value)}
          required
        />
        <button type="submit">Create</button>
      </form>

      <table border="1" cellPadding="6" style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr><th>ID</th><th>Target</th><th>Period</th><th>State</th></tr>
        </thead>
        <tbody>
          {goals.map((g) => (
            <tr key={g.id}>
              <td>{g.id}</td>
              <td>{g.targetAmount}</td>
              <td>{g.period}</td>
              <td>{g.state}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
