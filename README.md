# Cloud Computing Projecten

Dit repository bevat verschillende microservices en applicaties die diverse communicatieprotocollen en architecturen demonstreren. Hieronder volgt een overzicht van elke service.

## 1. GraphQL Service
**Map:** [`GraphQL/`](GraphQL/)

*   **Wat het doet:**
    Deze service draait een Python Flask-applicatie die een GraphQL API aanbiedt. Via een webinterface (`index.html`) kunnen gebruikers zoeken naar specifieke Premier League teams. De backend verwerkt queries om teamstatistieken, spelersinformatie en wedstrijdresultaten op te halen.
*   **Data:**
    De service werkt met statische **CSV-bestanden** die historische voetbaldata bevatten:
    *   `matches.csv`: Wedstrijdresultaten en scores.
    *   `players.csv`: Spelersinformatie (naam, leeftijd, positie, stats).
    *   `teams.csv`: Teamdetails.
    *   `league_stats.csv`: Algemene competitiestatistieken.

## 2. MQTT Service
**Map:** [`MQTT/`](MQTT/)

*   **Wat het doet:**
    Dit project demonstreert een Publish-Subscribe architectuur met twee componenten:
    *   **Sender:** Een simulator die sensor-data genereert en publiceert naar een MQTT-broker (HiveMQ).
    *   **Receiver:** Een client die abonneert op topics, de binnenkomende berichten verwerkt en deze visualiseert in een live grafiek (`plots/live_grafiek.png`).
*   **Data:**
    De service werkt met **gesimuleerde real-time prestatiedata** van atleten, verstuurd als JSON-berichten. De metrics omvatten:
    *   Hartslag (bpm)
    *   Systolische bloeddruk (mmHg)
    *   Lactaat waardes (mmol/L)
    *   Zuurstofopname (mL/kg/min)

## 3. Websockets Service
**Map:** [`Websockets/`](Websockets/)

*   **Wat het doet:**
    Een Node.js server die een real-time chat-applicatie faciliteert. De server houdt een open verbinding met clients (browsers), waardoor gebruikers direct berichten naar elkaar kunnen sturen (zowel publiek als privé) en hun gebruikersnaam of kleur kunnen aanpassen.
*   **Data:**
    De service verwerkt **real-time JSON-berichten** die vluchtig zijn (niet permanent opgeslagen in een database):
    *   Chatberichten (content, afzender, doel).
    *   Gebruikersmetadata (gebruikersnaam, kleur, ID).
    *   Statusupdates (nieuwe verbindingen, verbroken verbindingen).

## 4. SOAP Service
**Map:** [`soap-service/`](soap-service/)

*   **Wat het doet:**
    Een Java-applicatie die gebruikmaakt van Apache CXF om een SOAP (Simple Object Access Protocol) web service aan te bieden. Het exposeert een `TeamService` waarmee clients informatie over teams en spelers kunnen opvragen via gestructureerde XML-berichten.
*   **Data:**
    De service werkt met **Java Objecten** die in-memory worden geïnitialiseerd (hardcoded in de code).
    *   Voetbalteams (bv. Anderlecht, Genk, Brugge).
    *   Spelers gekoppeld aan deze teams (naam, positie, rugnummer).
