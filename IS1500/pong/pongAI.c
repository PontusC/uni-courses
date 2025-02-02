/*
  Containts logic and variables for AI in a Pong game
  Authors:    Pontus Curtsson
              Lovisa Montelius
*/

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

// Level of AI
int AILevel = 1;
int AIPaddle = 1;

// For delayed reaction time
int AITicks = 50;
const int AIDelay[] = {130, 70, 30};

// Current AI decision
int AIDirection = 2;
int predictedYPos = -1;

// Moves AI
void AITick(){
  if ((AILevel == 1 || AILevel == 2) && !AISleep()) {
    if (AITicks > AIDelay[AILevel - 1]) {
      AIDirection = ballAboveBelow(ballYpos);
      AITicks = 0;
    }
    rightPaddleState = AIDirection;
  }
  if (AILevel == 3 && !AISleep()) {
    if (AITicks > AIDelay[AILevel - 1]) {
      debugvalue = predictedYPos; // Writes to LEDs
      if (ballXpos > 70) { // only uses predicted value if ball is close
        AIDirection = ballAboveBelow(predictedYPos);
      }else{
        AIDirection = ballAboveBelow(ballYpos);
      }
      AITicks = 0;
    }
    rightPaddleState = AIDirection;
  }
  AITicks++;
}

// Returns 1 if ball is above paddle, 2 if in line, 0 if below
int ballAboveBelow(int ballYpos){
  if (ballYpos + 1 < *rightYpos + 3) {
    return 1;
  }
  if ((ballYpos + 1 == *rightYpos + 3) || (ballYpos + 1 == *rightYpos + 4)) {
    return 2;
  }
  if (ballYpos + 1 > *rightYpos + 4) {
    return 0;
  }
}

// Returns predicted yPos for when ball hits right paddles x-pos.
void ballPrediction(){
  if (AILevel == 3 && !AISleep()) {
    int ticksUntilHit = 0;
    int currentBallXPos = ballXpos;
    while (currentBallXPos + ballWidth - 1 + ticksUntilHit <= *rightXpos) {
      ticksUntilHit++;
    }
    ticksUntilHit *= ballXTickLimit; // Convert to game-ticks
    ticksUntilHit /= ballYTickLimit; // Convert to y-ticks
    int currentBallYPos = ballYpos;
    int direction =  ballYdir;
    while (ticksUntilHit > 0) {
      if ((currentBallYPos + ballYdir < 0) || (currentBallYPos + ballYdir > 31 - ballWidth)) {
        direction *= -1;
      }
      currentBallYPos += direction;
      ticksUntilHit--;
    }
    predictedYPos = currentBallYPos;
  }
}

/*
 Checks if AI should move or note
 Returns 1 if sleep, 0 if active
*/
int AISleep(){
  if (AILevel == 1) {
    if (ballXpos < 40 || ballXdir == -1) {
      return 1;
    }
  }
  if (AILevel == 2) {
    if (ballXpos < 30 || ballXdir == -1) {
      return 1;
    }
  }
  if (AILevel == 3) {
    if (ballXdir == -1) {
      return 1;
    }
  }
  return 0;
}
