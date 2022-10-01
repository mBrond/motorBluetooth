
#include <SoftwareSerial.h>
SoftwareSerial bluetooth(2, 3);
 
#define IN1 = 4;
#define IN2 = 5;
#define IN3 = 6;
#define IN4 = 7;
#define buzinaPin = 8 ;
char comando;
 
void setup() {
  //Define os pinos como saida
  bluetooth.begin(9600);
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);
  pinMode(IN3, OUTPUT);
  pinMode(IN4, OUTPUT);
}
 
void loop() {
  while (bluetooth.available()) {
    comando = bluetooth.read();
    if (comando == 'F') {
      frente();
    }
    else if (comando == 'B') {
      tras();
    }
    else if (comando == 'L') {
      esquerda();
    } 
    else if (comando == 'R') {
      direita();
    } else {
      parado();
    }
  }
}
 
void frente() {
  
 digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);
}
 
void tras() {

  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);
  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);

}
 
void esquerda() {
 digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);
}
 
void direita() {
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);
}
void parado() {
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4,LOW);
}
void buzina({

}
}
}
