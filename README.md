# Twenty-One-Game
## Shcherbakov Alexey, B01-908
# Requirements
## JFrame
* Java 8 or higher
## Applet
* Java 8
# Build
## JFrame
    javac Game.java Sprite.java
## Applet
    javac appl.java Game.java Sprite.java
# Run
## JFrame
    java Game
## Applet
    appletviewer index.html
# Game
This is simple Java-based graphic game without third-party libraries.  
It's realized in two different verisions: using JFrame class and Applet deprecated class  
You can find the rules of the game [here](https://en.wikipedia.org/wiki/Twenty-One_(banking_game))
# Important
You'll need to find bundle of assets due to its absence in this repository.   
Without it the game will throw exceptions at trying of loading those assets.
