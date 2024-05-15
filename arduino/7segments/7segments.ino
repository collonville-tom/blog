

void setup() {               
  pinMode(2, OUTPUT);  // pin 
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(8, OUTPUT);
  pinMode(9, OUTPUT);
}

void loop() {
  int temp=200;
  write0();
  delay(temp);
  write1();
  delay(temp);
  write2();
  delay(temp);
  write3();
  delay(temp);
  write4();
  delay(temp);
  write5();
  delay(temp);
  write6();
  delay(temp);
  write7();
  delay(temp);
  write8();
  delay(temp);
  write9();
  delay(temp);
}
//   A
//F     B
//   G
//E     C
//   D
void write0()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 1); // F
  digitalWrite(5, 0); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 1); // D
  digitalWrite(9, 1); // E
}

void write9()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 1); // F
  digitalWrite(5, 1); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 1); // D
  digitalWrite(9, 0); // E
}

void write8()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 1); // F
  digitalWrite(5, 1); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 1); // D
  digitalWrite(9, 1); // E
}

void write7()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 0); // F
  digitalWrite(5, 0); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 0); // D
  digitalWrite(9, 0); // E
}

void write6()
{
  digitalWrite(2, 0); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 1); // F
  digitalWrite(5, 1); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 1); // D
  digitalWrite(9, 1); // E
}

void write5()
{
  digitalWrite(2, 0); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 1); // F
  digitalWrite(5, 1); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 1); // D
  digitalWrite(9, 0); // E
}

void write4()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 0); // A
  digitalWrite(4, 1); // F
  digitalWrite(5, 1); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 0); // D
  digitalWrite(9, 0); // E
}

void write3()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 0); // F
  digitalWrite(5, 1); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 1); // D
  digitalWrite(9, 0); // E
}

void write2()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 1); // A
  digitalWrite(4, 0); // F
  digitalWrite(5, 1); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 0); // C
  digitalWrite(8, 1); // D
  digitalWrite(9, 1); // E
}

void write1()
{
  digitalWrite(2, 1); // B
  digitalWrite(3, 0); // A
  digitalWrite(4, 0); // F
  digitalWrite(5, 0); // G
  digitalWrite(6, 0); // DP
  digitalWrite(7, 1); // C
  digitalWrite(8, 0); // D
  digitalWrite(9, 0); // E
}


