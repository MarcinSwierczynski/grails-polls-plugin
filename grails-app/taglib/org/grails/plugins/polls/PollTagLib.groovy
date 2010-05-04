package org.grails.plugins.polls

class PollTagLib {
    static namespace = "polls";

    PollService pollService;

    private def chartParamsClosure;
    private def axisYLabels;

    /**
     * available types: pie, pie3D, bar, line, venn, scatter
     * colors -- list of colors hex values (without # prefix)
     */
    def pollResults = { attrs ->
        def poll = attrs.id ? Poll.get(attrs.id) : pollService.getLatestPoll();
        def chartType = attrs.type;
        int width = Integer.parseInt(attrs.width);
        int height = Integer.parseInt(attrs.height);
        def colorsList = attrs.colors;

        int maxVotesValue = poll.answerVotes().max();
        axisYLabels = ChartBuilderHelper.listUpToValueWithStep(maxVotesValue, 10);

        chartParamsClosure = {
            size(w:width, h:height);
            data(encoding:'text') {
                dataSet(poll.answerVotes());
            }
            labels {
                poll.answerContents().each { label(it) };
            }
            if(colorsList) {
                colors {
                    colorsList.each {color(it)}
                }
            }
            axis(left:axisYLabels);
        }

        String chartUrl = chartUrl(chartType);

        out << getChartImgTag(chartUrl);
    }

    private String chartUrl(def type) {
        def chart = new GoogleChartBuilder();
        String chartClosureName = type + 'Chart';
        String chartUrl;
        if (type.equals('bar')) {
            chartUrl = chart."$chartClosureName"(['vertical', 'grouped'], chartParamsClosure);
        } else {
            chartUrl = chart."$chartClosureName"(chartParamsClosure);
        }
        chartUrl += ChartBuilderHelper.getAxisScalingString(0, axisYLabels.max());
        chartUrl += ChartBuilderHelper.getBarChartLabelsString('000000');
        chartUrl += ChartBuilderHelper.getBarsScalingString('a');
        return chartUrl;
    }

    def getChartImgTag(def chartUrl) {
        String imgTag = '<img src="' + chartUrl + '" />';
        return imgTag;
    }
}
