/*
  Contains logic and variables for a ball in a Pong game
  Authors:  Pontus Curtsson
            Lovisa Montelius
*/

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

// Function prototypes
void moveBall(int axis);
void eraseBall();
void shiftBall(int axis);
void drawBall();
int wallCollision();
int paddleCollision();
int outOfBounds();

// Position for ball
int ballXpos = 60;
int ballYpos = 12;

// x/yDirection controls ball direction
int      ballXdir = -1;
int      ballYdir = 1;

// Controls ball size
const int ballWidth = 3;

/*
  Handles ball movement
  axis = 0 means movement on x-axis
  axis = 1 means movement on y-axis
*/
void moveBall(int axis){
  eraseBall();
  shiftBall(axis);
  drawBall();
}

// Removes ball from playfield
void eraseBall(){
  int i;
  int column;
  for (i = 0; i < ballWidth; i++) {
    column = playfield[ballXpos + i];
    column = (column & ~(0b111 << ballYpos));
    playfield[ballXpos + i] = column;
  }
}

/*
  Checks ball collision and adjusts its speed
  Checks gameover and resets game
  Axis 0 is movement on x-axis, y is 1.
*/
void shiftBall(int axis){
  // Check ball with paddle collision
  if (axis == 0) {
    if (paddleCollision()) {
      // Calculates angle change and makes ball faster
      if (ballXpos > 90) {
        setBallTicks(changeAngle(rightYpos));
      }else{
        setBallTicks(changeAngle(leftYpos));
      }
      // Makes ball move faster
      ballXdir = ballXdir * -1;
    }
    if (gameover()) {
      resetGame();
      return;
    }
  }
  if (wallCollision()) {
    ballYdir = ballYdir * -1;
  }
  // Moves ball
  if (axis == 0) {
    ballXpos += ballXdir;
  }
  if (axis == 1) {
    ballYpos += ballYdir;
  }
}

// Draws ball onto playfield
void drawBall(){
  int i;
  int column;
  for (i = 0; i < ballWidth; i++) {
    column = playfield[ballXpos + i];
    column = (column | (0b111 << ballYpos));
    playfield[ballXpos + i] = column;
  }
}

// Checks ball collision with upper and lower walls
int wallCollision(){
  if ((ballYpos + ballYdir < 0) || (ballYpos + ballYdir > 31 - ballWidth)) {
    return 1;
  }
  return 0;
}

// Checks if ball collides with a paddle
int paddleCollision(){
  int futureBallPos = ballXpos + ballXdir;
  int adjustedBallPos = ballYpos + ballWidth - 1;
  // Check left paddle x-axis collision
  if (futureBallPos <= *leftXpos + paddleWidth - 1) {
    // Check left paddle y-axis
    if ((ballYpos <= (*leftYpos + paddleHeight - 1)) && (adjustedBallPos >= *leftYpos)) {
      return 1;
    }
  }
  // Check right paddle x-axis
  if (futureBallPos + ballWidth - 1 >= *rightXpos) {
    // Check right paddle y-axis
    if ((ballYpos <= (*rightYpos + paddleHeight - 1)) && (adjustedBallPos >= *rightYpos)) {
      return 1;
    }
  }
  return 0;
}

// Checks after paddle colision, by how much to adjust angle
int changeAngle(int* paddleYpos){
  int angle = 3;
  int ballMiddle = ballYpos + 1;
  // First case: ball middle above paddle middle
  if (ballMiddle < *paddleYpos + 3) {
    return angle;
  }
  // Second case: ball middle below paddle middle
  if (ballMiddle > *paddleYpos + 5) {
    return angle * -1;
  }
  return 0;
}

// Checks if ball is going out-of-bounds
int outOfBounds(){
  int futureBallPos = ballXpos + ballXdir;
  // Left side
  if (futureBallPos < leftBorder) {
    return 1;
  }
  // Right side
  if (futureBallPos + ballWidth - 1 > rightBorder) {
    return 1;
  }
  return 0;
}
