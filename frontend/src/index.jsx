import React from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App';

// React 18 replaced ReactDOM.render with createRoot, and 19 removed the old
// API entirely.
createRoot(document.getElementById('root')).render(<App />);
