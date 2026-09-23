import React from 'react';
import { render } from '@testing-library/react';
import App from './App';

// App mounts its own BrowserRouter, so it is rendered directly here.
// This is a smoke test: it proves the component tree builds without throwing,
// which the previous Create React App default test never did.
test('renders without crashing', () => {
  const { container } = render(<App />);
  expect(container).toBeTruthy();
});
