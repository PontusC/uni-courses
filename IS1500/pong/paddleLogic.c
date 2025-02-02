/*
  Containts logic and variables for paddles in a Pong game
  Authors:    Pontus Curtsson
              Lovisa Montelius
*/

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

// Function prototypes
void movePaddle(int player, int direction);
void erasePaddle(int* xPos, int* yPos);
void shiftPaddle(int* yPos, int direction);
void drawPaddle(int* xPos, int* yPos);

// Positions for paddles
int leftX = 0;
int leftY = 10;
int rightX = 126;
int rightY = 10;

int* leftXpos = &leftX;
int* leftYpos = &leftY;
int* rightXpos = &rightX;
int* rightYpos = &rightY;

// Paddle dimesions
int paddleWidth = 2;
int paddleBinary = 0xff;
int paddleHeight = 8;

/*
 Handles moving of one specified paddle, and redrawing it
 player 0 is left paddle, 1 is right paddle
 direction 0 is down, 1 is up
*/
void movePaddle(int player, int direction){
  int* xPos;
  int* yPos;
  if (player == 0) {
    xPos = leftXpos;
    yPos = leftYpos;
  }
  if (player == 1) {
    xPos = rightXpos;
    yPos = rightYpos;
  }
  erasePaddle(xPos,yPos);
  shiftPaddle(yPos, direction);
  drawPaddle(xPos, yPos);
}

// Erases given paddle positions from the playfield
void erasePaddle(int* xPos, int* yPos){
  int i;
  int column, currentPosition;
  for (i = 0; i < paddleWidth; i++) {
    column = playfield[*xPos + i]; // fetch column containing paddle
    currentPosition = ~(paddleBinary << *yPos); // fetch current y position
    column &= currentPosition; // Changes all paddle values from 1 to 0
    playfield[*xPos + i] = column;
  }
}

// Shifts given paddle positions on the playfield
void shiftPaddle(int* yPos, int direction){
  // Adjusts paddle position based on given direciton. 0 is down, 1 is up
  // Cast paddleBinary to int for c comparison issues
  if ((direction == 0) && (*yPos < 31 - paddleHeight + 1)) {
    (*yPos)++; // Move down
  }
  if ((direction == 1) && (*yPos > 0)){
    (*yPos)--; // Move up
  }
}

// Draws given paddle positions on the playfield
void drawPaddle(int* xPos, int* yPos){
  int i;
  int column;
  for (i = 0; i < paddleWidth; i++) {
    column = playfield[*xPos + i];
    column = (column | (paddleBinary << *yPos));
    playfield[*xPos + i] = column;
  }
}