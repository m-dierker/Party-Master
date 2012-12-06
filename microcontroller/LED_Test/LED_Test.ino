int led = 13; // mandatory initialization
char message;

void setup(){
        pinMode(led, OUTPUT);
        Serial.begin(9600);
}

void loop(){
        while(Serial.available() > 0){
                message = Serial.read();
                if ( message == '1')  {
                  digitalWrite(led, HIGH);
                  Serial.print("LED on");

                } else if ( message == '0') {
                  digitalWrite(led, LOW);
                  Serial.print("LED off");
                }
        }
}
