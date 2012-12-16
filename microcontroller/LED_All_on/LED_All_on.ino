//Pin connected to ST_CP of 74HC595
int latchPin = 8;
//Pin connected to SH_CP of 74HC595
int clockPin = 12;
////Pin connected to DS of 74HC595
int dataPin = 11;

void setup() {
  //set pins to output so you can control the shift register
  pinMode(latchPin, OUTPUT);
  pinMode(clockPin, OUTPUT);
  pinMode(dataPin, OUTPUT);
}

void loop() {
 int num = 0x11111111;
 
  // take the latchPin low so 
  // the LEDs don't change while you're sending in bits:
  digitalWrite(latchPin, LOW);
  
  // shift out the bits:
  shiftOut(dataPin, clockPin, MSBFIRST, num);  

  //take the latch pin high so the LEDs will light up:
  digitalWrite(latchPin, HIGH);
  // pause before next value:
  delay(500);
}
