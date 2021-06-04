package sy.core;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import sy.connection.ClientHandler;
import sy.connection.packages.LobbyPlayerReady;
import sy.connection.packages.LobbyToStartGame;
import sy.connection.packages.PlayerJoinLobbyRequest;
import sy.screens.GameScreen;
import sy.screens.LobbyMenu;
import sy.screens.ScreenManager;

public class LobbyLogicClient extends LobbyLogic {
    private ClientHandler clientHandler;

    public LobbyLogicClient(ClientHandler clientHandler, LobbyMenu lobbyMenu, ScreenManager screenManager) {
        super(clientHandler.getCallbacks(), lobbyMenu, screenManager);
        this.clientHandler = clientHandler;


        callbacks.registerCallback(LobbyToStartGame.class, pckg -> {
            GameScreen gameScreen = screenManager.getScreen(GameScreen.class);
            List<Player> players = new ArrayList<>();
            Player localPlayer = null;
            for(LobbyPlayer lobbyPlayer : currLobbyPlayers.values()) {
                Player player = new Player(lobbyPlayer.getConnectionId());
                if(lobbyPlayer.getConnectionId() == 0) {
                    player.setLocalPlayer(true);
                    localPlayer = player;
                }
                players.add(player);
            }
            gameScreen.initialize(clientHandler, callbacks, players, localPlayer);
            screenManager.showScreen(GameScreen.class);
        });
    }

    public void sendJoinRequest() {
        int id =  clientHandler.getKryonetClient().getID();
        Gdx.app.log("FICKN", "FICKN");
        clientHandler.send(new PlayerJoinLobbyRequest(id));
    }

    @Override
    public void readyUp() {
        clientHandler.send(new LobbyPlayerReady(clientHandler.getKryonetClient().getID()));
    }
}