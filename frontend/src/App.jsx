import { useState } from 'react';
import UsagePage from './pages/UsagePage';
import GoalsPage from './pages/GoalsPage';
import AlertsPage from './pages/AlertsPage';
import ReportsPage from './pages/ReportsPage';

const PAGES = ['Usage', 'Goals', 'Alerts', 'Reports'];

export default function App() {
  const [page, setPage] = useState('Usage');

  return (
    <div style={{ fontFamily: 'sans-serif', maxWidth: 900, margin: '0 auto', padding: 24 }}>
      <h1>Water Monitor</h1>
      <nav style={{ display: 'flex', gap: 12, marginBottom: 24 }}>
        {PAGES.map((p) => (
          <button
            key={p}
            onClick={() => setPage(p)}
            style={{ fontWeight: page === p ? 'bold' : 'normal' }}
          >
            {p}
          </button>
        ))}
      </nav>
      {page === 'Usage'   && <UsagePage />}
      {page === 'Goals'   && <GoalsPage />}
      {page === 'Alerts'  && <AlertsPage />}
      {page === 'Reports' && <ReportsPage />}
    </div>
  );
}
