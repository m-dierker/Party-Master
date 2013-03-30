// The row pins control the 74HC595N
int rowData = 3;
int rowClock = 4;
int rowLatch = 2;

int charRead = 0;
char c1;

int serialStep = 0;
int serialRow = -1;
int serialCol = -1;
byte serialColor1;
byte serialColor2;

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
int loopDelay = 0;
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
    Serial.begin(38400);
    Serial.setTimeout(99999999);

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

    for (int a = 0; a < 8; a++)
    {
        for (int b = 0; b < 32; b++)
        {
            for (int c = 0; c < 3; c++)
            {
                // Checkerboard
                // if (a % 2 == 0 && b % 2 == 0 || a % 2 == 1 && b % 2 == 1) {
                //     if (c == 0) {
                //         panel[a][b][c] = 255;
                //     }
                // } else {
                //     if (c == 1){
                //         panel[a][b][c] = 255;
                //     }
                // }
//                if (a % 2 == 0){
//                    if (c == 1){
//                        panel[a][b][c] = 255;
//                    }
//                } else if (c == 0) {
//                    panel[a][b][c] = 255;
//                }

                // Orange
//                panel[a][b][0] = 255;
//                panel[a][b][1] = 128;
//                panel[a][b][2] = 0;
            }
        }
    }
}

void loop()
{
    for (int r = 0; r < 8; r++)
    {
        ShiftPWM.SetAll(0);
        setRow(r);
//        delayMicroseconds(loopDelay);
        for (int c = 0; c < 32; c++)
        {
            ShiftPWM.SetRGB(c, panel[r][c][0], panel[r][c][1], panel[r][c][2]);
        }
    }

    while (Serial.available()) {

        // Send <sync> <pos> <color 1> <color 2>
        byte b = Serial.read();

        // Sync byte
        // It's possible this will screw me over, but we can't transmit position zero if we don't add the serialStep check here
        if (b == 0 && serialStep != 1) {
            // We're now ready to get pos, color 1, and color 2
            serialStep = 1;
        } else {
            // We have some sort of data, so let's figure out what it is.

            if (serialStep == 1) {
                // We should be reading the pos right now, so b is the pos
                int r = (b & 0xF0) >> 4;
                int c = (b & 0x0F);

                // Translate 16x16 to be 8x32, which is how the panel is in memory
                if (r >= 8) {
                    r -= 8;
                    c += 16;
                }

                // Set the global pos data
                serialRow = r;
                serialCol = c;

                // Set that we should read the first color byte next
                serialStep = 2;
            } else if (serialStep == 2) {
                // We're reading the first color byte
                serialColor1 = b;

                // Set that we should read the second color byte next
                serialStep = 3;
            } else if (serialStep == 3) {
                // We're reading the second color byte
                serialColor2 = b;

                // Now that we have all the information, we can actually set the color

                unsigned char c1 = (unsigned char) (serialColor1 + 128 );
                unsigned char c2 = (unsigned char) (serialColor2 + 128 );

                unsigned int color = (c1 << 8) | c2;
                unsigned char red = ((color & 0x7C00) >> 10)  << 3 ;
                unsigned char green = ((color & 0x03E0) >> 5) << 3 ;
                unsigned char blue = (color & 0x001F) << 3 ;

                // Special case for the sync bit
                if (blue == 8) {
                  blue = 0;
                }

                panel[serialRow][serialCol][0] = red;
                panel[serialRow][serialCol][1] = green;
                panel[serialRow][serialCol][2] = blue;

                // Reset the serial step so if we more data nothing happens
                serialStep = 0;

                // Reset the row and column so if some random data were to come through somehow, nothing would affect the old LED
                serialRow = -1;
                serialCol = -1;
            }
        }
    }
}

