import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, test } from 'vitest';
import App from './App';

describe('App', () => {
  test('mounts without crashing', () => {
    const { container } = render(<App />);
    expect(container).toBeTruthy();
  });

  test('renders the navigation', () => {
    render(<App />);
    expect(screen.getByText('Medora')).toBeInTheDocument();
  });
});
