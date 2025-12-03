# Verschillende deeltaken voor Cloud Computing

## Taak 1: Soap-Service in Java
Deze soap-service geeft teams terug met spelers, en deze spelers hebben allemaal een positie, een nummer en een ploeg

Mogelijke API-request:

Headers:
```
Content type = text/xml
```

Body:
```
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://service.soap.cloud_comp/">
    <soapenv:Header/>
    <soapenv:Body>
        <soap:getTeams/>
    </soapenv:Body>
</soapenv:Envelope>
```

## Taak 2: GraphQL in Python
De GraphQL service stelt een soort kalender voor, waarbij er matchen van een voetbalseizoen kunnen worden opgevraagd met alle statistieken

Voor de mogelijke query's, zie de introductie op de website

## Taak 3: Websockets in JavaScript
Deze applicatie stelt een communicatie-platform voor waarbij gebruikers een naam en een kleur van chat kunnen instellen
