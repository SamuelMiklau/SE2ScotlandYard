package sy.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import sy.assets.AssetDescriptors;
import sy.assets.SYAssetManager;
import sy.core.MapNode;
import sy.core.Tickets.TicketType;
import sy.core.Tickets.Tickets;
import sy.rendering.RenderPipeline;

public abstract class PawnObject extends GameObject implements NetworkIdentifiable {
    private int netId;
    private int playerID;
    private MapNode mapNode;
    private Tickets tickets;


    /**
     * Index vom NodeGraphObject (nodepositions)
     */
    private int index;
    private Sprite sprite;


    public PawnObject(String uuid, Tickets tickets) {
        super(uuid);
        mapNode = new MapNode();
        mapNode.setPosition(new Vector2(-2923, 2636));
        index = 0;
        Texture texture = SYAssetManager.getAsset(AssetDescriptors.MONSTER1);
        sprite = new Sprite(texture);
        sprite.setScale(0.45f);
    }


    @Override
    public void update(float delta) {
    }

    @Override
    public void draw(float delta, RenderPipeline pipeline) {
        sprite.setPosition(mapNode.getPosition().x, mapNode.getPosition().y);
        pipeline.add(sprite, 55);
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMapNode(MapNode mapNode){
        this.mapNode = mapNode;
        this.sprite.setPosition(mapNode.getPosition().x, mapNode.getPosition().y);
    }

    public Tickets getTickets() {
        return tickets;
    }
    public void setTickets(Tickets tickets){
        this.tickets = tickets;
    }

    public abstract boolean removeTicket(TicketType type);

    @Override
    public int getNetId() {
        return 0;
    }
    public void setNetId(int netId) {
        this.netId = netId;
    }

    public abstract boolean hasEnoughTickets (TicketType ticketType);
}