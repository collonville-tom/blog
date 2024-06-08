#include <Adafruit_BMP085.h>
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <ESP8266WiFi.h>

#define seaLevelPressure_hPa 1013.25




#define OLED_RESET     -1 // Reset pin # (or -1 if sharing Arduino reset pin)
#define SCREEN_ADDRESS 0x3C ///< See datasheet for Address; 0x3D for 128x64, 0x3C for 128x32
Adafruit_SSD1306 display(128, 64, &Wire, OLED_RESET);// 64 4 OLED rectangular
Adafruit_BMP085 bmp;
int repeat=0;

void setup() {  
  Serial.begin(9600);
  Wire.begin(0,2); // pour l'esp-01

  display.begin(SSD1306_SWITCHCAPVCC, SCREEN_ADDRESS);
  
  display.clearDisplay();
  display.setTextSize(1.5);      // Normal 1:1 pixel scale
  display.setTextColor(SSD1306_WHITE); // Draw white text
  display.cp437(true); 
  display.setCursor(0, 0);
  display.print("Init OK");


  if (!bmp.begin()) {
    Serial.println("Could not find a valid BMP085 sensor, check wiring!");
    while (1) {}
  }
}

void loop() {

  int tempo=2000;

  float pression = bmp.readPressure();
  // Read temperature as Celsius
  float temp = bmp.readTemperature();

  // Read temperature as Celsius
  float alt = bmp.readAltitude();

  printOnDisplay(temp,pression,alt);

  delay(tempo);
}

void printOnDisplay(float temp, float pression,float altitude)
{
  display.clearDisplay();
  display.setCursor(0, 0);
  display.print("Temperature:");
  display.print(temp);
  display.println("C");
  display.print("Pression:");
  display.print(pression);
  display.println("Pa");
  display.display();
  display.print("Altitude:");
  display.print(altitude);
  display.println("m");
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




