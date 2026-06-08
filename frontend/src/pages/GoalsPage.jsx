import { useEffect, useState, useCallback } from 'react';
import { getGoals, createGoal } from '../api';
import useWebSocket from '../useWebSocket';
import { USER_ID } from '../constants';

const CATEGORIES = ['SHOWER', 'BATH', 'LAUNDRY', 'DISHWASHER', 'GARDEN', 'DRINKING', 'OTHER'];

export default function GoalsPage() {
  const [goals, setGoals] = useState([]);
  const [error, setError] = useState(null);

  // Form state
  const [form, setForm] = useState({ 
    targetLitres: '', 
    period: '', 
    category: '' 
  });

  const updateForm = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const load = useCallback(() => {
    getGoals(USER_ID).then(setGoals).catch((err) => setError(err.message));
  }, []);

  useEffect(load, [load]);

  // auto-reload when a goal state changes
  useWebSocket({ onGoal: load });

  function computeEndDate(period) {
    const start = new Date();
    if (period === 'MONTHLY') {
      start.setMonth(start.getMonth() + 1);
    } else {
      start.setDate(start.getDate() + 7);
    }
    return start.toISOString().slice(0, 10);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    try {
      await createGoal({
        userId: USER_ID,
        category: form.category || null,
        targetLitres: parseFloat(form.targetLitres),
        period: form.period,
        startsAt: new Date().toISOString().slice(0, 10),
        endsAt: computeEndDate(form.period),
      });
      const updated = await getGoals(USER_ID);
      setGoals(updated);
      setForm({ targetLitres: '', period: '', category: '' });
    } catch (err) {
      setError(err.message);
    }
  }

  const isFormValid = form.targetLitres && parseFloat(form.targetLitres) > 0 && form.period;

  return (
    <div>
      <h2>Goals</h2>

      <form onSubmit={handleSubmit} style={{ display: 'flex', gap: 8, marginBottom: 16, flexWrap: 'wrap', alignItems: 'flex-end' }}>
        <div>
          <label style={labelStyle}>Target (L)</label>
          <input
            value={form.targetLitres}
            onChange={updateForm('targetLitres')}
            required
            type="number"
            min="0.1"
            step="any"
            style={inputStyle}
          />
        </div>

        <div>
          <label style={labelStyle}>Period</label>
          <select value={form.period} onChange={updateForm('period')} style={inputStyle}>
            <option value={''} disabled>Select</option>
            <option value="WEEKLY">Weekly</option>
            <option value="MONTHLY">Monthly</option>
          </select>
        </div>

        <div>
          <label style={labelStyle}>Category (optional)</label>
          <select value={form.category} onChange={updateForm('category')} style={inputStyle}>
            <option value={''} disabled>Select</option>
            {CATEGORIES.map(c => (
              <option key={c} value={c}>{c.charAt(0) + c.slice(1).toLowerCase()}</option>
            ))}
          </select>
        </div>

        <button type="submit" disabled={!isFormValid} style={{ padding: '8px 16px', alignSelf: 'flex-end', opacity: isFormValid ? 1 : 0.5, cursor: isFormValid ? 'pointer' : 'not-allowed' }}>Create</button>
      </form>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <table border="1" cellPadding="6" style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Category</th>
            <th>Target (L)</th>
            <th>Period</th>
            <th>State</th>
            <th>Progress</th>
            <th>Starts</th>
            <th>Ends</th>
          </tr>
        </thead>
        <tbody>
          {goals.map((g) => (
            <tr key={g.id}>
              <td>{g.id}</td>
              <td>{g.category || 'N/A'}</td>
              <td>{g.targetLitres}</td>
              <td>{g.period}</td>
              <td>{g.state}</td>
              <td>{g.progressPercent.toFixed(1)}%</td>
              <td>{g.startsAt}</td>
              <td>{g.endsAt}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const labelStyle = { display: 'block', fontSize: 12, marginBottom: 4, color: '#546e7a' };
const inputStyle = { padding: '6px 10px', fontSize: 14, borderRadius: 5, border: '1px solid #cfd8dc' };
