/*
  Containts functions to get button/switch states
  Authors:    Pontus Curtsson
              Lovisa Montelius
*/

#include <stdint.h>
#include <pic32mx.h>
#include "mipslab.h"

// Gets switch status
int getsw(void){
  int switches = (PORTD >> 8) & 0xf; // Gets value of PORTD, bitshifts 8 to the right and removes all but 4 first bits.
  return switches;
}

// Gets buttonstatus for buttons 2-4
int getbtns(void){
  int buttons = (PORTD >> 5) & 0b111; // Gets value of PORTD, bitshifts 5 to the right and removes all but first 3 bits.
  int button1 = (PORTF >> 1) & 0x1;
  return (buttons << 1) + button1;
}
