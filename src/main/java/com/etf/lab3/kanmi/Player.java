package com.etf.lab3.kanmi;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

import static com.etf.lab3.kanmi.Game.WINDOW_HEIGHT;
import static com.etf.lab3.kanmi.Game.WINDOW_WIDTH;

public class Player extends Group implements EventHandler<Event>
{
    public static final double NEAR_CLIP = 0.1;
    public static final double FAR_CLIP = 10_000;
    public static final double FIELD_OF_VIEW = 60;
    public static final double PLAYER_SPEED = 0.8;
    public static final double PLAYER_RADIUS = 100;

    private final PerspectiveCamera camera;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean jumping = false;
    private boolean isGameActive = true;

    private double oldMouseX = WINDOW_WIDTH / 2;
    private double oldMouseY = WINDOW_HEIGHT / 2;


    public Player()
    {
        Sphere shape = new Sphere(PLAYER_RADIUS);
        shape.setVisible(false);

        camera = new PerspectiveCamera(true);
        camera.setNearClip(NEAR_CLIP);
        camera.setFarClip(FAR_CLIP);
        camera.setFieldOfView(FIELD_OF_VIEW);

        setRotationAxis(new Point3D(0, 1, 0));
        this.getChildren().addAll(shape, camera);
    }

    @Override
    public void handle(Event event)
    {
        if (event instanceof KeyEvent keyEvent)
        {
            if (keyEvent.getCode() == KeyCode.ESCAPE && keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                System.exit(0);
            else
            {
                if (!isGameActive)
                {
                    upPressed = false;
                    downPressed = false;
                    rightPressed = false;
                    leftPressed = false;
                    return;
                }

                if (keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        upPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        upPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        downPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        downPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        leftPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        leftPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        rightPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        rightPressed = false;
                } else if (keyEvent.getCode() == KeyCode.SPACE){

                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED){
                        if(!jumping) {
                            jumping = true;
                            Animation jump = new Transition() {
                                {
                                    setCycleDuration(Duration.millis(1000));
                                    setOnFinished(e -> jumping = false);
                                }

                                @Override
                                protected void interpolate(double frac) {
                                    setTranslateY(-10 * Math.sin(frac * Math.PI));
                                }
                            };
                            jump.playFromStart();
                        }
                    }

                }
            }
        }
        else if (event instanceof MouseEvent mouseEvent)
        {
            if (MouseEvent.MOUSE_MOVED.equals(mouseEvent.getEventType())) {
                double dx = mouseEvent.getSceneX() - oldMouseX;
                //System.out.println("Mouse x: " + mouseEvent.getSceneX() + " oldMouseX: " + oldMouseX);

                if(Math.abs(dx) > 10) {
                    setRotate(getRotate() + Math.PI * (dx * 15 / WINDOW_WIDTH));
                    Robot robot = new Robot();
                    robot.mouseMove(getScene().getWindow().getX() + getScene().getWidth() / 2+9, getScene().getWindow().getY() + getScene().getHeight() / 2);
                    //System.out.println("Mouse moved, mouse x: " + mouseEvent.getSceneX() + " oldMouseX: " + oldMouseX);
                }
            }
        }
    }

    public Camera getCamera()
    {
        return camera;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public void setGameActive(boolean gameActive) {
        isGameActive = gameActive;
    }

    public boolean isMoving() {
        return upPressed || downPressed || leftPressed || rightPressed;
    }
}
