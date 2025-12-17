<?php

namespace App\Services;

use App\Models\PlayerStatus;

class PlayerMetrics
{
    public static function estimateRemainingPlayingMinutes(PlayerStatus $status): int
    {
        if ($status->getExhausted()) {
            return 0;
        }

        $heartRate = $status->getHeartRate();
        $lactate = $status->getLactate();

        // Formule: 90 - (heartRate - 90) - (lactate * 2)
        $estimate = 90 - ($heartRate - 90) - ($lactate * 2);

        // Zorg dat het niet negatief kan zijn (Math.max(0, ...))
        return (int) max(0, $estimate);
    }
}
