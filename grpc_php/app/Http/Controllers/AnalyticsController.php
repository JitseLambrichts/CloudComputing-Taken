<?php

namespace App\Http\Controllers;

use App\Models\AnalyticsServiceClient;
use App\Models\PlayerRequest;
use App\Services\PlayerMetrics;
use Grpc\ChannelCredentials;

class AnalyticsController extends Controller
{
    public function getStatus()
    {
        // 1. Maak de client aan (let op de poort, hier voorbeeld 50051)
        $client = new AnalyticsServiceClient('localhost:8080', [
            'credentials' => ChannelCredentials::createInsecure(),
        ]);

        // 2. Maak het request object
        $request = new PlayerRequest();
        $request->setPlayerName("Bryan Heynen"); // Of data uit een formulier

        // 3. Roep de functie aan
        list($response, $status) = $client->GetPlayerStatus($request)->wait();

        if ($status->code !== \Grpc\STATUS_OK) {
            return "Error: " . $status->details;
        }

        // 4. Bereken statistieken
        $remainingMinutes = PlayerMetrics::estimateRemainingPlayingMinutes($response);

        // 5. Gebruik de data
        return "Speler: " . $response->getPlayerName() .
            "<br>Hartslag: " . $response->getHeartRate() .
            "<br>Lactaat-waarde: " . $response->getLactate() .
            "<br>Vermoeid? " . ($response->getExhausted() ? "Ja" : "Nee") .
            "<br>Geschatte minuten dat " . $response->getPlayerName() . " nog kan spelen: " . $remainingMinutes;
    }
}
