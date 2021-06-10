package com.anirudh.de.service;

import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import com.anirudh.de.domain.request.AnfrageDTO;
import com.anirudh.de.domain.response.AbstractTilgungResponse;

public abstract class AbstractTilgungService<M extends AbstractTilgungResponse> implements Tilgung<M> {

    void verifyRequestPayload(AnfrageDTO anfrageDTO) throws TilgungPlanCreationException {
        if (anfrageDTO == null) {
            throw new TilgungPlanCreationException("Die AnfrageDTO is null.");
        } else if (anfrageDTO.getDarlehensbetrag() == null) {
            throw new TilgungPlanCreationException("Der darlehensbetrag fehlt in der AnfrageDTO.");
        } else if (anfrageDTO.getSollzins() == null) {
            throw new TilgungPlanCreationException("Sollzins fehlt in der AnfrageDTO.");
        } else if (anfrageDTO.getTilgungsatz() == null) {
            throw new TilgungPlanCreationException("Der Tilgungsatz fehlt in der AnfrageDTO.");
        }
    }
}
