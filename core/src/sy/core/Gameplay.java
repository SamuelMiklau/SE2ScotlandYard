package sy.core;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import sy.GameStart;
import sy.connection.NetworkPackageCallbacks;
import sy.connection.packages.DetectiveDies;
import sy.connection.packages.DetectivesWon;
import sy.connection.packages.MisterXwon;
import sy.connection.packages.MovePlayerObject;
import sy.gameObjects.GameObjectManager;
import sy.gameObjects.NodeGraphObject;
import sy.gameObjects.PawnDetectiveObject;
import sy.gameObjects.PawnMisterXObject;
import sy.gameObjects.PawnObject;
import sy.screens.GameEndDetectives;
import sy.screens.GameEndMrX;
import sy.screens.ScreenManager;

public abstract class Gameplay {
    protected Player localPlayer;
    private int playerTurnId;
    protected NodeGraphObject nodeGraphObject;
    protected NetworkPackageCallbacks callbacks;
    protected List<Player> players;
    protected GameObjectManager gameObjectManager;
    protected PawnMisterXObject pawnMisterXObject;
    protected List<PawnDetectiveObject> pawnDetectiveObjectList = new ArrayList<>();
    protected PawnObject playerPawnObject;
    protected int gameround = 0;



    protected PlayerTurnIF playerTurnIF;


    protected Gameplay(Player localPlayer, List<Player> players, NetworkPackageCallbacks callbacks, GameObjectManager gameObjectManager) {
        this.localPlayer = localPlayer;
        this.players = players;
        this.callbacks = callbacks;
        this.gameObjectManager = gameObjectManager;
        registerCallbacks();
    }

    public abstract void initialize(NodeGraphObject nodeGraphObject);

    private void registerCallbacks() {

        callbacks.registerCallback(MovePlayerObject.class, packageObj -> {
            List<PawnObject> pawnObjectList = getPawnObjects();
            MovePlayerObject playerMoved = (MovePlayerObject) packageObj;
            for (PawnObject pawnObject : pawnObjectList) {
                if (pawnObject.getNetId() == playerMoved.playerObjectNetId) {
                    MapNode newMapNode = nodeGraphObject.getMapNodes().get(playerMoved.newNodeId);
                    pawnObject.setMapNode(newMapNode);
                    break;
                }
            }
        });

        callbacks.registerCallback(DetectiveDies.class, packageObj -> {
            DetectiveDies detectiveDies = (DetectiveDies) packageObj;
            for (PawnDetectiveObject pawnDetectiveObject : pawnDetectiveObjectList) {
                if (pawnDetectiveObject.getNetId() == detectiveDies.netID) {
                    pawnDetectiveObject.setAlive(false);
                    pawnDetectiveObjectList.remove(pawnDetectiveObject);
                    break;
                }
            }
        });

        ScreenManager screenManager = new ScreenManager(GameStart.Instance);
        screenManager.addScreen(new GameEndMrX(GameStart.Instance.renderPipeline, GameStart.Instance.camera, screenManager));
        screenManager.addScreen(new GameEndDetectives(GameStart.Instance.renderPipeline, GameStart.Instance.camera, screenManager));

        callbacks.registerCallback(DetectivesWon.class, packageObj -> {
            DetectivesWon detectivesWon = (DetectivesWon) packageObj;
            Gdx.app.log("Winner: ", "The detectives won");

            //TODO: Show new screen
            screenManager.showScreen(GameEndDetectives.class);
        });

        callbacks.registerCallback(MisterXwon.class, packageObj -> {
            MisterXwon misterXwon = (MisterXwon) packageObj;
            //TODO: Show new screen
            screenManager.showScreen(GameEndMrX.class);
            Gdx.app.log("Winner: ", "MisterX won");
        });
    }


    public List<PawnObject> getPawnObjects() {
        List<PawnObject> pawnObjectList = new ArrayList<>();
        pawnObjectList.addAll(pawnDetectiveObjectList);
        pawnObjectList.add(pawnMisterXObject);
        return pawnObjectList;
    }

    public int getPlayerTurnId() {
        return playerTurnId;
    }

    public void setPlayerTurnId(int playerTurnId) {
        this.playerTurnId = playerTurnId;
    }

    public boolean isLocalTurn() {
        return playerTurnId == localPlayer.getConnectionId();
    }

    public abstract boolean canMove(MapNode toNode, sy.core.Tickets.TicketType ticketType);

    public abstract void movePlayer(MapNode toNode, sy.core.Tickets.TicketType ticketType);

    public void setPlayerTurnIF(PlayerTurnIF playerTurnIF) {
        this.playerTurnIF = playerTurnIF;
    }

    public PawnObject getCurrentPlayer() {
        List<PawnObject> list = getPawnObjects();
        for (PawnObject pawnObject : list) {
            if (pawnObject.getNetId() == playerTurnId) {
                return pawnObject;
            }
        }
        return null;
    }
}
