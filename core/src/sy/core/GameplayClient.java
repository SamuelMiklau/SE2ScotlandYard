package sy.core;

import sy.connection.ClientHandler;
import sy.connection.NetworkPackageCallbacks;
import sy.connection.packages.ClientMoveRequest;
import sy.gameObjects.PawnObject;

public class GameplayClient extends Gameplay {
    private ClientHandler client;

    public GameplayClient(Player player, ClientHandler client) {
        super(player, client.getCallbacks());
        this.client = client;
        callbacks.registerCallback(PlayerTurn.class, packageObj->{
            PlayerTurn turn = (PlayerTurn) packageObj;
            setPlayerTurnId(turn.getIndex());
        });

    }

    @Override
    public void movePlayer(PawnObject pawnObject, MapNode newNode, TicketType ticketType) {
        boolean move = canMove(pawnObject, newNode, ticketType);
        if(isLocalTurn() && move) {
            pawnObject.setMapNode(newNode);
            pawnObject.removeTicket(ticketType);
            client.send(new ClientMoveRequest(pawnObject, newNode));
        }
    }
}
