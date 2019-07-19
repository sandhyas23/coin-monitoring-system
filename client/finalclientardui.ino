/*
 *  This sketch sends data via HTTP GET requests to data.sparkfun.com service.
 *
 *  You need to get streamId and privateKey at data.sparkfun.com and paste them
 *  below. Or just customize this script to talk to other HTTP servers.
 *
 */

#include <WiFi.h>
# define ACTIVATED LOW

const char* ssid     = "*****";
const char* password = "******";

const char* host = "10.78.128.20";
const char* streamId   = "Jxyjr7DmxwTD5dG1D1Kv";
const char* privateKey = "gzgnB4VazkIg7GN1g1qA";
const int coinIntPin = 2;          //interruptPin 0 is digital pin 2
volatile boolean newCoin = false;                  
volatile int pulseCount;           //counts pulse for 
volatile int timeOut = 0;          //counts timeout after last coin inserted
String coinType;
int coinTypeInt;
float total = 0;
String coinAcceptorId = "ID456";
const int buttonPin = 0;     // the number of the pushbutton pin
int buttonState = 0;         // variable for reading the pushbutton status
bool reset = false;     // the number of the LED pin 

void setup()
{
    Serial.begin(115200);
    delay(10);

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

    //Serial.begin(9600);                 
    attachInterrupt(digitalPinToInterrupt(coinIntPin), coinInserted, RISING);   
    //establishContact();  
    pinMode(buttonPin, INPUT); 
    digitalWrite(buttonPin, HIGH); 
    
}
void coinInserted(){
    newCoin = true; 
    pulseCount++;
    timeOut = 0;
}



void loop()
{    if (newCoin == true){
            //Serial.println(timeOut);
            if(pulseCount == 3 || timeOut>50){           //timeOut insures that the pulses have finished before the coin Type is determined
              coinSwitch();
            }
            timeOut++;
            delay(5);
        } 
        buttonState = digitalRead(buttonPin);

  // check if the pushbutton is pressed.
  // if it is, the buttonState is HIGH:
    if (digitalRead(buttonPin) == HIGH) {     
    // since we're writing HIGH to the pin when, if it's HIGH , the button isn't pressed, as in there is no connectivity between ground and pin 2:
//so do whatever here that you want when the button is not pushed    
        reset = false;
        //Serial.println("Button unpresses"); 
    
    } 
    else if (digitalRead(buttonPin) == LOW) {
    // turn LED on, or do whatever else you want when your button is pushed
        reset = true;
        Serial.println("button pushed");
        Serial.print("connecting to ");
        Serial.println(host);

    // Use WiFiClient class to create TCP connections
        WiFiClient client;
        const int httpPort = 8080;
        if (!client.connect(host, httpPort)) {
            Serial.println("connection failed");
            return;
        }
        String url1 = "/";
        url1 += "reset";
        url1 += "?coinAcceptorId=";
        url1 += coinAcceptorId;
        url1 += "&reset=";
        url1 += reset;

        Serial.print("Requesting URL: ");
        Serial.println(url1);

    // This will send the request to the server
        client.print(String("GET ") + url1 + " HTTP/1.1\r\n" +
                 "Host: " + host + "\r\n" +
                 "Connection: close\r\n\r\n");
             
        unsigned long timeout = millis();
        while (client.available() == 0) {
            if (millis() - timeout > 5000) {
                Serial.println(">>> Client Timeout !");
                client.stop();
                return;
            }
        }

    // Read all the lines of the reply from server and print them to Serial
        while(client.available()) {
            String line = client.readStringUntil('\r');
            Serial.print(line);
        }

        Serial.println();
        Serial.println("closing connection");
    }
    else{}

    
}

void coinSwitch(){
    switch (pulseCount) {                                //pulseCount can be anything from 1 to 50, programmed in the Coin Acceptor. There can be up to 4 cases. 
        case 1:
            coinType = "quarter";
            //Serial.println("Coin Type: " + coinType);
            coinTypeInt = 1;
            total = total + 0.25;
            //Serial.println(coinTypeInt,DEC); 
            //Serial.println("total:");
            //Serial.print(total);
            //Serial.println();           
            pulseCount = 0;
            newCoin = false;
            break;
        case 2:
            coinType = "five-cents";
            //Serial.println("Coin Type: " + coinType);
            coinTypeInt = 2;
            total = total + 0.05;
            //Serial.println(coinTypeInt,DEC); 
            //Serial.println("total:");
            //Serial.print(total);
            //Serial.println();           
            pulseCount = 0;
            newCoin = false;
            break;
        case 3:
            coinType = "none";
            //Serial.println("Coin Type: " + coinType);
            coinTypeInt = 3;
            //Serial.println(coinTypeInt,DEC);
            pulseCount = 0;
            newCoin = false;
            break; 
    }
    //delay(5000);
    

    Serial.print("connecting to ");
    Serial.println(host);

    // Use WiFiClient class to create TCP connections
    WiFiClient client;
    const int httpPort = 8080;
    if (!client.connect(host, httpPort)) {
        Serial.println("connection failed");
        return;
    }

    // We now create a URI for the request
    String url = "/";
    url += "event";
    url += "?coinAcceptorId=";
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
        if (millis() - timeout > 10000) {
            Serial.println(">>> Client Timeout !");
            client.stop();
            return;
        }
    }

    // Read all the lines of the reply from server and print them to Serial
    while(client.available()) {
        String line = client.readStringUntil('\r');
        Serial.print(line);
    }

    Serial.println();
    Serial.println("closing connection");
}
