#include "DHT.h"
#include <LiquidCrystal.h>
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

#define DHTTYPE DHT11   // DHT 11 

DHT dht(7, DHTTYPE);


#define OLED_RESET     -1 // Reset pin # (or -1 if sharing Arduino reset pin)
#define SCREEN_ADDRESS 0x3C ///< See datasheet for Address; 0x3D for 128x64, 0x3C for 128x32
Adafruit_SSD1306 display(128, 64, &Wire, OLED_RESET);// 64 4 OLED rectangular

int repeat=0;

const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

void setup() {  
  Serial.begin(9600);
  dht.begin();
  lcd.begin(16, 2);

  display.begin(SSD1306_SWITCHCAPVCC, SCREEN_ADDRESS);
  
  display.clearDisplay();
  display.setTextSize(1.5);      // Normal 1:1 pixel scale
  display.setTextColor(SSD1306_WHITE); // Draw white text
  display.cp437(true); 
}

void loop() {

  
  
  int tempo=2000;

  float hum = dht.readHumidity();
  // Read temperature as Celsius
  float temp = dht.readTemperature();

  printOnDisplay(temp,hum);

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

void printOnDisplay(float temp, float hum)
{
  display.clearDisplay();
  display.setCursor(0, 0);
  display.print("Temperature:");
  display.print(temp);
  display.println("C");
  display.print("Humidity:");
  display.print(hum);
  display.println("%");
  display.display();
}

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




