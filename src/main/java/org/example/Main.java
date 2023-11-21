package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    float t = 0.f;
    Pane root = new Pane();

    Pane menu = new Pane();

    BorderPane game = new BorderPane();

    SpriteImg player = new SpriteImg("player", 100, 100, 500, 400, "/Images/spaceships.png");

    SpriteImg background = new SpriteImg("black", 1000, 800, 0, 0, "/Images/space.png");
    Canvas canvas = new Canvas(1000, 800);
    GraphicsContext context = canvas.getGraphicsContext2D();

    ArrayList<SpriteImg> playerShots = new ArrayList<>();
    ArrayList<SpriteImg> enemyShots = new ArrayList<>();
    ArrayList<SpriteImg> enemies = new ArrayList<>();
    Random rd = new Random();
    AnimationTimer timer = new AnimationTimer() {  // Создаем таймер для обновления root (чтобы спрайты могли двигаться)
        @Override
        public void handle(long l) {
            update();
        }
    };


    int scoreEnemy = 0;
    int lives = 3;

    Text scoreText = new Text(20, 750, "score: " + scoreEnemy);
    Font font = new Font(25);
    Text livesText = new Text(20, 775, "lives: " + lives);
    private Parent createGame(){
        scoreEnemy = 0;
        lives = 3;
        scoreText.setText("score: " + scoreEnemy);
        livesText.setText("score: " + lives);
        player.isDead = false;
        game.getChildren().clear();
        game.setPrefSize(1000, 800);


        game.setCenter(canvas);

        game.setStyle("-fx-background-color: #000020;");
        scoreText.setFont(font);
        scoreText.setFill(Color.WHITE);
        game.getChildren().add(scoreText);
        livesText.setFont(font);
        livesText.setFill(Color.WHITE);
        game.getChildren().add(livesText);

        background.draw(context);
        player.draw(context);

        return game;
    }
    private void showGame(){
        timer.start();
        root.getChildren().clear();
        root.getChildren().add(createGame());
    }

    private Parent createMenu(){
        menu.setPrefSize(1000, 800);
        menu.setStyle("-fx-background-color: #000020;");


        Button startButton = new Button();
        startButton.setText("START");
        startButton.setTranslateX(500);
        startButton.setTranslateY(400);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showGame();

            }
        });
        Button exitButton = new Button();
        exitButton.setText("EXIT");
        exitButton.setTranslateX(500);
        exitButton.setTranslateY(450);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });
        menu.getChildren().add(startButton);
        menu.getChildren().add(exitButton);
        return menu;
    }
    private void showMenu(){
        root.getChildren().clear();
        root.getChildren().add(createMenu());
    }

    private Parent createRoot()
    {
        root.setPrefSize(1000, 800);
        root.setStyle("-fx-background-color: #000020;");

        return root;
    }



    private void update()
    {
        t += 0.016;
        for (SpriteImg enemy: enemies){
            int shootChance = rd.nextInt(1001);
                    if (shootChance > 995)
                    {
                        enemyShots.add(shoot(enemy));
                    }
        }

        for (SpriteImg shot: playerShots){
            for (SpriteImg enemy: enemies){

                if(shot.collides(enemy)){
                    enemy.isDead = true;  
                  shot.isDead = true;  
                  scoreEnemy++;
                  scoreText.setText("score: " + scoreEnemy);
                }
            }
        }
        for (SpriteImg shot: enemyShots){
            if (!player.isDead){
                if(shot.collides(player)) {
                    player.isDead = true;
                    shot.isDead = true;
                    lives--;
                    livesText.setText("lives: " + lives);

                }
            }
        }


      // if(enemies.size()==0)
        if (enemies.isEmpty()){
           spawnEnemy(4);
       }

        playerShots.removeIf(shot -> (shot.isDead));
        enemyShots.removeIf(shot -> (shot.isDead));
        enemies.removeIf(enemy -> (enemy.isDead));

        background.draw(context);
        if(!player.isDead){
            player.draw(context);
        }




        for (SpriteImg enemy : enemies){
           if (player.collides(enemy))
               System.out.println("Collision detectet.");
            enemy.draw(context);
        }

        for (SpriteImg shot: playerShots){
            shot.draw(context);
            shot.setPosY(shot.getPosY() - 10);
        }
        for (SpriteImg shot: enemyShots){
            shot.draw(context);
            shot.setPosY(shot.getPosY() + 10);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createRoot());

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode())
                {
                    case A:
                        if (player.getPosX()> 0){
                            player.moveLeft();
                        }

                        break;
                    case D:
                         if (player.getPosX() + player.getW() < 1000){
                             player.moveRight();
                         }

                        break;
                    case W:
                        if(player.getPosY() > 0){
                            player.moveUp();
                        }
                        break;
                    case S:
                        if(player.getPosY()+player.getH() < 800){
                            player.moveDown();
                        }
                        break;
                    case SPACE:
                        if (!player.isDead){
                            playerShots.add(shoot(player));
                        }

                        break;
                    case R:
                        if(lives > 0){
                            player.isDead = false;
                        }
                        break;
                }
            }
        });

        showMenu();
        stage.setScene(scene);
        stage.show();
    }

    private SpriteImg shoot(SpriteImg who)
    {
        String rocketType;
        if(who.type.equals("player"))
        {
            rocketType = "player_rocket";

        }
        else {
            rocketType = "enemy_rocket";
        }


        SpriteImg rocket = new SpriteImg(rocketType, 10, 20, (int)(who.getPosX() + who.getW()/2), (int)who.getPosY(), "/Images/laser_blast.png");
        return rocket;


    }
    public void spawnEnemy(int numOfEnemies){
        for(int i = 0; i < numOfEnemies; i++){
            int x = rd.nextInt(1000);
            SpriteImg enemy = new SpriteImg("enemy", 150, 150, x, 100, "/Images/enemy2.png");  // Создаем спрайт врага

            enemies.add(enemy);

        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}