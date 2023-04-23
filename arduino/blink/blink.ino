const int ledPin8 = 8;

void setup() {
  pinMode(ledPin8, OUTPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  
}

void loop() {
  digitalWrite(LED_BUILTIN, HIGH);  
  digitalWrite(ledPin8, HIGH);
   
  delay(1000);                      
  digitalWrite(LED_BUILTIN, LOW);   
  digitalWrite(ledPin8, LOW);
  
  delay(1000);                      
}