import React from 'react';
import { render } from '@testing-library/react';
import App from './App';

test('Render tilgung rechner test', () => {
  const { getByText } = render(<App />);
  const navtext1 = getByText(/Tilgungrechner/i);
  const navtext2 = getByText(/Created by Anirudh/i);
  expect(navtext1).toBeInTheDocument();
  expect(navtext2).toBeInTheDocument();
});
