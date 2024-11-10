package org.csdg8.game;

import org.csdg8.game.dto.GetGameResponse;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameController {
    
    public Entity getGame() throws Siren4JException {
        GetGameResponse gameResponse = new GetGameResponse();
        return ReflectingConverter.newInstance().toEntity(gameResponse);
    }
}
