#!/bin/bash

# This script compiles and runs the BrickBreaker JavaFX game
# It will automatically download JavaFX if not present

JAVAFX_VERSION="23.0.1"
JAVAFX_DIR="$HOME/javafx-sdk"
JAVAFX_LIB="$JAVAFX_DIR/javafx-sdk-$JAVAFX_VERSION/lib"

# Check if JavaFX is already installed
if [ ! -d "$JAVAFX_LIB" ]; then
    echo "JavaFX not found. Installing..."
    mkdir -p "$JAVAFX_DIR"
    cd "$JAVAFX_DIR"
    
    # Download JavaFX SDK from official Maven repository
    echo "Downloading JavaFX SDK..."
    
    # Detect architecture
    ARCH=$(uname -m)
    if [ "$ARCH" = "arm64" ]; then
        JAVAFX_URL="https://download2.gluonhq.com/openjfx/23.0.1/openjfx-23.0.1_macos-aarch64_bin-sdk.zip"
    else
        JAVAFX_URL="https://download2.gluonhq.com/openjfx/23.0.1/openjfx-23.0.1_macos-x64_bin-sdk.zip"
    fi
    
    # Try downloading with curl
    if ! curl -f -L -o "javafx-sdk.zip" "$JAVAFX_URL" 2>/dev/null; then
        echo "Direct download failed. Please manually download JavaFX SDK from:"
        echo "https://gluonhq.com/products/javafx/"
        echo "Extract it to: $JAVAFX_DIR"
        exit 1
    fi
    
    unzip -q javafx-sdk.zip
    rm javafx-sdk.zip
    echo "JavaFX installed successfully!"
fi

# Navigate to the project directory
cd "$(dirname "$0")"

# Compile
echo "Compiling BrickBreaker.java..."
javac --module-path "$JAVAFX_LIB" --add-modules javafx.controls BrickBreaker.java

if [ $? -eq 0 ]; then
    echo "Compilation successful! Running the game..."
    java --module-path "$JAVAFX_LIB" --add-modules javafx.controls BrickBreaker
else
    echo "Compilation failed!"
    exit 1
fi
