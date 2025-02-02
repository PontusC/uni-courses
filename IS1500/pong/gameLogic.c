/*
  Contains logic and variables for the game state of a Pong game.
  Also checks for input.
  Authors:  Pontus Curtsson
            Lovisa Montelius
*/

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

// Function prototypes
void gameInitialize();
int gameover();
void resetGame();
void gameTick();
void ballTick();
void setBallTicks();
void paddleTick();
void setPaddleState();

// Game border
const int leftBorder = 0;
const int rightBorder = 127;

// 0 if disabled, 1 if enabled
int AIEnabled = 1;

// Controls stating game difficulty
int gameDifficulty = 1;

// Ticks for object movement
int ballXTickCounter = 0;
int ballYTickCounter = 0;
int paddleTicks = 0;

// Tick limit is used as # of clock ticks before ball moves
int ballXTickLimit = 18;
int ballYTickLimit = 35;

// States for paddle movement, used to store button presses
// 0 is stored move down, 1 is stored up, 2 is do nothing
int leftPaddleState = 2;
int rightPaddleState = 2;

// Sets up the game
void gameInitialize(){
  drawPaddle(leftXpos, leftYpos);
  drawPaddle(rightXpos, rightYpos);
  ballXTickLimit -= gameDifficulty;
  ballYTickLimit -= gameDifficulty;
}

// Checks if ball is going out-of-bounds
int gameover(){
  return outOfBounds();
}

// Resets all objects values
void resetGame(){
  // Resets all positions
  ballXpos = 63;
  ballYpos = 15;
  ballXTickLimit = 18;
  ballYTickLimit = 18;
  //ballXdir = 0;
  //ballYdir = 0;
  // Reset playfield
  int i;
  for (i = 0; i < 128; i++) {
    playfield[i] = 0;
  }
  // Re-initialize game
  gameInitialize();
  display_pong();
  quicksleep(10000);
}

// Moves all objects, and checks for collisions/gameover
void gameTick(){
  if (!(getsw() >> 3)){
    ballTick();
    setPaddleState();
    if (AIEnabled) {
      AITick();
    }
    paddleTick();
    display_pong();
  }
}

// Handles ball movement based on # of clock ticks
void ballTick(){
  ballXTickCounter++;
  ballYTickCounter++;
  // x-axis is 0, y-axis is 1 for moveBall
  if (ballXTickCounter >= ballXTickLimit) {
    moveBall(0);
    ballXTickCounter = 0;
  }
  if (ballYTickCounter >= ballYTickLimit) {
    moveBall(1);
    ballYTickCounter = 0;
  }
}

// Adjusts how many # of clock ticks before ball Moves
// Additional adjustment based on angle parameter
void setBallTicks (int angle){
  if (ballXTickLimit > 1) {
    ballXTickLimit--;
    //if (ballXTickLimit - angle > 1) {
    //  ballXTickLimit -= angle;
    //}
  }
  if (ballYTickLimit - angle > 1) {
    ballYTickLimit -= angle;
  }
}

// Handles paddle movement based on paddle states and # of clock ticks
void paddleTick(){
  paddleTicks++;
  // Moves paddles based on paddleTicks
  if (paddleTicks >= 20) {
    paddleTicks = 0;
    // Adjusts left paddle
    if (leftPaddleState == 0 || leftPaddleState == 1) {
      movePaddle(0, leftPaddleState); // paddleState 0 is up, 1 is down
      leftPaddleState = 2;
    }
    // Adjusts right paddle
    if (rightPaddleState == 0 || rightPaddleState == 1) {
      movePaddle(1, rightPaddleState); // paddleState 0 is up, 1 is down
      rightPaddleState = 2;
    }
  }
}

// Sets a state based on latest button press for paddles
void setPaddleState(){
  int buttons = getbtns();
  // Left side player
  if ((buttons >> 2) & 0x1) { // Button 3
    leftPaddleState = 0;  // State 0 is up
  }
  if ((buttons >> 3) & 0x1) { // Button 4
    leftPaddleState = 1;  // State 1 is up
  }
  // Check if both buttons are pressed for 3&4
  if ((buttons >> 2) == 3) {
    leftPaddleState = 2;
  }
  // Right side player ----
  if (!AIEnabled) {
    if (buttons & 0x1) { // Button1
      rightPaddleState = 0;
    }
    if ((buttons >> 1) & 0x1) { // Button2
      rightPaddleState = 1;
    }
    // Check if both buttons are pressed for 1&2
    if ((buttons & 0b11) == 3) {
      rightPaddleState = 2;
    }
  }
}
