/*
  Contains logic and variables for the menu in a Pong game.
  Stores choics and sets gamestate accordingly when finished.
  Authors:  Pontus Curtsson
            Lovisa Montelius
*/

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

// Function prototypes
void menuTick();
void gameModeMenu();
void gameDifficultyMenu();
void checkButtons();
void checkSwitchDown();

// Variables for menu state
int menuState = 1;          // State 1 is game mode selection, 2 is difficulty
int hasChanged = 1;         // 1 if needs to redraw menu, 0 if not
int selectedGameMode = 1;   // 1 is 1-player vs AI, 2 is 2-player
int maxChoices = 2;         // How many choices are available currently in menu
int selectedDifficulty = 1; // Difficulty 1-3

// States for buttons
int btnContinue = 0;
int btnSelect = 2; // 0 is down, 1 is up, 2 is nothing
int btnTicks = 0;

// Contains different strings to display
char *difficultyStrings[] = {"      EASY","     MEDIUM","      HARD"};

// Called every game tick to redraw menu if needed
void menuTick(){
  if (menuState == 1) {
    gameModeMenu();
  }
  if (menuState == 2) {
    gameDifficultyMenu();
  }
  if (menuState == 3) {
    checkSwitchDown();
  }
}

// Checks and sets button states
// Called with button interrupts
void menuSetButtonState(){
  int buttons = getbtns();
  if ((buttons >> 1) & 0x1) { // Button2
    btnContinue = 1;
  }
  if ((buttons >> 2) & 0x1) { // Button 3
    btnSelect = 0;
  }
  if ((buttons >> 3) & 0x1) { // Button 4
    btnSelect = 1;
  }
  btnTicks++;
}

// Draws first menu to select game mode
void gameModeMenu(){
  if (hasChanged) {
    display_string(0, "      PONG");
    display_string(1, "  SELECT MODE");
    display_string(2, "1-PLAYER");
    display_string(3, "2-PLAYER");
    display_update();
    display_choice(selectedGameMode + 1);
    //hasChanged = 0;
  }
  if (btnContinue) { // Button2
    menuState = 2;
    hasChanged = 1;
    maxChoices = 3;
    btnContinue = 0; // Reset buttons
    btnSelect = 2;
  }
  if (btnSelect == 0) { // Button 3
    if (selectedGameMode < maxChoices) {
      selectedGameMode++;
      hasChanged = 1;
    }
    btnSelect = 2;
  }
  if (btnSelect == 1) { // Button 4
    if (selectedGameMode > 1) {
      selectedGameMode--;
      hasChanged = 1;
    }
    btnSelect = 2;
  }
}

// Draws second menu for difficulty selection
void gameDifficultyMenu(){
  if (hasChanged) {
    display_string(0, "      PONG");
    display_string(1, "SET DIFFICULTY");
    display_string(3, difficultyStrings[selectedDifficulty - 1]);
    display_string(2, "");
    display_update();
    //hasChanged = 0;
  }
  if (btnContinue == 1) { // Button2
    menuState = 3;
    // Reset buttons
    btnContinue = 0;
    btnSelect = 2;
    // Set game difficulty
    if (selectedGameMode == 1) { // VS AI
      AIEnabled = 1;
      AILevel = selectedDifficulty;
      gameDifficulty = selectedDifficulty;
    }else{ // VS Player
      AIEnabled = 0;
      gameDifficulty = selectedDifficulty;
    }
    gameInitialize();
  }
  if (btnSelect == 0) { // Button 3
    if (selectedDifficulty < maxChoices) {
      selectedDifficulty++;
      hasChanged = 1;
    }
    btnSelect = 2;
  }
  if (btnSelect == 1) { // Button 4
    if (selectedDifficulty > 1) {
      selectedDifficulty--;
      hasChanged = 1;
    }
    btnSelect = 2;
  }
}

// Draws a prompt to switch SW3 down if it's still up
void checkSwitchDown(){
  if ((getsw() >> 2) & 0b01) {
    display_string(0, "");
    display_string(1, "  SWITCH DOWN");
    display_string(2, "   TO START");
    display_string(3, "");
    display_update();
  }else{
    menuState = 1; // Start menu state
    inMenuState = 0; // Done with menus
    maxChoices = 2;
    btnContinue = 0; // Reset buttons
    btnSelect = 2;
    hasChanged = 1;
    resetGame(); // Reset playfield of Pong
  }
}
