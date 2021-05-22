package sy.core;

import java.util.List;

import sy.connection.ClientHandler;
import sy.connection.packages.ClientMoveRequest;
import sy.gameObjects.PawnObject;

public class GameplayClient extends Gameplay {
    private ClientHandler client;

    public GameplayClient(Player player, ClientHandler client) {
        super(player);
        this.client = client;
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
