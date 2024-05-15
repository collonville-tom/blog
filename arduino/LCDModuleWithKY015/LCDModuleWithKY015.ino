#include "DHT.h"
#include <LiquidCrystal.h>


#define DHTTYPE DHT11   // DHT 11 

DHT dht(7, DHTTYPE);




const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

void setup() {  
  Serial.begin(9600);
  dht.begin();
  lcd.begin(16, 2);
}

void loop() {

  int tempo=10;

  float hum = dht.readHumidity();
  // Read temperature as Celsius
  float temp = dht.readTemperature();

  printSerial(100,hum,"Humidity:","%");

  lcd.setCursor(0, 0);
  lcd.print("Humidity:");
  lcd.print(hum);
  lcd.print("%");

  printSerial(100,temp,"Temperature:","C");
  lcd.setCursor(0, 1);
  lcd.print("Temp:");
  lcd.print(temp);
  lcd.print("C");


  delay(tempo);
}

int repeat=0;

void printSerial(int value,float value2display, String messsage,String unity)
{
  if(value<repeat)
  {
    Serial.print(messsage);
    Serial.print(value2display);
    Serial.print(" ");
    Serial.print(unity);
    Serial.print("\n");
    repeat=0;
  }
  repeat++;
}




