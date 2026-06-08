const BASE = '/api';

async function request(path, options = {}) {
  const res = await fetch(`${BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

// Usage
export const getUsage = (userId, from, to, category) => {
  const params = new URLSearchParams({ userId, from, to });
  if (category) params.set('category', category);
  return request(`/usage?${params}`);
};
export const getUsageSummary = (userId, period = 'week') =>
  request(`/usage/summary?userId=${userId}&period=${period}`);
export const getBenchmark = (userId, category, region = 'DEFAULT') =>
  request(`/usage/benchmark?userId=${userId}&category=${category}&region=${region}`);
export const logUsage = (body) => request('/usage', { method: 'POST', body: JSON.stringify(body) });

/** Command pattern - undo the last usage log. */
export const undoUsage = () => request('/usage/undo', { method: 'DELETE' });

/** Iterator pattern - paginated usage history. */
export const getUsagePage = (userId, from, to, pageNum = 0, pageSize = 20) => {
  const params = new URLSearchParams({ userId, from, to, pageNum, pageSize });
  return request(`/usage/page?${params}`);
};

// Goals
export const getGoals = (userId) => request(`/goals?userId=${userId}`);
export const createGoal = (body) => request('/goals', { method: 'POST', body: JSON.stringify(body) });

// Alerts
export const getAlerts = (userId) => request(`/alerts?userId=${userId}`);

// Reports
export const getReport = (period, userId) => request(`/reports/${period}?userId=${userId}`);

// Dashboard 
export const getDashboard = (userId) => request(`/dashboard?userId=${userId}`);
