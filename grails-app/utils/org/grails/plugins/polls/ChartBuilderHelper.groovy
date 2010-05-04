package org.grails.plugins.polls

/**
 * @author Marcin Świerczyński
 * Date: 2010-04-30 @ 18:54:07
 */
class ChartBuilderHelper {
    public static List<Integer> listUpToValueWithStep(int max, int step) {
        List<Integer> axisValues = [];

        max += step;

        for(int i = 0; i <= max; i+=step) {
            axisValues << i;
        }

        return axisValues;
    }

    public static String getAxisScalingString(int min, int max) {
        return '&amp;chds=' + min + ',' + max;
    }

    public static String getBarChartLabelsString(String color) {
        return '&amp;chm=N,' + color + ',0,-1,11';
    }

    public static String getBarsScalingString(String mode) {
        return '&amp;chbh='+mode;
    }
}
