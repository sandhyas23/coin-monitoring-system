#include <SoftwareSerial.h>

#include <WiFi.h>

const char* ssid     = "xxx";
const char* password = "xxx";
WiFiClient client;
const char* host = "";
const int httpPort = 80 ;
const char* streamId   = "event";
const int coinpin = 2;
const int coinpinO = 5;
const int targetcents = 1;
volatile int cents = 0;
String coinAcceptorId = "ID321";
int credits = 0;
int resetState;
SoftwareSerial swser(coinpin, coinpinO);
unsigned long lastDebounceTime = 0;
const float coinTypeArr[2] = {.25,.05};
String coinType;
#define pulseCount 275
#define coinInput swser
int pulse_count = 0;
unsigned long latestPulse = 0;
unsigned long latestAction = 0;
unsigned long tempPC;
unsigned long tempAc;
const int RESET_PIN = 8;
const int RESET_VALUE = 0;
const unsigned long DEBOUNCE_DELAY = 50;

void acceptorCount();

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

void setup()
{
    Serial.begin(115200);
    coinInput.begin(115200);
    delay(10);
    
    pinMode(RESET_PIN, INPUT_PULLUP);
    attachInterrupt(digitalPinToInterrupt(coinpin), acceptorCount, RISING);
    attachInterrupt(digitalPinToInterrupt(coinpinO), acceptorPulse, RISING);
    pulse_count = 0;

    // We start by connecting to a WiFi network

    Serial.println();
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);

    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }

    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
    Serial.print("connecting to ");
    Serial.println(host);

    // Use WiFiClient class to create TCP connections
   
    
//    if (!client.connect(host, httpPort)) {
//        Serial.println("connection failed");
//        return;
//    }
//    if (client.connected()) {
//    Serial.println("hello from ESP8266");
//  }
}

int value = 0;

void loop()
{
    delay(5000);
    ++value;

    
    bool checkResetButton();
    tempPC = latestPulse;
    tempAc = latestAction;
    if(millis()-latestPulse >= pulseCount && pulse_count>0 || pulse_count>=2){
          if(tempAc != latestAction || tempPC != latestPulse)
             return;
          if(coinTypeArr[pulse_count-1]==.25)
          {
          //Sending quarter to server 
          coinType = "quarter";
          client.println(coinType);
          }
          else if(coinTypeArr[pulse_count-1]==.05)
          {
            // Sending five cents to server 
            coinType = "five-cents";
            client.println(coinType);
          }
          else
          {
              Serial.println("I can't read other coins");
          }
        pulse_count = 0;
        Serial.println(coinType);
    }

    // We now create a URI for the request
    String url = "/event";
    url += "?coinAcceptorId:";
    url += coinAcceptorId;
    url += "&coinType=";
    url += coinType;

    Serial.print("Requesting URL: ");
    Serial.println(url);

    // This will send the request to the server
    client.print(String("GET ") + url + " HTTP/1.1\r\n" +
                 "Host: " + host + "\r\n" +
                 "Connection: close\r\n\r\n");
    unsigned long timeout = millis();
  
    while (client.available() == 0) {
        if (millis() - timeout > 5000) {
            delay(5000);
            Serial.println(">>> Client Timeout !");
            client.stop();
            return;
        }
    }

    // Read all the lines of the reply from server and print them to Serial
    while(client.available()) {
        Serial.println("connected");
        String line = client.readStringUntil('\r');
        Serial.print(line);
        
    
  }
  while(!client.available()){Serial.println("Client not connected!");}
    Serial.println();
    Serial.println("closing connection");
}

void acceptorPulse(){
      latestPulse = millis();
      latestAction = millis();
      pulse_count++;
    }

void acceptorCount(){
    digitalWrite(13, digitalRead(13));
  }
