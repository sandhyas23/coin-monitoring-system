#include <SoftwareSerial.h>
const int coinpin = 2;
const int coinpinO = 5;
const int targetcents = 1;
volatile int cents = 0;
String coinAcceptorId = "Coin Acceptor 1";
int credits = 0;
int resetState;

unsigned long lastDebounceTime = 0;
const float coinTypeArr[2] = {.25,.05};
String coinType;
#define pulseCount 275
int pulse_count = 0;
unsigned long latestPulse = 0;
unsigned long latestAction = 0;
unsigned long tempPC;
unsigned long tempAc;
const int RESET_PIN = 8;
const int RESET_VALUE = 0;
const unsigned long DEBOUNCE_DELAY = 50;

bool checkResetButton() {
  resetState = digitalRead(RESET_PIN);
//  if ((millis() - lastDebounceTime) > DEBOUNCE_DELAY) {
  if (resetState == RESET_VALUE ) {
        Serial.println("RESET");
        return true;
}
  else
    return false;
  
//  }
  }
void setup() {
  // put your setup code here, to run once:

  Serial.begin(115200);
  pinMode(RESET_PIN, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(coinpin), acceptorCount, RISING);
  attachInterrupt(digitalPinToInterrupt(coinpinO), acceptorPulse, RISING);
  pulse_count = 0;

}

void loop() {
  bool checkResetButton();
  tempPC = latestPulse;
  tempAc = latestAction;
  if(millis()-latestPulse >= pulseCount && pulse_count>0 || pulse_count>=2){
    if(tempAc != latestAction || tempPC != latestPulse) return;
    if(coinTypeArr[pulse_count-1]==.25){
//        Serial.println("Quarter");
        coinType = "Quarter";
        }
        else if(coinTypeArr[pulse_count-1]==.05)
        {
//          Serial.println("Five cents");
          coinType = "five-cents";
          }
          else
          {
            Serial.println("I can't read other coins");
            }
    Serial.println(coinType);
    pulse_count = 0;
  }

  delay(5000);
  
}



void acceptorPulse(){
      latestPulse = millis();
      latestAction = millis();
      pulse_count++;
    }

void acceptorCount(){
    digitalWrite(13, digitalRead(13));
  }
