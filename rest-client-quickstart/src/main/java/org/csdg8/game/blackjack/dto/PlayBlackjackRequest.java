package org.csdg8.game.blackjack.dto;

//needs to be blackjack staet
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayBlackjackRequest {
   

    @NotNull
    @Min(value = 1)
    @Digits(integer = 99, fraction = 0)
    private Long betAmount;
}