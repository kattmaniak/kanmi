package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends Application {
    public static final double WINDOW_WIDTH = 800.0;
    public static final double WINDOW_HEIGHT = 600.0;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.CADETBLUE;

    private Group objects;
    private Scene scene;
    private Stage stage;
    private Player player;
    private World world;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Node> blocks = new ArrayList<>();
    private ArrayList<Node> pickups = new ArrayList<>();

    private Group guiRoot;
    private long time = 0;
    private Label elapsedTime;
    private int score = 0;
    private Label scoreLabel;
    private double stamina = 200;
    private Rectangle staminaBar;
    private Rectangle healthBar;
    private double health = 100;

    private int enemiesFrozen = 0;
    private Label freezeTimer;
    private Label immunityTimer;
    private int immunity = 0;

    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    private final UpdateTimer timer = new UpdateTimer();

    private class UpdateTimer extends AnimationTimer {
        long coinTime = 0;
        long lastTime = 0;
        long staminaTime = 0;
        int staminaRecharge = 0;
        long energyTime = 0;
        long enemyTime = 0;
        long freezeTime = 0;
        long freezeGenTime = 0;
        long healthTime = 0;
        long pillTime = 0;
        long immunityUpdateTime = 0;
        long cannonTime = 0;
        long bulletTime = 0;
        long jokerCoinTime = 0;

        @Override
        public void handle(long now) {
            if (lastTime == 0) {
                lastTime = now;
            }
            if (energyTime == 0) {
                energyTime = now;
            }
            if (freezeTime == 0) {
                freezeTime = now;
            }
            if (freezeGenTime == 0) {
                freezeGenTime = now;
            }
            if (healthTime == 0) {
                healthTime = now;
            }
            if (pillTime == 0) {
                pillTime = now;
            }
            if (immunityUpdateTime == 0) {
                immunityUpdateTime = now;
            }
            if (cannonTime == 0) {
                cannonTime = now;
            }
            if (bulletTime == 0) {
                bulletTime = now;
            }
            if (jokerCoinTime == 0) {
                jokerCoinTime = now;
            }

            if (now - jokerCoinTime > 15e9) {
                JokerCoin jokerCoin = new JokerCoin(Math.random() * 400 - 200, Math.random() * 400 - 200);
                objects.getChildren().add(jokerCoin);
                pickups.add(jokerCoin);
                jokerCoinTime = now;
            }

            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.move();
                if (bullet.getTranslateX() > 300 || bullet.getTranslateX() < -300 || bullet.getTranslateZ() > 300 || bullet.getTranslateZ() < -300) {
                    objects.getChildren().remove(bullet);
                    bulletIterator.remove();
                }
                for (Node block : blocks) {
                    if (bullet.getBoundsInParent().intersects(block.getBoundsInParent())) {
                        objects.getChildren().remove(bullet);
                        bulletIterator.remove();
                        break;
                    }
                }
            }

            if (now - cannonTime > 4e9) {
                for (Cannon cannon : cannons) {
                    Bullet bullet = cannon.shoot();
                    objects.getChildren().add(bullet);
                    bullets.add(bullet);
                }
                cannonTime = now;
            }

            if (now - immunityUpdateTime > 1e9) {
                if (immunity > 0) {
                    immunity--;
                    immunityTimer.setText(String.valueOf(immunity));
                }
                immunityUpdateTime = now;
            }

            if (now - pillTime > 10e9) {
                if (Math.random() < 0.1) {
                    Immunity pill = new Immunity(Math.random() * 400 - 200, Math.random() * 400 - 200);
                    objects.getChildren().add(pill);
                    pickups.add(pill);
                    pillTime = now;
                }
            }

            if (now - healthTime > 10e9) {
                if (Math.random() < 0.2) {
                    Health health = new Health(Math.random() * 400 - 200, Math.random() * 400 - 200);
                    objects.getChildren().add(health);
                    pickups.add(health);
                    healthTime = now;
                }
            }

            if (now - freezeTime > 1e9) {
                if (enemiesFrozen > 0) {
                    enemiesFrozen--;
                    freezeTimer.setText(String.valueOf(enemiesFrozen));
                }
                freezeTime = now;
            }
            if (now - freezeGenTime > 5e9) {
                if (Math.random() < 0.3) {
                    Freeze freeze = new Freeze(Math.random() * 400 - 200, Math.random() * 400 - 200);
                    objects.getChildren().add(freeze);
                    pickups.add(freeze);
                }
                freezeGenTime = now;
            }

            updatePlayer(now);
            for (Enemy enemy : enemies) {
                if (enemiesFrozen == 0) {
                    if (now - enemyTime > 2e9) {
                        enemyTime = now;
                        if (enemy instanceof SpikedEnemy) {
                            ((SpikedEnemy) enemy).changeDirection();
                        }
                    }
                    enemy.move(player.getTranslateX(), player.getTranslateZ(), blocks);
                    if (enemy instanceof SpikedEnemy) {
                        for (Node node : pickups) {
                            if (node.getBoundsInParent().intersects(((SpikedEnemy) enemy).getBoundsInParent())) {
                                pickups.remove(node);
                                objects.getChildren().remove(node);
                                break;
                            }
                        }
                    } else if (enemy instanceof FollowerEnemy) {
                        for (Node node : pickups) {
                            if (node.getBoundsInParent().intersects(((FollowerEnemy) enemy).getBoundsInParent())) {
                                pickups.remove(node);
                                objects.getChildren().remove(node);
                                break;
                            }
                        }
                    }
                }
            }

            if (now - coinTime > 5e9) {
                double rnd = Math.random();
                if (rnd < 0.5) {
                    Coin coin = new Coin(Math.random() * 400 - 200, Math.random() * 400 - 200);
                    objects.getChildren().add(coin);
                    pickups.add(coin);
                } else if (rnd < 0.8) {
                    GreenCoin coin = new GreenCoin(Math.random() * 400 - 200, Math.random() * 400 - 200);
                    objects.getChildren().add(coin);
                    pickups.add(coin);
                } else {
                    BlueCoin coin = new BlueCoin(Math.random() * 400 - 200, Math.random() * 400 - 200);
                    objects.getChildren().add(coin);
                    pickups.add(coin);
                }
                coinTime = now;
            }

            if (now - lastTime > 1e9) {
                time += 1;
                lastTime = now;
                DateFormat df = new SimpleDateFormat("mm:ss");
                elapsedTime.setText(df.format(time * 1000));
            }

            if (now - energyTime > 12e9) {
                Energy energy = new Energy(Math.random() * 400 - 200, Math.random() * 400 - 200);
                objects.getChildren().add(energy);
                pickups.add(energy);
                energyTime = now;
            }

            if (now - staminaTime > 1e8) {
                if (player.isMoving()) {
                    if (stamina > 0) {
                        stamina -= 0.5;
                        staminaBar.setWidth(stamina);
                        staminaTime = now;
                    }
                } else {
                    if (stamina < 200) {
                        if (staminaRecharge == 10) {
                            stamina += 0.5;
                            staminaBar.setWidth(stamina);
                            staminaRecharge = 0;
                        } else {
                            staminaRecharge++;
                        }
                    }
                }
            }
        }
    }

    private void setupScene() {
        Group root = new Group();
        objects = new Group();
        guiRoot = new Group();
        scene = new Scene(guiRoot,
                WINDOW_WIDTH,
                WINDOW_HEIGHT);
        SubScene game = new SubScene(root, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.DISABLED);
        guiRoot.getChildren().addAll(game);

        DateFormat df = new SimpleDateFormat("mm:ss");
        elapsedTime = new Label(df.format(time));
        elapsedTime.setFont(Font.font("Arial", 20));
        elapsedTime.setTextFill(Color.RED);
        elapsedTime.setTranslateX(WINDOW_WIDTH - 80);
        elapsedTime.setTranslateY(20);
        guiRoot.getChildren().add(elapsedTime);

        scoreLabel = new Label(String.valueOf(score));
        scoreLabel.setFont(Font.font("Arial", 20));
        scoreLabel.setTextFill(Color.YELLOW);
        scoreLabel.setTranslateX(20);
        scoreLabel.setTranslateY(20);
        guiRoot.getChildren().add(scoreLabel);

        staminaBar = new Rectangle(200, 20, Color.YELLOW);
        staminaBar.setTranslateX(WINDOW_WIDTH / 2 - 100);
        staminaBar.setTranslateY(20);
        guiRoot.getChildren().add(staminaBar);
        Rectangle staminaBarBorder = new Rectangle(200, 20, Color.TRANSPARENT);
        staminaBarBorder.setStroke(Color.BLACK);
        staminaBarBorder.setStrokeWidth(2);
        staminaBarBorder.setTranslateX(WINDOW_WIDTH / 2 - 100);
        staminaBarBorder.setTranslateY(20);
        guiRoot.getChildren().add(staminaBarBorder);

        healthBar = new Rectangle(200, 20, Color.RED);
        healthBar.setTranslateX(WINDOW_WIDTH / 2 - 100);
        healthBar.setTranslateY(50);
        guiRoot.getChildren().add(healthBar);
        Rectangle healthBarBorder = new Rectangle(200, 20, Color.TRANSPARENT);
        healthBarBorder.setStroke(Color.BLACK);
        healthBarBorder.setStrokeWidth(2);
        healthBarBorder.setTranslateX(WINDOW_WIDTH / 2 - 100);
        healthBarBorder.setTranslateY(50);
        guiRoot.getChildren().add(healthBarBorder);

        freezeTimer = new Label(String.valueOf(enemiesFrozen));
        freezeTimer.setFont(Font.font("Arial", 20));
        freezeTimer.setTextFill(Color.AQUA);
        freezeTimer.setTranslateX(20);
        freezeTimer.setTranslateY(WINDOW_HEIGHT - 40);
        guiRoot.getChildren().add(freezeTimer);

        immunityTimer = new Label(String.valueOf(immunity));
        immunityTimer.setFont(Font.font("Arial", 20));
        immunityTimer.setTextFill(Color.PURPLE);
        immunityTimer.setTranslateX(50);
        immunityTimer.setTranslateY(WINDOW_HEIGHT - 40);
        guiRoot.getChildren().add(immunityTimer);

        scene.setFill(DEFAULT_BACKGROUND_COLOR);
        scene.setCursor(Cursor.NONE);

        player = new Player();
        game.setCamera(player.getCamera());
        scene.setOnMouseMoved(player);
        scene.setOnKeyPressed(player);
        scene.setOnKeyReleased(player);

        world = new World();

        AmbientLight ambientLight = new AmbientLight(Color.DARKGRAY);
        ambientLight.setOpacity(0.2);
        ambientLight.setBlendMode(BlendMode.SOFT_LIGHT);
        PointLight pointLight = new PointLight(Color.WHITESMOKE);
        pointLight.setTranslateY(-100);

        for (int i = 0; i < 10; ++i) {
            Block block = new Block(Math.random() * 400 - 200, Math.random() * 400 - 200);
            blocks.add(block);
            objects.getChildren().add(block);
        }
        for (int i = 0; i < 5; i++) {
            ObstacleL obstacleL = new ObstacleL(Math.random() * 400 - 200, Math.random() * 400 - 200, Math.random() > 0.5);
            objects.getChildren().add(obstacleL);
            blocks.add(obstacleL);
        }
        for (int i = 0; i < 5; i++) {
            ObstacleP obstacleP = new ObstacleP(Math.random() * 400 - 200, Math.random() * 400 - 200, Math.random() > 0.5);
            objects.getChildren().add(obstacleP);
            blocks.add(obstacleP);
        }

        for (int i = 0; i < 3; i++) {
            SpikedEnemy enemy = new SpikedEnemy(Math.random() * 400 - 200, Math.random() * 400 - 200);
            boolean collides = true;
            while (collides) {
                collides = false;
                for (Node block : blocks) {
                    if (enemy.getBoundsInParent().intersects(block.getBoundsInParent())) {
                        collides = true;
                        enemy.setTranslateX(Math.random() * 400 - 200);
                        enemy.setTranslateZ(Math.random() * 400 - 200);
                        break;
                    }
                }
                if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    collides = true;
                    enemy.setTranslateX(Math.random() * 400 - 200);
                    enemy.setTranslateZ(Math.random() * 400 - 200);
                }
            }
            objects.getChildren().add(enemy);
            enemies.add(enemy);
        }

        FollowerEnemy enemy = new FollowerEnemy(Math.random() * 400 - 200, Math.random() * 400 - 200);
        boolean collides = true;
        while (collides) {
            collides = false;
            for (Node block : blocks) {
                if (enemy.getBoundsInParent().intersects(block.getBoundsInParent())) {
                    collides = true;
                    enemy.setTranslateX(Math.random() * 400 - 200);
                    enemy.setTranslateZ(Math.random() * 400 - 200);
                    break;
                }
            }
            if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())) {
                collides = true;
                enemy.setTranslateX(Math.random() * 400 - 200);
                enemy.setTranslateZ(Math.random() * 400 - 200);
            }
        }

        objects.getChildren().add(enemy);
        enemies.add(enemy);

        double choice = Math.random();
        boolean leftChosen = choice < 0.25;
        boolean rightChosen = choice >= 0.25 && choice < 0.5;
        boolean upChosen = choice >= 0.5 && choice < 0.75;
        boolean downChosen = choice >= 0.75;
        for (int i = 0; i < 2; i++) {

            Cannon cannon = new Cannon(leftChosen ? -300 : rightChosen ? 300 : -100 + i * 200, upChosen ? -300 : downChosen ? 300 : -100 + i * 200);
            objects.getChildren().add(cannon);
            cannons.add(cannon);
        }

        /*JokerCoin jokerCoin = new JokerCoin(0, 0);
        objects.getChildren().add(jokerCoin);*/

        Coin coin1 = new Coin(200, 200);
        Coin coin2 = new Coin(200, -200);
        Coin coin3 = new Coin(-200, 200);
        Coin coin4 = new Coin(-200, -200);
        pickups.add(coin1);
        pickups.add(coin2);
        pickups.add(coin3);
        pickups.add(coin4);
        objects.getChildren().addAll(coin1, coin2, coin3, coin4);
        root.getChildren().addAll(world, player, ambientLight, pointLight, objects);
    }

    private void showStage() {
        stage.setTitle("Kanmi");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        timer.start();
    }

    private void updatePlayer(long now) {
        double oldX = player.getTranslateX();
        double oldZ = player.getTranslateZ();
        if (player.isUpPressed()) {
            player.setTranslateX(player.getTranslateX() + Player.PLAYER_SPEED * ((double) stamina / 200) * Math.cos(Math.toRadians(player.getRotate() - 90)));
            player.setTranslateZ(player.getTranslateZ() - Player.PLAYER_SPEED * ((double) stamina / 200) * Math.sin(Math.toRadians(player.getRotate() - 90)));
        }
        if (player.isDownPressed()) {
            player.setTranslateX(player.getTranslateX() + Player.PLAYER_SPEED * ((double) stamina / 200) * Math.cos(Math.toRadians(player.getRotate() + 90)));
            player.setTranslateZ(player.getTranslateZ() - Player.PLAYER_SPEED * ((double) stamina / 200) * Math.sin(Math.toRadians(player.getRotate() + 90)));
        }
        if (player.isLeftPressed()) {
            player.setTranslateX(player.getTranslateX() - Player.PLAYER_SPEED * ((double) stamina / 200) * Math.cos(Math.toRadians(player.getRotate())));
            player.setTranslateZ(player.getTranslateZ() + Player.PLAYER_SPEED * ((double) stamina / 200) * Math.sin(Math.toRadians(player.getRotate())));
        }
        if (player.isRightPressed()) {
            player.setTranslateX(player.getTranslateX() + Player.PLAYER_SPEED * ((double) stamina / 200) * Math.cos(Math.toRadians(player.getRotate())));
            player.setTranslateZ(player.getTranslateZ() - Player.PLAYER_SPEED * ((double) stamina / 200) * Math.sin(Math.toRadians(player.getRotate())));
        }

        if (player.getTranslateX() + Player.PLAYER_RADIUS / 2 >= world.getBoundsInLocal().getMaxX() ||
                player.getTranslateX() - Player.PLAYER_RADIUS / 2 <= world.getBoundsInLocal().getMinX())
            player.setTranslateX(oldX);
        if (player.getTranslateZ() + Player.PLAYER_RADIUS / 2 >= world.getBoundsInLocal().getMaxZ() ||
                player.getTranslateZ() - Player.PLAYER_RADIUS / 2 <= world.getBoundsInLocal().getMinZ())
            player.setTranslateZ(oldZ);

        for (int i = 0; i < objects.getChildren().size(); ++i) {
            Node node = objects.getChildren().get(i);
            if (node instanceof Coin) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    ++score;
                    scoreLabel.setText(String.valueOf(score));
                    --i;
                }
            } else if (node instanceof GreenCoin) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    score += 3;
                    scoreLabel.setText(String.valueOf(score));
                    --i;
                }
            } else if (node instanceof BlueCoin) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    score += 5;
                    scoreLabel.setText(String.valueOf(score));
                    --i;
                }
            } else if (node instanceof Block || node instanceof ObstacleL || node instanceof ObstacleP) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    player.setTranslateX(oldX);
                    player.setTranslateZ(oldZ);
                }
            } else if (node instanceof Energy) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    stamina += 66;
                    if (stamina > 200) stamina = 200;
                    staminaBar.setWidth(stamina);
                    --i;
                }
            } else if (node instanceof SpikedEnemy || node instanceof FollowerEnemy) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    if (immunity == 0) {
                        if (health > 0) {
                            health -= 0.5;
                            healthBar.setWidth(health * 2);
                        } else {
                            gameOver();
                        }
                    }
                }
            } else if (node instanceof Freeze) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    enemiesFrozen += 10;
                    if (enemiesFrozen > 10) enemiesFrozen = 10;
                    freezeTimer.setText(String.valueOf(enemiesFrozen));
                    --i;
                }
            } else if (node instanceof Health) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    health += 25;
                    if (health > 100) health = 100;
                    healthBar.setWidth(health * 2);
                    --i;
                }
            } else if (node instanceof Immunity) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    immunity += 10;
                    if (immunity > 10) immunity = 10;
                    immunityTimer.setText(String.valueOf(immunity));
                    --i;
                }
            } else if (node instanceof Bullet) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    if (immunity == 0) {
                        health -= 15;
                        if (health > 0) {
                            healthBar.setWidth(health * 2);
                        } else {
                            gameOver();
                        }
                    }
                    objects.getChildren().remove(node);
                    bullets.remove(node);
                    --i;
                }
            } else if (node instanceof JokerCoin) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    objects.getChildren().remove(node);
                    pickups.remove(node);
                    double choice = Math.random();
                    if (choice < 0.1) {
                        immunity += 10;
                        if (immunity > 10) immunity = 10;
                        immunityTimer.setText(String.valueOf(immunity));
                    } else if (choice < 0.2) {
                        enemiesFrozen += 10;
                        if (enemiesFrozen > 10) enemiesFrozen = 10;
                        freezeTimer.setText(String.valueOf(enemiesFrozen));
                    } else if (choice < 0.4) {
                        health -= 20;
                        stamina += 40;
                        if (health < 0) {
                            health = 1;
                        }
                        if (stamina > 200) stamina = 200;
                        staminaBar.setWidth(stamina);
                        healthBar.setWidth(health * 2);
                    } else if (choice < 0.6) {
                        health += 20;
                        stamina -= 40;
                        if (stamina < 0) {
                            stamina = 1;
                        }
                        if (health > 100) health = 100;
                        staminaBar.setWidth(stamina);
                        healthBar.setWidth(health * 2);
                    } else {
                        int numOfCoins = (int) (Math.random() * 10) + 1;
                        score += numOfCoins;
                        scoreLabel.setText(String.valueOf(score));
                    }
                    --i;
                }

            }

        }
    }

    private void gameOver() {
        timer.stop();
        Label gameOver = new Label("Game Over");
        gameOver.setFont(Font.font("Arial", 50));
        gameOver.setTextFill(Color.RED);
        gameOver.setTranslateX(WINDOW_WIDTH / 2 - 120);
        gameOver.setTranslateY(WINDOW_HEIGHT / 2 - 60);
        Label scoreTotal = new Label("Score: " + score);
        scoreTotal.setFont(Font.font("Arial", 30));
        scoreTotal.setTextFill(Color.YELLOW);
        scoreTotal.setTranslateX(WINDOW_WIDTH / 2 - 50);
        scoreTotal.setTranslateY(WINDOW_HEIGHT / 2);
        guiRoot.getChildren().addAll(gameOver, scoreTotal);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        setupScene();
        showStage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
