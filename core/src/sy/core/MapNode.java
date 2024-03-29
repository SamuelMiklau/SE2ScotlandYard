package sy.core;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;


public class MapNode {
    private int id;
    private Vector2 position = Vector2.Zero;
    private List<Edge> edges = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MapNode() {

    }

    public MapNode(Vector2 position, int id) {

        this.position = position;
        this.id = id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Edge> getEdgesForMovement(MoveType moveType) {
        List<Edge> ret = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.isAllowedMove(moveType))
                ret.add(edge);
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapNode)) return false;
        MapNode mapNode = (MapNode) o;
        return position.equals(mapNode.position);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
