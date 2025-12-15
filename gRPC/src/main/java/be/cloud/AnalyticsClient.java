package be.cloud;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AnalyticsClient {
    public static void main(String[] args) {
        String playerName = "Konstaninos Karetsas";

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        AnalyticsServiceGrpc.AnalyticsServiceBlockingStub stub = AnalyticsServiceGrpc.newBlockingStub(channel);

        System.out.println("Data opvragen voor: " + playerName);
        PlayerStatus status = stub.getPlayerStatus(PlayerRequest.newBuilder()
                .setPlayerName(playerName)
                .build()
        );

        System.out.println("Ontvangen: " + status.getPlayerName());
        System.out.println("Hartslag: " + status.getHeartRate());
        System.out.println("Lactaat-waarde: " + status.getLactate());
        System.out.println("Vermoeid? " + status.getExhausted());

        int remainingMinutes = PlayerMetrics.estimateRemainingPlayingMinutes(status);
        System.out.println("Geschatte minuten dat " + status.getPlayerName() + " nog kan spelen: " + remainingMinutes);

        channel.shutdown();
    }
}
