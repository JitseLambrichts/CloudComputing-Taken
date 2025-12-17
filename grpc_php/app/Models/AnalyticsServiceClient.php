<?php
namespace App\Models;

use Grpc\BaseStub;

class AnalyticsServiceClient extends BaseStub {
    public function __construct($hostname, $opts, $channel = null) {
        parent::__construct($hostname, $opts, $channel);
    }

    public function GetPlayerStatus(\App\Models\PlayerRequest $argument, $metadata = [], $options = []) {
        // De string verwijst naar /package.Service/Methode
        return $this->_simpleRequest('/be.cloud.AnalyticsService/GetPlayerStatus',
            $argument,
            ['\App\Models\PlayerStatus', 'decode'],
            $metadata, $options);
    }


    public function StreamPlayerAnalytics($metadata = [], $options = []) {
        return $this->_serverStreamRequest(
            '/be.cloud.AnalyticsService/StreamPlayerAnalytics',
            [],
            ['\\App\\Models\\AnalysisResponse', 'decode'],
            $metadata,
            $options
        );
    }
}