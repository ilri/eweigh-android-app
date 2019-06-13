package org.ilri.eweigh.liveweight;

import java.text.DecimalFormat;

public class LiveWeight {

    /**
     *
     * Calculate the estimated LiveWeight(LW) of cattle using Hearth Girth measurement
     *
     * */
    public static double calculateLW(double heartGirth){
        double liveWeight = Math.pow((0.02451 + 0.04894 * heartGirth), (1/0.3595));

        // Return value to two decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        return Double.valueOf(df.format(liveWeight));
    }
}
