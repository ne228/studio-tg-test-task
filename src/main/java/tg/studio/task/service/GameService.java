package tg.studio.task.service;

import tg.studio.task.request.GameTurnRequest;
import tg.studio.task.request.NewGameRequest;
import tg.studio.task.response.GameInfoResponse;

public interface GameService {
    GameInfoResponse create(NewGameRequest request) throws Exception;
    GameInfoResponse turn(GameTurnRequest request) throws Exception;

}
