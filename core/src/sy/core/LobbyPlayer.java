package sy.core;

public class LobbyPlayer {
    private int connectionId;
    private boolean isReady;
    private byte[] customTexture;
    private String name;

    public LobbyPlayer(int connectionId, String name){
        this.connectionId = connectionId;
        this.name = name;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public byte[] getCustomTexture() {
        return customTexture;
    }

    public void setCustomTexture(byte[] customTexture) {
        this.customTexture = customTexture;
    }

    public String getName() {
        return name;
    }
}
