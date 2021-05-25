package sy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Scaling;

import sy.assets.AssetDescriptors;
import sy.assets.SYAssetManager;
import sy.connection.ClientHandler;
import sy.connection.NetworkPackageCallbacks;
import sy.rendering.RenderPipeline;
import sy.ui.AliveButton;

public class JoinGameMenu extends AbstractScreen {
    private float screenWidth;
    private float screenHeight;
    private ScreenManager screenManager;
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonSound.mp3"));
    private Skin textfieldSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));


    public JoinGameMenu(RenderPipeline renderPipeline, OrthographicCamera camera, ScreenManager screenManager) {
        this.screenManager = screenManager;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        textfieldSkin.getFont("default-font").getData().setScale(2.75f);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void buildStage() {
        TextField userName;
        TextField userIP;
        TextField tcpPort;
        TextField udpPort;
        AliveButton join;
        AliveButton host;
        AliveButton leave;
        NetworkPackageCallbacks networkPackageCallbacks = new NetworkPackageCallbacks();


        userIP = new TextField("",textfieldSkin);
        userIP.setMessageText("Enter IP");
        userIP.setSize(screenWidth *0.6f, screenHeight*0.1f);
        userIP.setPosition(screenWidth/2 - userIP.getWidth()/2, screenHeight/2 + (userIP.getHeight()*3.5f));
        addActorsToStage(userIP);

        userName = new TextField("",textfieldSkin);
        userName.setMessageText("Playername");
        userName.setSize(screenWidth *0.6f, screenHeight*0.1f); 
        userName.setPosition(screenWidth/2 - userName.getWidth()/2, screenHeight/2 + (userName.getHeight()*2));
        addActorsToStage(userName);

        tcpPort = new TextField("54555", textfieldSkin);
        tcpPort.setSize(screenWidth*0.6f, screenHeight*0.1f);
        tcpPort.setPosition(screenWidth/2 - tcpPort.getWidth()/2, screenHeight/2 +(tcpPort.getHeight())*0.5f);
        addActorsToStage(tcpPort);

        udpPort = new TextField("54777", textfieldSkin);
        udpPort.setSize(screenWidth*0.6f, screenHeight*0.1f);
        udpPort.setPosition(screenWidth/2 - udpPort.getWidth()/2, screenHeight/2.5f);
        addActorsToStage(udpPort);


        Texture hostTexture = SYAssetManager.getAsset(AssetDescriptors.HOST_GAME);
        host = new AliveButton(hostTexture);
        Vector2 btnHostSize = Scaling.fillX.apply(hostTexture.getWidth(), hostTexture.getHeight(), screenWidth*0.40f,0);
        host.setSize(btnHostSize.x, btnHostSize.y);
        host.setPosition(screenWidth/2+(host.getWidth()/80), screenHeight/10);
        addActorsToStage(host);

        Texture joinTexture = SYAssetManager.getAsset(AssetDescriptors.BUTTON_JOIN);
        join = new AliveButton(joinTexture);
        Vector2 btnJoinSize = Scaling.fillX.apply(joinTexture.getWidth(), joinTexture.getHeight(), screenWidth*0.40f,0);
        join.setSize(btnJoinSize.x, btnJoinSize.y);
        join.setPosition(screenWidth/2 - join.getWidth(), screenHeight/10);
        addActorsToStage(join);

        Texture leaveTexture = SYAssetManager.getAsset(AssetDescriptors.LEAVE);
        leave = new AliveButton(leaveTexture);
        Vector2 btnLeaveSize = Scaling.fillX.apply(leaveTexture.getWidth(), leaveTexture.getHeight(), screenWidth*0.10f, 0);
        leave.setSize(btnLeaveSize.x, btnLeaveSize.y);
        leave.setPosition(screenWidth-(leave.getWidth()/1.5f), screenHeight - leave.getHeight());
        addActorsToStage(leave);



        join.addListener(new AliveButton.AliveButtonListener() {
            @Override
            public void onClick() {
                ClientHandler client = new ClientHandler(networkPackageCallbacks);
                client.clientStart(userIP.getText(),Integer.parseInt(tcpPort.getText()),Integer.parseInt(udpPort.getText()));
                sound.play();
                screenManager.showScreen(GameScreen.class);
                screenManager.getScreen(GameScreen.class).initialize(client,networkPackageCallbacks);

            }
        });


        leave.addListener(new AliveButton.AliveButtonListener() {
            @Override
            public void onClick() {
                sound.play();
                //screenManager.showScreen(MainMenuScreen.class);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);

    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);

    }
}



