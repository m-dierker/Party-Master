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
    Serial.begin(28800);
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
                if (a % 2 == 0 && b % 2 == 0 || a % 2 == 1 && b % 2 == 1) {
                    if (c == 1) {
                        panel[a][b][c] = 255;
                    }
                } else {
                    if (c == 0){
                        panel[a][b][c] = 255;
                    }
                }

                // Orange
                // panel[a][b][0] = 255;
                // panel[a][b][1] = 128;
                // panel[a][b][2] = 0;
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
        delayMicroseconds(loopDelay);
        for (int c = 0; c < 32; c++)
        {
            ShiftPWM.SetRGB(c, panel[r][c][0], panel[r][c][1], panel[r][c][2]);
        }
    }

    if (Serial.available()) {
        char panel_data[512];
        Serial.readBytes(panel_data, 512);

        for (int r = 0; r < 16; r++) {
            for (int j = 0; j < 16; j++) {
                int fixed_r = r;
                int fixed_j = j;

                // Translate 16x16 to be 8x32
                if (r >= 8) {
                    fixed_r -= 8;
                    fixed_j += 16;
                }

                byte b1 = panel_data[2*(r*16 + j)];
                byte b2 = panel_data[2*(r*16 + j) + 1];

                unsigned char c1 = (unsigned char) (b1 + 128);
                unsigned char c2 = (unsigned char) (b2 + 128);

                unsigned int color = (c1 << 8) | c2;
                unsigned char red = ((color & 0x7C00) >> 10)  << 3 ;
                unsigned char green = ((color & 0x03E0) >> 5) << 3 ;
                unsigned char blue = (color & 0x001F) << 3 ;
                panel[fixed_r][fixed_j][0] = red;
                panel[fixed_r][fixed_j][1] = green;
                panel[fixed_r][fixed_j][2] = blue;
            }
        }

    }

    digitalWrite(13, HIGH);
}
