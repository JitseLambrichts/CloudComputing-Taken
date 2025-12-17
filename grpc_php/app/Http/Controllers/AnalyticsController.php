<?php

namespace App\Http\Controllers;

use App\Models\AnalyticsServiceClient;
use App\Models\PlayerRequest;
use App\Services\PlayerMetrics;
use Grpc\ChannelCredentials;
use Illuminate\Http\Request;

class AnalyticsController extends Controller
{
    public function getPlayers()
    {
        return response()->json([
            'Bryan Heynen',
            'Konstantins Karetsas',
            'Matte Smets',
        ]);
    }

    public function getStatus(Request $request)
    {
        // 1. Maak de client aan
        $client = new AnalyticsServiceClient('localhost:8080', [
            'credentials' => ChannelCredentials::createInsecure(),
        ]);
        // 2. Haal de speler naam op uit de query parameter
        $playerName = $request->query('player', 'Bryan Heynen');
        // 3. Maak het gRPC request object
        $grpcRequest = new PlayerRequest();
        $grpcRequest->setPlayerName($playerName);
        // 4. Roep de functie aan
        list($response, $status) = $client->GetPlayerStatus($grpcRequest)->wait();
        if ($status->code !== \Grpc\STATUS_OK) {
            return "Error: " . $status->details;
        }
        // 5. Bereken statistieken
        $remainingMinutes = PlayerMetrics::estimateRemainingPlayingMinutes($response);
        // 6. Return de data
        return
            "Hartslag: " . $response->getHeartRate() .
            "<br>Lactaat-waarde: " . $response->getLactate() .
            "<br>Vermoeid? " . ($response->getExhausted() ? "Ja" : "Nee") .
            "<br>Geschatte minuten dat " . $response->getPlayerName() . " nog kan spelen: " . $remainingMinutes;
    }
}
