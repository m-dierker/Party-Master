void setup() {
  for(int a = 0; a <= 13; a++) {
    pinMode(a, OUTPUT);
  } 
}

boolean isOn = true;
unsigned long time;

void loop() {
  
  // plugging in in the afternoon
  
  time = millis();
  
  // number of milliseconds in a day
  time %= 86400000;
  
  // in the part where it should be on (15 hours from when it's plugged in)
  if (time <= 54000000) {
    isOn = true;
  } else {
    isOn = false;
  }
  
  for(int a = 0; a <= 13; a++) {
      digitalWrite(a, isOn ? HIGH : LOW);
  }
   
   
   delay(1000);
}
