package com.example.yos.mycluster;

public class Null {
    static double fromLatitude(double latitude) {
        double radians = Math.toRadians(latitude + 90) / 2;
        return Math.toDegrees(Math.log(Math.tan(radians)));
    }

    static double toLatitude(double mercator) {
        double radians = Math.atan(Math.exp(Math.toRadians(mercator)));
        return Math.toDegrees(2 * radians) - 90;
    }

    public void run() {
        double mercator = fromLatitude(85.0511287798);
        double[] mercators = new double[16];
        double[][] latitudes = new double[16][2];
        double[][] longitudes = new double[16][2];
        mercators[0] = mercator;
        latitudes[0][0] = toLatitude(mercators[0]);
        latitudes[0][1] = toLatitude(mercators[0]);
        longitudes[0][0] = 180;
        longitudes[0][1] = 180;
        System.out.println();
        System.out.printf("m(%d): %2.10f\n", 0, mercators[0]);
        System.out.printf("l(%d): %2.10f\n", 0, latitudes[0][0]);
        System.out.printf("l(%d): %2.10f\n", 0, latitudes[0][1]);
        for (int i = 1; i < 16; ++i) {
            mercators[i] = mercators[i-1] / 2;
            latitudes[i][0] = toLatitude(mercators[i]);
            latitudes[i][1] = toLatitude(mercators[i-1]+mercators[i]);
            longitudes[i][0] = longitudes[i-1][0] / 2;
            longitudes[i][1] = longitudes[i-1][0] + longitudes[i][0];

            System.out.println();
            System.out.printf("mrc(%d): %2.10f\n", i, mercators[i]);
            System.out.printf("lat(%d): %2.10f\n", i, latitudes[i][0]);
            System.out.printf("lat(%d): %2.10f\n", i, latitudes[i][1]);
            System.out.printf("lng(%d): %2.10f\n", i, longitudes[i][0]);
            System.out.printf("lng(%d): %2.10f\n", i, longitudes[i][1]);
        }

        System.out.println("double[] latitudes = new double[17][2];");
        for (int i = 0; i < 16; ++i) {
            System.out.printf("latitudes[%d][0] = %2.10f;\n", i, latitudes[i][0]);
            System.out.printf("latitudes[%d][1] = %2.10f;\n", i, latitudes[i][1]);
        }
    }
}
