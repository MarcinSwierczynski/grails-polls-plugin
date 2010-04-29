package org.grails.plugins.polls

class PollTagLib {
    static namespace = "polls";

    PollService pollService;

    def pollResults = { attrs ->
        def type = attrs.type;
        int width = Integer.parseInt(attrs.width);
        int height = Integer.parseInt(attrs.height);
        def poll = pollService.getLatestPoll();

        def chart = new GoogleChartBuilder();
        //TODO use different chart types
        def chartUrl = chart.pie3DChart {
            size(w:width, h:height);
            data(encoding:'text') {
                dataSet(poll.answerVotes());
            }
            labels {
                poll.answerContents().each { label(it) };
            }
        };

        out << getChartImgTag(chartUrl);
    }

    def getChartImgTag(def chartUrl) {
        String imgTag = '<img src="' + chartUrl + '" />';
        return imgTag;
    }
}
