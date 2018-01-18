## Beschreibung

Das Beispiel soll die Orchestrierung von als Lambda implementierten 
Geschäftsfunktionen durch einen Geschäftsprozess verdeutlichen, wobei
weder Prozess noch Lambda sich gegenseitig kennen und leiglich einen 
Contract über die zu übertragenden Datenformate erfüllen.

Das Beispiel besteht aus zwei Komponenten:
1. Camunda Spring Boot Anwendung
2. AWS Lambda Function

Die Anwendung enthält einen Prozess, der in einem asynchronen Service Task
über ein SNS Topic eine Nachricht ("Hello World") sendet 
und dann auf den Eingang einer anderen Nachricht auf einem zweiten 
SNS Topic wartet.

Die Function ist auf das erste Topic subscribed und sendet die erhaltene 
Nachricht unverändert über das zweite Topic zurück.

## Warum überhaupt Orchestrierung?

Um Funktionen oder auch Microservices zu einer fachlich sinnvollen Zusammenarbeit zu bewegen gibt es zwei Möglichkeiten:
* Orchestrierung
* Choreographie 

Bei der Choreographie gibt es keine zentrale, steuernde Einheit. Stattdessen müssen alle Beteiligten
selber wissen, wie sie in den Geschäftsprozess eingebettet sind.
Ändert sich nun die Reihenfolge, müssen mehrere Komponenten angepasst werden.
Sind die Komponenten Teil von mehreren Geschäftsprozessen, müssen sie ihre Position 
innerhalb jedes dieser Prozesse wissen und sie müssen bei jeder Ausführung wissen, 
in welchem Prozess sie sich befinden.
Die Komplexität der einzelnen Komponenten ist damit vergleichsweise hoch.

Bei der Orchestrierug gibt es eine zentral Stelle, die alle Prozesse und alle Beteiligten kennt.
Damit ist sie ein Single Point of Failure. Gleichzeitig erfordern Änderungen in der Reihenfolge 
nur Änderungen an einer Komponente und die orchestrierten Komponenten sind frei von Kontextwissen.  
Die Komplexität der einzelnen Komponenten ist damit vergleichsweise niedrig.

Man muss für sich selber bewerten, welche Eigenschaft das höhere Risiko darstellt:
* Hohe Komplexität in Entwicklung und Wartung
* Ein bekannter Single Point of Failure im Betrieb  

## Warum ein Asynchroner Service Task? 

Ziele sind:
* untereinander unbekannte Komponenten zu verbinden
* die Aktivität der Komponenten gering zu halten, um die (kostenpflichtigen)
Resourcen zu schonen 
* Request und Response des Funktionsaufruf als ein unteilbar zusammenhängendes 
Element zu modellieren      

Grundsätzlich gibt es drei Möglichkeiten die Lambda Function asynchron 
aus dem Prozess aufzurufen:
1. Send + Receive Message Events
2. External Task
3. [asynchron implementierter Service Task](https://github.com/camunda/camunda-bpm-examples/tree/master/servicetask/service-invocation-asynchronous)

Der __External Task__ erfordert auf Seiten der Function Wissen über die 
Anwendung und einen Worker Mechanismus, der regelmäßig auf abzuholende Arbeit
wartet. Das steht im Widerspruch zu dem Ziel, die Aktivität gering zu halten.

__Send + Receive Message Events__ machen die asynchrone Natur sehr deutlich 
und wären in jedem Fall keine schlechte Wahl. Gleichzeitig wird der fachlich 
atomare Charakter des Funktionsaufrufes versteckt.   
Perspektivisch möchte man zudem auf das Nicht-Eintreffen der Rückantwort 
mit einem Timeout reagieren können, was ein Event-based Gateway mit 
zusätzlichem Timer Event erfordert.

Mit einem __asynchronen Service Task__ lassen sich die gewünschten Ziele erreichen. 

## Lambda Function

```
'use strict';

console.log('Loading function');

var AWS = require('aws-sdk');  
AWS.config.region = 'eu-central-1';

exports.handler = (event, context) => {
    console.log('Received event:', JSON.stringify(event, null, 2));

    var snsMessage = event.Records[0].Sns;
    console.log('Subject =', snsMessage.Subject);
    console.log('Message =', snsMessage.Message);
    var response = snsMessage.Subject + ":" + snsMessage.Message

    var sns = new AWS.SNS();
    sns.publish({
        Subject: snsMessage.Subject,
        Message: response,
        TopicArn: 'arn:aws:sns:eu-central-1:831064628565:cam-echo-response'
    }, function(err, data) {
        if (err) {
            console.log(err.stack);
            return;
        }
        console.log('push sent');
        console.log(data);
        context.done(null, 'Function Finished!');  
    });
};
```

## AWS Anforderungen

### EC2 Instance

t2.micro

### Benötigte Rechte

Die Anwendung benötigt folgende Rechte:

```
SNS:Publish
SNS:Subscribe
SNS:ConfirmSubscription 
SNS:Unsubscribe
```

Spring Cloud AWS Autodiscovery benötigt zusätzlich diese:
```
autoscaling:DescribeAutoScalingInstances 
cloudformation:Describe*
```

## Configuration

### Umgebungsvariablen

SERVER_PORT = 5000

### application.yaml

```
serverless-bpm:
  aws:
    publishTopicArn: arn:aws:sns:eu-central-1:831064628565:cam-test
    subscribeTopicArn: arn:aws:sns:eu-central-1:831064628565:cam-echo-response
```

