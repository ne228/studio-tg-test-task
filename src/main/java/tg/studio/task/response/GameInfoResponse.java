package tg.studio.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tg.studio.task.entity.Field;
import tg.studio.task.entity.Game;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameInfoResponse {
    @JsonProperty("game_id")
    private String gameId;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("completed")
    private boolean completed;

    @JsonProperty("field")
    private List<List<String>> field;


    public GameInfoResponse(Game game, List<Field> fields) {
        gameId = game.getGameId();
        width = game.getWidth();
        height = game.getHeight();
        completed = game.isCompleted();

        field = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            field.add(new ArrayList<>());
            for (int col = 0; col < width; col++) {
                int index = width * row + col;
                var targetField = fields.get(index);
                field.get(row).add(targetField.getValue());
            }
        }

    }
}
