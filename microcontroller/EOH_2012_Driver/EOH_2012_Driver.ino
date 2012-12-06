void setup() {
  for(int a = 0; a <= 13; a++) {
    pinMode(a, OUTPUT);
    digitalWrite(a, HIGH);
  } 

  Serial.begin(9600);
}


void loop() {
   while(Serial.available()) {
     int strand = (Serial.read() - '0');
     while(!Serial.available()) {}
     char on = Serial.read();
     
     if (on == '1') {
       digitalWrite(strand, HIGH);
     } else {
       digitalWrite(strand, LOW);
     }
   } 
}
