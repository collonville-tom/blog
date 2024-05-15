//   A
//F     B
//   G
//E     C
//   D

  int A=4;
  int B=2;
  int C=7;
  int D=8;
  int E=9;
  int F=3;
  int G=5;
  int DP=6;

  int dig1=10;
  int dig2=11;
  int dig3=12;
  int dig4=13;

void setup() {  


  pinMode(B, OUTPUT);  
  pinMode(A, OUTPUT);
  pinMode(F, OUTPUT);
  pinMode(G, OUTPUT);
  pinMode(C, OUTPUT);
  pinMode(D, OUTPUT);
  pinMode(E, OUTPUT);

  pinMode(DP, OUTPUT);

  pinMode(dig1, OUTPUT);  
  pinMode(dig2, OUTPUT);
  pinMode(dig3, OUTPUT);
  pinMode(dig4, OUTPUT);
  digitalWrite(dig1, 0);
  digitalWrite(dig2, 0);
  digitalWrite(dig3, 0);
  digitalWrite(dig4, 0);
}

void loop() {
  digitalWrite(DP, 0); // DP
  int tempo=1;


  // convert float value into digit val
  double value=98.76;
  int regularValue=value*100;
  int dig1value=regularValue/1000;
  int dig2value=(regularValue-(dig1value*1000))/100;
  int dig3value=(regularValue-(dig1value*1000)-(dig2value*100))/10;
  int dig4value=(regularValue-(dig1value*1000)-(dig2value*100)-(dig3value*10));

  printValue(dig1value,dig1,false);
  delay(tempo);
  printValue(dig2value,dig2,true);
  delay(tempo);
  printValue(dig3value,dig3,false);
  delay(tempo);
  printValue(dig4value,dig4,false);
  delay(tempo);
}

void printValue(int val, int digit,bool point)
{
  if (point)
  {
    digitalWrite(DP, 1);
  } 
  else
  {
    digitalWrite(DP, 0);
  }
  switch(val)
  {
  case 0:
    write0(digit);
    break;
  case 1:
    write1(digit);
    break;
  case 2:
    write2(digit);
    break;
  case 3:
    write3(digit);
    break;
  case 4:
    write4(digit);
    break;
  case 5:
    write5(digit);
    break;
  case 6:
    write6(digit);
    break;
  case 7:
    write7(digit);
    break;
  case 8:
    write8(digit);
    break;
  case 9:
    write9(digit);
    break;
  default:
    break;
  }
}

void clear()
{
  digitalWrite(dig1, 1);
  digitalWrite(dig2, 1);
  digitalWrite(dig3, 1);
  digitalWrite(dig4, 1);
}

void selectDigit(int digit)
{
  clear();
  digitalWrite(digit, 0);
}

void write0(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 1); // F
  digitalWrite(G, 0); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 1); // D
  digitalWrite(E, 1); // E
}

void write9(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 1); // F
  digitalWrite(G, 1); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 1); // D
  digitalWrite(E, 0); // E
}

void write8(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 1); // F
  digitalWrite(G, 1); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 1); // D
  digitalWrite(E, 1); // E
}

void write7(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 0); // F
  digitalWrite(G, 0); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 0); // D
  digitalWrite(E, 0); // E
}

void write6(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 0); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 1); // F
  digitalWrite(G, 1); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 1); // D
  digitalWrite(E, 1); // E
}

void write5(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 0); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 1); // F
  digitalWrite(G, 1); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 1); // D
  digitalWrite(E, 0); // E
}

void write4(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 0); // A
  digitalWrite(F, 1); // F
  digitalWrite(G, 1); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 0); // D
  digitalWrite(E, 0); // E
}

void write3(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 0); // F
  digitalWrite(G, 1); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 1); // D
  digitalWrite(E, 0); // E
}

void write2(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 1); // A
  digitalWrite(F, 0); // F
  digitalWrite(G, 1); // G
  digitalWrite(C, 0); // C
  digitalWrite(D, 1); // D
  digitalWrite(E, 1); // E
}

void write1(int digit)
{
  selectDigit(digit);
  digitalWrite(B, 1); // B
  digitalWrite(A, 0); // A
  digitalWrite(F, 0); // F
  digitalWrite(G, 0); // G
  digitalWrite(C, 1); // C
  digitalWrite(D, 0); // D
  digitalWrite(E, 0); // E
}


