package org.grails.plugins.polls

//TODO Add scaling to bar typed charts
class PollTagLib {
    static namespace = "polls";

    PollService pollService;

    private def chartParamsClosure;

    /**
     * available types: pie, pie3D, bar, line, lineXY, venn, scatter
     * colors -- list of colors hex values (without # prefix)
     */
    def pollResults = { attrs ->
        def poll = attrs.id ? Poll.get(attrs.id) : pollService.getLatestPoll();
        def chartType = attrs.type;
        int width = Integer.parseInt(attrs.width);
        int height = Integer.parseInt(attrs.height);
        def colorsList = attrs.colors;

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
        return chartUrl;
    }

    def getChartImgTag(def chartUrl) {
        String imgTag = '<img src="' + chartUrl + '" />';
        return imgTag;
    }
}
