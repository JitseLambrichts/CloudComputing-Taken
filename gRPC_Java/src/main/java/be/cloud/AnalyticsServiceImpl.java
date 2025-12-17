package be.cloud;

import io.grpc.stub.StreamObserver;

public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    @Override
    public StreamObserver<LivePlayerUpdate> streamPlayerAnalytics(StreamObserver<AnalysisResponse> responseObserver) {
        return new StreamObserver<LivePlayerUpdate>() {
            @Override
            public void onNext(LivePlayerUpdate update) {
                System.out.println("Ontvagen update voor: " + update.getPlayerName());

                boolean shouldSubstitute = update.getCurrentHeartRate() > 180 || update.getCurrentLactate() > 15.0f;
                int fatigueLevel = calculateFatigueLevel(update);
                String recommendation = shouldSubstitute ? "Wissel speler" : "Speler kan nog doorspelen";

                AnalysisResponse response = AnalysisResponse.newBuilder()
                        .setPlayerName(update.getPlayerName())
                        .setRecommendation(recommendation)
                        .setShouldSubstitute(shouldSubstitute)
                        .setFatigueLevel(fatigueLevel)
                        .build();

                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in stream: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Client stream gestopt");
                responseObserver.onCompleted();
            }

            private int calculateFatigueLevel(LivePlayerUpdate update) {
                int level = (int) ((update.getCurrentHeartRate() / 20) + update.getCurrentLactate() / 2);
                return Math.min(10, Math.max(1, level));
            }
        };
    }

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
