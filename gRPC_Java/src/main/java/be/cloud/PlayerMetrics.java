package be.cloud;

public class PlayerMetrics {
    private PlayerMetrics() {}

    public static int estimateRemainingPlayingMinutes(PlayerStatus status) {
        if (status.getExhausted()) return 0;

        int heartRate = status.getHeartRate();
        float lactate = status.getLactate();

        return (int) Math.max(0, 90 - (heartRate-90) - (lactate*2));
    }
}
