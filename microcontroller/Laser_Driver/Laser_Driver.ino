#include <Servo.h>

Servo bot;
Servo top;

int state = 0;
int motor = 0;
int deg = 0;

void setup() {
  bot.attach(7);
  top.attach(6);
  
  Serial.begin(9600);
  
//  bot.write(110);
//  top.write(60);
}

void loop() {
  while(Serial.available() > 0) {
    char c = Serial.read();
    
    if(state == 0) {
      if(c != '.') {
        Serial.print("Junked character:");
        Serial.println(c);
        continue;
      }
     
      state = 1;
    } else if (state == 1) {
       motor = c - 48;
       state = 2;
    } else if (state == 2) {
       deg = (c - 65) * 10;

       state = 3;
    } else if (state == 3) {
       deg += c - 48;  
       
       if(motor == 1) {
         bot.write(deg);
       } else if (motor == 2) {
         top.write(deg);
       }
        
       delay(5);
       state = 0;
    }
        
  }
}
