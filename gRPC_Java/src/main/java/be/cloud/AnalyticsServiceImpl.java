package be.cloud;

import io.grpc.stub.StreamObserver;

public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    @Override
    public void getPlayerStatus(PlayerRequest request, StreamObserver<PlayerStatus> responseObserver) {
        String playerName = request.getPlayerName();

        String name;
        int heartRate;
        float lactate;
        boolean exhausted;

        switch (playerName) {
            case "Bryan Heynen" -> {
                name = playerName;
                heartRate = 150;
                lactate = 4;
                exhausted = false;
            }
            case "Konstaninos Karetsas" -> {
                name = playerName;
                heartRate = 190;
                lactate = 17;
                exhausted = true;
            }
            case "Matte Smets" -> {
                name = playerName;
                heartRate = 100;
                lactate = 5;
                exhausted = false;
            }
            default -> {
                name = "Geen speler gevonden";
                heartRate = 0;
                lactate = 0;
                exhausted = false;
            }
        }

        PlayerStatus response = PlayerStatus.newBuilder()
                .setPlayerName(name)
                .setHeartRate(heartRate)
                .setLactate(lactate)
                .setExhausted(exhausted)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
