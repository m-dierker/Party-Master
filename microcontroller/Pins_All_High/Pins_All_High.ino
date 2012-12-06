void setup() {
  for(int a = 0; a <= 13; a++) {
    pinMode(a, OUTPUT);
  } 
}


void loop() {
   for(int a = 0; a <= 13; a++) {
     digitalWrite(a, HIGH);
   } 
}
