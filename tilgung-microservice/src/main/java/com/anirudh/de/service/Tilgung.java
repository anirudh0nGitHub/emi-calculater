package com.anirudh.de.service;

import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import com.anirudh.de.domain.request.AnfrageDTO;
import com.anirudh.de.domain.response.AbstractTilgungResponse;
import java.util.List;
import java.util.Optional;

public interface Tilgung<T extends AbstractTilgungResponse> {

  Optional<List<T>> planErzeugen(AnfrageDTO anfrageDTO) throws TilgungPlanCreationException;
}
