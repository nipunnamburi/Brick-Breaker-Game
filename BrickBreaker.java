import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BrickBreaker extends Application {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;

    // Paddle
    private Rectangle paddle;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 14;
    private static final double PADDLE_SPEED = 6.5;

    // Ball
    private Circle ball;
    private double dx = 3.5;
    private double dy = -3.5;
    private static final double BALL_RADIUS = 8;

    // Bricks
    private final List<Rectangle> bricks = new ArrayList<>();
    private static final int BRICK_ROWS = 6;
    private static final int BRICK_COLS = 10;
    private static final int BRICK_WIDTH = 52;
    private static final int BRICK_HEIGHT = 20;
    private static final int BRICK_GAP = 6;

    // Game state
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean gameOver = false;
    private boolean paused = false;
    private int score = 0;
    private int lives = 3;

    // UI
    private Text scoreText;
    private Text livesText;
    private Text messageText;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);

        createPaddle(root);
        createBall(root);
        createBricks(root);
        createUI(root);

        Scene scene = new Scene(root);
        setupInput(scene);

        stage.setScene(scene);
        stage.setTitle("Brick Breaker - JavaFX");
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            private long last = System.nanoTime();

            @Override
            public void handle(long now) {
                double delta = (now - last) / 1_000_000_000.0;
                if (delta > 0.05) delta = 0.05;
                last = now;
                if (!paused) update(delta, root);
            }
        };
        timer.start();
    }

    private void createPaddle(Pane root) {
        paddle = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT, Color.DODGERBLUE);
        paddle.setArcWidth(10);
        paddle.setArcHeight(10);
        paddle.setTranslateX((WIDTH - PADDLE_WIDTH) / 2.0);
        paddle.setTranslateY(HEIGHT - 60);
        root.getChildren().add(paddle);
    }

    private void createBall(Pane root) {
        ball = new Circle(BALL_RADIUS, Color.CRIMSON);
        resetBallAndPaddle();
        root.getChildren().add(ball);
    }

    private void createBricks(Pane root) {
        double startX = (WIDTH - (BRICK_COLS * BRICK_WIDTH + (BRICK_COLS - 1) * BRICK_GAP)) / 2.0;
        double startY = 60;
        Color[] colors = {Color.ORANGE, Color.GOLD, Color.LIGHTGREEN, Color.CORNFLOWERBLUE, Color.PLUM, Color.PINK};

        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLS; col++) {
                double x = startX + col * (BRICK_WIDTH + BRICK_GAP);
                double y = startY + row * (BRICK_HEIGHT + BRICK_GAP);
                Rectangle brick = new Rectangle(BRICK_WIDTH, BRICK_HEIGHT, colors[row % colors.length]);
                brick.setTranslateX(x);
                brick.setTranslateY(y);
                brick.setArcWidth(6);
                brick.setArcHeight(6);
                bricks.add(brick);
                root.getChildren().add(brick);
            }
        }
    }

    private void createUI(Pane root) {
        scoreText = new Text("Score: 0");
        scoreText.setFont(Font.font(20));
        scoreText.setTranslateX(12);
        scoreText.setTranslateY(26);

        livesText = new Text("Lives: 3");
        livesText.setFont(Font.font(20));
        livesText.setTranslateX(WIDTH - 110);
        livesText.setTranslateY(26);

        messageText = new Text("");
        messageText.setFont(Font.font(28));
        messageText.setTranslateX(80);
        messageText.setTranslateY(HEIGHT / 2.0);

        root.getChildren().addAll(scoreText, livesText, messageText);
    }

    private void setupInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = true;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
            if (e.getCode() == KeyCode.P) paused = !paused;
            if (e.getCode() == KeyCode.R && gameOver) resetGame((Pane) scene.getRoot());
            if (e.getCode() == KeyCode.SPACE && !gameOver) { // space to launch if ball stuck
                if (Math.abs(dx) < 1e-6 && Math.abs(dy) < 1e-6) {
                    dx = 3.5; dy = -3.5;
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        });

        scene.setOnMouseMoved(e -> {
            double mx = e.getX();
            double newX = mx - PADDLE_WIDTH / 2.0;
            if (newX < 0) newX = 0;
            if (newX > WIDTH - PADDLE_WIDTH) newX = WIDTH - PADDLE_WIDTH;
            paddle.setTranslateX(newX);
        });

        scene.setOnMouseClicked(e -> {
            if (gameOver) resetGame((Pane) scene.getRoot());
        });
    }

    private void update(double delta, Pane root) {
        // Move paddle via keyboard
        if (leftPressed) paddle.setTranslateX(Math.max(0, paddle.getTranslateX() - PADDLE_SPEED));
        if (rightPressed) paddle.setTranslateX(Math.min(WIDTH - PADDLE_WIDTH, paddle.getTranslateX() + PADDLE_SPEED));

        // Move ball
        ball.setTranslateX(ball.getTranslateX() + dx);
        ball.setTranslateY(ball.getTranslateY() + dy);

        // Wall collisions
        if (ball.getTranslateX() - BALL_RADIUS <= 0) {
            ball.setTranslateX(BALL_RADIUS);
            dx = -dx;
        }
        if (ball.getTranslateX() + BALL_RADIUS >= WIDTH) {
            ball.setTranslateX(WIDTH - BALL_RADIUS);
            dx = -dx;
        }
        if (ball.getTranslateY() - BALL_RADIUS <= 0) {
            ball.setTranslateY(BALL_RADIUS);
            dy = -dy;
        }

        // Paddle collision
        Bounds ballBounds = ball.getBoundsInParent();
        if (ballBounds.intersects(paddle.getBoundsInParent())) {
            // reflect depending on where it hits the paddle
            double paddleCenter = paddle.getTranslateX() + PADDLE_WIDTH / 2.0;
            double hitPos = (ball.getTranslateX() - paddleCenter) / (PADDLE_WIDTH / 2.0); // -1 .. 1
            double angle = hitPos * Math.toRadians(60); // tilt up to 60 degrees
            double speed = Math.sqrt(dx*dx + dy*dy);
            dx = speed * Math.sin(angle);
            dy = -Math.abs(speed * Math.cos(angle));
            // ensure ball is placed above paddle
            ball.setTranslateY(paddle.getTranslateY() - BALL_RADIUS - 1);
        }

        // Brick collisions
        Iterator<Rectangle> it = bricks.iterator();
        while (it.hasNext()) {
            Rectangle brick = it.next();
            if (ball.getBoundsInParent().intersects(brick.getBoundsInParent())) {
                // basic collision response: reflect vertically or horizontally depending on overlap
                Bounds b = ball.getBoundsInParent();
                Bounds r = brick.getBoundsInParent();

                double overlapLeft = b.getMaxX() - r.getMinX();
                double overlapRight = r.getMaxX() - b.getMinX();
                double overlapTop = b.getMaxY() - r.getMinY();
                double overlapBottom = r.getMaxY() - b.getMinY();

                double minOverlap = Math.min(Math.min(Math.abs(overlapLeft), Math.abs(overlapRight)),
                        Math.min(Math.abs(overlapTop), Math.abs(overlapBottom)));

                if (minOverlap == Math.abs(overlapLeft) || minOverlap == Math.abs(overlapRight)) {
                    dx = -dx;
                } else {
                    dy = -dy;
                }

                // remove brick
                root.getChildren().remove(brick);
                it.remove();
                score += 10;
                scoreText.setText("Score: " + score);
                break; // only handle one brick per frame to avoid tunneling
            }
        }

        // Check for loss
        if (ball.getTranslateY() - BALL_RADIUS > HEIGHT) {
            lives--;
            livesText.setText("Lives: " + lives);
            if (lives <= 0) {
                endGame();
            } else {
                resetBallAndPaddle();
            }
        }

        // Check for win
        if (bricks.isEmpty()) {
            messageText.setText("You Win! Press R to play again");
            gameOver = true;
        }
    }

    private void resetBallAndPaddle() {
        paddle.setTranslateX((WIDTH - PADDLE_WIDTH) / 2.0);
        ball.setTranslateX(WIDTH / 2.0);
        ball.setTranslateY(paddle.getTranslateY() - BALL_RADIUS - 2);
        // put ball stationary until player launches
        dx = 0;
        dy = 0;
    }

    private void resetGame(Pane root) {
        // remove remaining bricks if any
        for (Rectangle b : new ArrayList<>(bricks)) {
            root.getChildren().remove(b);
        }
        bricks.clear();
        createBricks(root);

        score = 0; lives = 3; gameOver = false; paused = false;
        scoreText.setText("Score: 0");
        livesText.setText("Lives: " + lives);
        messageText.setText("");
        resetBallAndPaddle();
    }

    private void endGame() {
        gameOver = true;
        messageText.setText("Game Over! Press R to restart");
        // stop ball
        dx = 0; dy = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
