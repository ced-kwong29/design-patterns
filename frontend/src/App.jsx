import { useState } from 'react';
import DashboardPage from './pages/DashboardPage';
import UsagePage from './pages/UsagePage';
import GoalsPage from './pages/GoalsPage';
import AlertsPage from './pages/AlertsPage';
import ReportsPage from './pages/ReportsPage';

const PAGES = ['Dashboard', 'Usage', 'Goals', 'Alerts', 'Reports'];

export default function App() {
  const [page, setPage] = useState('Dashboard');

  return (
    <div style={{ fontFamily: 'sans-serif', maxWidth: 900, margin: '0 auto', padding: 24, minHeight: '100vh', background: 'linear-gradient(180deg, #e3f2fd 0%, #f8fbff 100%)', padding: 24, }}>
      <div style={{ marginBottom: 24 }}>
        <h1
          style={{
            margin: 0,
            color: '#01579b',
            fontSize: 40,
          }}
        >
          💧 Water Monitor
        </h1>

        <p
          style={{
            marginTop: 6,
            color: '#607d8b',
          }}
        >
          Track and visualize household water consumption
        </p>
      </div>
      <div
  style={{
    height: 6,
    background:
      'linear-gradient(90deg, #03a9f4, #4fc3f7, #81d4fa)',
    borderRadius: 20,
    marginBottom: 24,
  }}
/>
      <nav style={{ display: 'flex', gap: 12, marginBottom: 24 }}>
        {PAGES.map((p) => (
          <button
            key={p}
            onClick={() => setPage(p)}
            style={{
              padding: '10px 18px',
              borderRadius: 12,
              border: 'none',
              cursor: 'pointer',
              transition: 'all 0.2s ease',
              background:
                page === p ? '#0288d1' : 'white',
              color:
                page === p ? 'white' : '#455a64',
              boxShadow:
                page === p
                  ? '0 4px 12px rgba(2,136,209,0.25)'
                  : '0 2px 6px rgba(0,0,0,0.08)',
            }}
          >
            {p}
          </button>
        ))}
      </nav>
      {page === 'Dashboard' && <DashboardPage />}
      {page === 'Usage'     && <UsagePage />}
      {page === 'Goals'     && <GoalsPage />}
      {page === 'Alerts'    && <AlertsPage />}
      {page === 'Reports'   && <ReportsPage />}
    </div>
  );
}
