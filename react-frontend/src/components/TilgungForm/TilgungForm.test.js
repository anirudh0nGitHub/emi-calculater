import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import TilgungForm from './TilgungForm';

describe('<TilgungForm />', () => {
  test('it should mount', () => {
    render(<TilgungForm />);

    const darlehensTextbox = document.getElementById("darlehensbetrag");
    const sollzinsTextbox = document.getElementById("sollzins");
    const zinsbindungTextbox = document.getElementById("zinsbildung");
    const tilgungTextbox = document.getElementById("tilgungsatz");
    const berechnenButton = document.getElementById("berechnen");

    expect(darlehensTextbox).toBeInTheDocument();
    expect(sollzinsTextbox).toBeInTheDocument();
    expect(zinsbindungTextbox).toBeInTheDocument();
    expect(tilgungTextbox).toBeInTheDocument();
    expect(berechnenButton).toBeInTheDocument();
  });
});
