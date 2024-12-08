package org.csdg8.game.blackjack.dto;

import org.csdg8.game.blackjack.model.BlackjackState;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlackjackActionRequest {
      @NotNull
    private BlackjackState choice;
}
