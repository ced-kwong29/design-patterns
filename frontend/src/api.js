const BASE = '/api';

async function request(path, options = {}) {
  const res = await fetch(`${BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  return res.json();
}

// Usage
export const getUsage = () => request('/usage');
export const getUsageSummary = () => request('/usage/summary');
export const getBenchmark = () => request('/usage/benchmark');
export const logUsage = (body) => request('/usage', { method: 'POST', body: JSON.stringify(body) });

// Goals
export const getGoals = () => request('/goals');
export const createGoal = (body) => request('/goals', { method: 'POST', body: JSON.stringify(body) });

// Alerts
export const getAlerts = () => request('/alerts');

// Reports
export const getWeeklyReport = () => request('/reports/weekly');
export const getMonthlyReport = () => request('/reports/monthly');
