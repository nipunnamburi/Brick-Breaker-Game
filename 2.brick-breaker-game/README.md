# Brick Breaker Game - JavaFX

A classic brick breaker game built with JavaFX.

## Game Controls

- **Mouse**: Move paddle left/right
- **Arrow Keys**: Move paddle left/right (keyboard control)
- **SPACE**: Launch the ball (when it's stationary)
- **P**: Pause/unpause the game
- **R**: Restart the game (after game over or win)

## Features

- 60 colorful bricks to break
- Lives system (3 lives)
- Score tracking
- Paddle angle mechanics for strategic ball control
- Smooth animations
- Pause functionality

## Installation & Setup

### Option 1: Using Homebrew and Maven

1. Install Maven if not already installed:
   ```bash
   brew install maven
   ```

2. Navigate to the project directory:
   ```bash
   cd "/Users/nipunnamburi/Downloads/VS Code/geeksforgeeksprojects/2.brickbreakergame"
   ```

3. Use the provided Maven commands below

### Option 2: Manual JavaFX Setup

1. Download JavaFX SDK from: https://gluonhq.com/products/javafx/
   - Select version 23 or later
   - Choose macOS (for Apple Silicon, choose "aarch64"; for Intel, choose "x64")

2. Extract the downloaded ZIP file to a location, e.g., `~/javafx-sdk-23.0.1`

3. Note the path to the `lib` folder inside the extracted directory

## Running the Game

### Using Maven (Recommended)

If you have Maven installed, run:

```bash
mvn clean javafx:run
```

### Using javac and java commands

Replace `<path-to-javafx-lib>` with the actual path to your JavaFX lib folder:

**Compile:**
```bash
javac --module-path <path-to-javafx-lib> --add-modules javafx.controls BrickBreaker.java
```

**Run:**
```bash
java --module-path <path-to-javafx-lib> --add-modules javafx.controls BrickBreaker
```

**Example (if JavaFX is in ~/javafx-sdk-23.0.1):**
```bash
javac --module-path ~/javafx-sdk-23.0.1/lib --add-modules javafx.controls BrickBreaker.java
java --module-path ~/javafx-sdk-23.0.1/lib --add-modules javafx.controls BrickBreaker
```

### Using the provided script

Make the script executable and run it:
```bash
chmod +x run.sh
./run.sh
```

Note: The script will attempt to download JavaFX automatically, but if that fails, you'll need to download it manually as described above.

## Troubleshooting

**Error: JavaFX runtime components are missing**
- Ensure you've downloaded JavaFX SDK
- Verify the path to the JavaFX lib folder is correct
- Make sure you're using the correct architecture (aarch64 for M1/M2/M3 Macs)

**Game window doesn't appear**
- Check that your Java version is 11 or higher
- Verify JavaFX modules are properly loaded

**Compilation errors**
- Ensure all JavaFX imports are available
- Check that you're using `--add-modules javafx.controls` flag

## Game Objective

Break all the bricks to win! You have 3 lives. The ball bounces at different angles depending on where it hits the paddle, allowing for strategic gameplay.

Enjoy!
