package pong;

/**
 *
 * @author heete
 */
import java.io.FileInputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author heete
 */
public class Pong extends Application {

    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Pong Standaard");
        Pong.PongGame pong = new Pong.PongGame();
        btn.setOnAction(event -> {

            try {
                pong.start(primaryStage);
            } catch (Exception ex) {
                Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        StackPane root = new StackPane();

        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Pong Menu");
        primaryStage.setScene(scene);

        primaryStage.show();

    }
//PongGame

    public class PongGame {

        private static final int width = 800;
        private static final int height = 600;
        private static final int PLAYER_HEIGHT = 100;
        private static final int PLAYER_WIDTH = 15;
        private static final double BALL_R = 15;
        private int ballYSpeed = 5;
        private int ballXSpeed = 5;
        private double playerOneYPos = height / 2;
        private double playerTwoYPos = height / 2;
        private double ballXPos = width / 2;
        private double ballYPos = height / 2;
        private int scoreP1 = 0;
        private int scoreP2 = 0;
        private boolean gameStarted;
        private int playerOneXPos = 0;
        private double playerTwoXPos = width - PLAYER_WIDTH;

        //image background
        public void start(Stage stage) throws Exception {

            Group root = new Group();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);

            Canvas canvas = new Canvas(width, height);
            canvas.setOnMouseMoved(e -> playerOneYPos = e.getY());
            canvas.setOnMouseClicked(e -> gameStarted = true);
            root.getChildren().add(canvas);

            GraphicsContext gc = canvas.getGraphicsContext2D();

            Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
            tl.setCycleCount(Timeline.INDEFINITE);

            //sound

            
            //background
            HBox hbox = new HBox();

            FileInputStream input = new FileInputStream("C:\\Users\\heete\\Desktop\\school\\java\\H2\\Pong\\src\\photos\\background-pong.png");

            Image image = new Image(input);

            BackgroundImage backgroundimage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);

            Background background = new Background(backgroundimage);

            hbox.setBackground(background);

            tl.play();
            stage.show();

        }
//VOOR MENU GEBRUIK MULTITHREADING

        public void run(GraphicsContext gc) {

            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, width, height);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(25));
            if (gameStarted) {
                ballXPos += ballXSpeed;
                ballYPos += ballYSpeed;
                if (ballXPos < width - width / 4) {
                    playerTwoYPos = ballYPos - PLAYER_HEIGHT / 2;
                } else {
                    playerTwoYPos = ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ? playerTwoYPos += 1 : playerTwoYPos - 1;
                }
                gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);
            } else {
                gc.setStroke(Color.YELLOW);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.strokeText("Click to Start", width / 2, height / 2);
                ballXPos = width / 2;
                ballYPos = height / 2;
                ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
                ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            }
            if (ballYPos > height || ballYPos < 0) {
                ballYSpeed *= -1;
            }
            if (ballXPos < playerOneXPos - PLAYER_WIDTH) {

                scoreP2++;
                gameStarted = false;
            }
            if (ballXPos > playerTwoXPos + PLAYER_WIDTH) {
                scoreP1++;
                gameStarted = false;
            }
            if (((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT)
                    || ((ballXPos < playerOneXPos + PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
                ballYSpeed += 1 * Math.signum(ballYSpeed);
                ballXSpeed += 1 * Math.signum(ballXSpeed);
                ballXSpeed *= -1;
                ballYSpeed *= -1;
            }
            gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);
            gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
            gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
    }

}
