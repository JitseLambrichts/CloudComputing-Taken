package be.cloud;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StreamingAnalyticsClient {
    public static void main(String[] args) throws InterruptedException {
        String host = System.getenv("GRPC_HOST") != null ? System.getenv("GRPC_HOST") : "localhost";
        int port = System.getenv("GRPC_PORT") != null ? Integer.parseInt(System.getenv("GRPC_PORT")) : 8080;
        
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        AnalyticsServiceGrpc.AnalyticsServiceStub asyncStub = 
            AnalyticsServiceGrpc.newStub(channel);

        CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<AnalysisResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(AnalysisResponse response) {
                System.out.println("=== ANALYSE ONTVANGEN ===");
                System.out.println("Speler: " + response.getPlayerName());
                System.out.println("Vermoeidheid level: " + response.getFatigueLevel());
                System.out.println("Aanbeveling: " + response.getRecommendation());
                System.out.println("=== EINDE ANALYSE SPELER ===");
                System.out.println();
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server stream gestopt");
                finishLatch.countDown();
            }
        };

        StreamObserver<LivePlayerUpdate> requestObserver = asyncStub.streamPlayerAnalytics(responseObserver);

        String[] players = {"Bryan Heynen", "Konstantins Karetsas", "Matte Smets"};

        Random random = new Random();

        for (String player : players) {
            LivePlayerUpdate update = LivePlayerUpdate.newBuilder()
                    .setPlayerName(player)
                    .setCurrentHeartRate(120 + random.nextInt(80))
                    .setCurrentLactate(2.0f + random.nextFloat() * 15.0f)
                    .setTimestamp(System.currentTimeMillis())
                    .build();

            System.out.println("=== STUUR ANALYSE ===");
            System.out.println("Stuur update voor: " + update.getPlayerName());
            System.out.println("Met hartslag: " + update.getCurrentHeartRate());
            System.out.println("Met lactaat-waardes: " + update.getCurrentLactate());
            requestObserver.onNext(update);
            Thread.sleep(1000);            
        }

        requestObserver.onCompleted();

        finishLatch.await(30, TimeUnit.SECONDS);
        channel.shutdown();
    }
}