package tg.studio.task.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameTurnRequest {

    @JsonProperty("game_id")
    private String gameId;

    @JsonProperty("row")
    private Integer row;

    @JsonProperty("col")
    private Integer col;
}
