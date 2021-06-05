package sy.core;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import sy.connection.ServerHandler;
import sy.connection.packages.AddPawnObject;
import sy.connection.packages.ClientMoveRequest;
import sy.connection.packages.GameplayReady;
import sy.connection.packages.MovePlayerObject;
import sy.connection.packages.PlayerTurn;
import sy.connection.packages.RemoveTicket;
import sy.core.Tickets.TicketType;
import sy.gameObjects.GameObjectManager;
import sy.gameObjects.NodeGraphObject;

public class GameplayServer extends Gameplay {
    private ServerHandler server;
    private Queue<Player> turnQueue = new LinkedList<>();
    private int gameplaysReady = 1;

    public GameplayServer(Player player, List<Player> players, ServerHandler server, GameObjectManager gameObjectManager) {
        super(player, players, server.getCallbacks(), gameObjectManager);
        this.server = server;

        callbacks.registerCallback(ClientMoveRequest.class, packageObj -> {
            ClientMoveRequest clientRequest = (ClientMoveRequest) packageObj;
            server.sendAll(new MovePlayerObject(clientRequest.playerObjNetId, clientRequest.newNodeId), true);
            server.sendAll(new RemoveTicket(clientRequest.playerObjNetId, clientRequest.ticketType), true);
        });

        callbacks.registerCallback(GameplayReady.class, packageObj -> {
            GameplayReady gameplayReady = (GameplayReady) packageObj;
            gameplaysReady++;
            if(gameplaysReady == players.size()){
                spawnPlayerPawns(nodeGraphObject);
            }
        });


    }

    public void changeTurn(){
        Player nextPlayer = turnQueue.poll();
        server.sendAll(new PlayerTurn(nextPlayer.getConnectionId()), true);
        turnQueue.add(nextPlayer);

    }

    public void playerConnected(Player player){
        turnQueue.add(player);
    }

    @Override
    public void initialize(NodeGraphObject nodeGraphObject) {
        super.nodeGraphObject = nodeGraphObject;
    }

    private void spawnPlayerPawns(NodeGraphObject nodeGraphObject) {
        //MapNode randomNode = Collections.getRandomItem(nodeGraphObject.getMapNodes());
        MapNode randomNode = nodeGraphObject.getMapNodes().get(0);
        int id = players.get(0).getConnectionId();
        server.sendAll(new AddPawnObject(id,randomNode.getId(),true), true);

        for (int i = 1; i < players.size(); i++){
            //randomNode = Collections.getRandomItem(nodeGraphObject.getMapNodes());
            randomNode = nodeGraphObject.getMapNodes().get(i);
            id = players.get(i).getConnectionId();
            server.sendAll(new AddPawnObject(id,randomNode.getId(),false), true);
        }
    }

    @Override
    public void movePlayer(MapNode newNode, TicketType ticketType) {
      boolean move = canMove(newNode, ticketType);
        if(isLocalTurn() && move) {
            playerPawnObject.setMapNode(newNode);
            server.sendAll(new MovePlayerObject(playerPawnObject, newNode), true);
            server.sendAll(new RemoveTicket(playerPawnObject.getNetId(), ticketType), true);
        }
    }
}
