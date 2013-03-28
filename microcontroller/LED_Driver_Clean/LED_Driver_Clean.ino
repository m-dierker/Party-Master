// The row pins control the 74HC595N
int rowData = 3;
int rowClock = 4;
int rowLatch = 2;

int r = 0;
int j = 0;
int charRead = 0;
char c1;

//int serialTimeout = 5000;

// Clock and data pins are pins from the hardware SPI, you cannot choose them yourself if you use the hardware SPI.
// Data pin is MOSI (Uno and earlier: 11, Leonardo: ICSP 4, Mega: 51, Teensy 2.0: 2, Teensy 2.0++: 22)
// Clock pin is SCK (Uno and earlier: 13, Leonardo: ICSP 3, Mega: 52, Teensy 2.0: 1, Teensy 2.0++: 21)

// You can choose the latch pin yourself.
const int ShiftPWM_latchPin=8;

// ** uncomment this part to NOT use the SPI port and change the pin numbers. This is 2.5x slower **
// #define SHIFTPWM_NOSPI
// const int ShiftPWM_dataPin = 11;
// const int ShiftPWM_clockPin = 13;


const bool ShiftPWM_invertOutputs = false;
const bool ShiftPWM_balanceLoad = false;

#include <ShiftPWM.h>   // include ShiftPWM.h after setting the pins!

unsigned char maxBrightness = 31;
unsigned char pwmFrequency = 255;
int loopDelay = 12;
int numRegisters = 12;



void setRow(int row)
{
  digitalWrite(rowClock, LOW);
  digitalWrite(rowLatch, LOW);
  shiftOut(rowData, rowClock, MSBFIRST, 1 << row);
  digitalWrite(rowLatch, HIGH);
}

unsigned char panel[8][32][3];

void setup()
{
  Serial.begin(9600);

  pinMode(rowData, OUTPUT);
  pinMode(rowClock, OUTPUT);
  pinMode(rowLatch, OUTPUT);

  pinMode(13, OUTPUT);
  digitalWrite(13, LOW);

  // Sets the number of 8-bit registers that are used.
  ShiftPWM.SetAmountOfRegisters(numRegisters);

  // If your LED's are connected like this: RRRRGGGGBBBBRRRRGGGGBBBB, use SetPinGrouping(4).
  ShiftPWM.SetPinGrouping(8);

  ShiftPWM.Start(pwmFrequency,maxBrightness);
//  loopDelay = pwmFrequency/20;
//loopDelay = 12;

  for (int a = 0; a < 8; a++)
  {
    for (int b = 0; b < 32; b++)
    {
      for (int c = 0; c < 3; c++)
      {
//        if (a % 2 == 0 && b % 2 == 0 || a % 2 == 1 && b % 2 == 1) {
//          if (c == 1) {
//            panel[a][b][c] = 255;
//          }
//        } else {
//          if (c == 0){
//            panel[a][b][c] = 255;
//          }
//        }
panel[a][b][0] = 255;
panel[a][b][1] = 128;
panel[a][b][2] = 0;
      }
    }
  }
}

void loop()
{
  for (int i = 0; i < 8; i++)
  {
     ShiftPWM.SetAll(0);
     setRow(i);
     delayMicroseconds(loopDelay);
//delay(1);
     //ShiftPWM.SetAllRGB(0, 0, 255);
     for (int j = 0; j < 32; j++)
     {
       //ShiftPWM.SetAll(255);

       //checkerboard
//       if ((((i >> 1) << 1 == i) && (j >> 1) << 1 == j) || (((i >> 1) << 1 != i) && (j >> 1) << 1 != j))
//         ShiftPWM.SetRGB(j, 255, 0, 0);
//       else
//         ShiftPWM.SetRGB(j, 0, 255, 0);


       //Half and half
       //if (i < 4) ShiftPWM.SetRGB(j, 255, 0, 0);
       //else ShiftPWM.SetRGB(j, 0, 255, 0);

       //Alternating rows
//       if ( (i >> 1) << 1 == i) ShiftPWM.SetRGB(j, 255, 0, 0);
//       else ShiftPWM.SetRGB(j, 0, 255, 0);

       //Gradient
       //ShiftPWM.SetRGB(j, 255/8*j, 255/8*(8-j), 0);

       //Load pattern(random)
       ShiftPWM.SetRGB(j, panel[i][j][0], panel[i][j][1], panel[i][j][2]);
     }
//     delay(10);
   }

if (Serial.available()) {
//    char pos = (char) Serial.read();
//    r = pos & 0xE0;
//    j = pos & 0x1F;
//    delay(15);

//    char c1 = (char) Serial.read();
//    delay(15);
//    char c2 = (char) Serial.read();
//    delay(15);
//    int c = (c1 << 8) | c2;
//    unsigned char red = ((c & 0x7C00) >> 10) * 8;
//    unsigned char green = ((c & 0x03E0) >> 5) * 8;
//    unsigned char blue = (c & 0x001F) * 8;
//    panel[r][j][0] = red;
//    panel[r][j][1] = green;
//    panel[r][j][2] = blue; uu
//   long serialStart = millis();

   Serial.setTimeout(99999999);
   char panel_data[512];

   Serial.readBytes(panel_data, 512);

   for (int r = 0; r < 16; r++) {
     for (int j = 0; j < 16; j++) {

       int actual_r = r;
       int actual_j = j;

       // Quadrant stuff to translate 16x16 to be 8 x 32
       if (r >= 8) {
         actual_r -= 8;
         actual_j += 16;
       }

       byte b1 = panel_data[2*(r*16 + j)];
       byte b2 = panel_data[2*(r*16 + j) + 1];

       unsigned char c1 = (unsigned char) (b1 + 128);
//       delay(20);

       unsigned char c2 = (unsigned char) (b2 + 128);
//       Serial.println(c2);
//       delay(20);

//       c1 = 124;
//       c2 = 128;

       unsigned int c = (c1 << 8) | c2;
//       Serial.println(c);
       unsigned char red = ((c & 0x7C00) >> 10)  << 3 ;
       unsigned char green = ((c & 0x03E0) >> 5) << 3 ;
       unsigned char blue = (c & 0x001F) << 3 ;
       panel[actual_r][actual_j][0] = red;
       panel[actual_r][actual_j][1] = green;
       panel[actual_r][actual_j][2] = blue;
     }
   }

 }

         digitalWrite(13, HIGH);
}


