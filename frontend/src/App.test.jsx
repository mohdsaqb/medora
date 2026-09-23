import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, test } from 'vitest';
import App from './App';

// Components fetch on mount, so these await the rendered output rather than
// asserting synchronously. Network calls are stubbed in setupTests.js.
describe('App', () => {
  test('mounts without crashing', async () => {
    const { container } = render(<App />);
    expect(await screen.findByText('Medora')).toBeInTheDocument();
    expect(container).toBeTruthy();
  });

  test('offers sign in when signed out', async () => {
    render(<App />);
    expect(await screen.findByPlaceholderText('Username')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
  });

  test('hides Add Patient when signed out', async () => {
    render(<App />);
    await screen.findByText('Medora');
    expect(screen.queryByText('Add Patient')).not.toBeInTheDocument();
  });
});
