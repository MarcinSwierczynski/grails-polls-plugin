package org.grails.plugins.polls

import org.codehaus.groovy.grails.commons.ConfigurationHolder

//TODO more configuration options, ex. colours
class PollTagLib {
    static namespace = "polls";

    PollService pollService;

    //TODO handle errors
    def latestPollResults = { attrs ->
        def type = attrs.type;
        int width = Integer.parseInt(attrs.width);
        int height = Integer.parseInt(attrs.height);
        def poll = pollService.getLatestPoll();

        def chart = new GoogleChartBuilder();
        def result = chart.pie3DChart {
            size(w:width, h:height);
            data(encoding:'text') {
                dataSet(poll.answerVotes());
            }
            labels {
                poll.answerContents().each { label(it) };
            }
        };

//        def chartHtmlTag = getChartImgTag(poll, width, height, type);

        out << result;
    }

    def getChartImgTag(Poll poll, int width, int height, String type) {
        String chartUrl = getChartUrl(poll, width, height, type);
        String imgTag = '<img src="' + chartUrl + '" />';
        return imgTag;
    }

    private String getChartUrl(Poll poll, int width, int height, String type) {
        String googleChartsPrefix = ConfigurationHolder.config.org.grails.plugins.polls.google.charts.url;
        String chartType = "cht=" + type + "&amp;";
        String chartSize = "chs=" + width + "x" + height + "&amp;";
        String data = "chd=" + poll.formatVotesAsTxt() + "&amp;";
        String labels = "chl=" + poll.formatAnswersAsTxt();

        return googleChartsPrefix + chartType + chartSize + data + labels;
    }
}
