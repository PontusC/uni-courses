/* mipslabwork.c

   This file written 2015 by F Lundevall
   Updated 2017-04-21 by F Lundevall

   This file should be changed by YOU! So you must
   add comment(s) here with your name(s) and date(s):

   This file modified 2017-04-31 by Ture Teknolog

   Modified by Pontus Curtsson, Lovisa Montelius 2018.
   Contains setup for interrupts and interrupt calls.

   For copyright and licensing, see file COPYING */

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

int debugvalue = 0;
int timeoutcount = 0;
int inMenuState = 1;

/* Interrupt Service Routine */
void user_isr( void ) {
  if ((IFS(0) >> 8) & 1) { // Timer interrupt
    IFS(0) &= ~(1 << 8); // Clear interruptflag for timer2
    timeoutcount++;
    if (timeoutcount > 99) {
      timeoutcount = 0;
      PORTE = debugvalue; // Writes debugvalue onto the LED's
    }

    if ((getsw() >> 2) & 0b01) { // If SW3 up, enter menu state
      inMenuState = 1;
    }
    if (inMenuState) {
      menuTick();
    } else {
      gameTick();
    }
  }
  if (IFS(1) & 1) { // CN-Interrupt. Used for menu buttons
    IFS(1) &= ~1;
    if (inMenuState) {
      menuSetButtonState(); // Checks input and sets button states
    }
  }
}

/* Lab-specific initialization goes here */
void labinit( void )
{
  // Config for PORTE, used for LEDS
  TRISE &= ~0xff; // Sets first 8 bits as output
  // config PORTD bits 5-11 as inputs, BTN2-4 are PORTD5-7. SW1-4 are 8-11
  TRISD |= (0x7f << 5);
  TRISF |= (0x1 << 1); // Button1 is on TRISF

  // Initialize interrupts for buttons/switches
  CNCON = 1 << 15; // Enable CN module
  IPC(6) |= 6 << 18; // Set priority
  IFS(1) &= ~1; // Clear interrupt flag
  IEC(1) |= 1; // Enable CN interrupt
  CNEN |= (0b111 << 14); // Port enable 14, 15 ,16, btns,

  // initialize timer
  T2CON = 0x70; // sets all control bits to 0 and prescale to 1:256, also turns off timer
  TMR2 = 0x0; // Clears timer
  PR2 = 1562; // Period (original 31250)
  IFS(0) &= ~(1 << 8); // Clear interruptflag for timer2
  IPC(2) = 7 << 2; // Sets priority
  T2CONSET = 1 << 15; // Starts timer
  IEC(0) |= (1 << 8); // Enable T2IE

  enable_interrupt();
  return;
}


/* This function is called repetitively from the main program */
void labwork( void ) {
  ballPrediction(); // Calculates for AI to have something to do when bored
}
