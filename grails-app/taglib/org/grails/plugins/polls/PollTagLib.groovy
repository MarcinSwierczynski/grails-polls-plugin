package org.grails.plugins.polls

import groovy.xml.MarkupBuilder

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

        int maxVotesValue = pollService.answerVotes(poll).max();
        axisYLabels = ChartBuilderHelper.listUpToValueWithStep(maxVotesValue, 10);

        chartParamsClosure = {
            size(w:width, h:height);
            data(encoding:'text') {
                dataSet(pollService.answerVotes(poll));
            }
            labels {
                pollService.answerContents(poll).each { label(it) };
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

    def poll = { attrs ->
        def poll = attrs.id ? Poll.get(attrs.id) : pollService.getLatestPoll();

        def writer = new StringWriter();
        def htmlBuilder = new MarkupBuilder(writer);
        htmlBuilder.setDoubleQuotes(true);

        def formAction = g.createLink(controller: 'pollPlugin', action: 'submit')
        htmlBuilder.div('class':'poll', id: 'poll_'+poll.id) {
            htmlBuilder.form(action: formAction, method: 'post',
                onsubmit: 'new Ajax.Updater("poll_'+poll.id+'","'+formAction+'",{asynchronous:true,evalScripts:true,parameters:Form.serialize(this)});return false') {
                fieldset {
                    legend(poll.question)
                    poll.answers.each {answer ->
                        input(type: poll.isMultiple ? 'checkbox' : 'radio', name: 'id', id: 'answer_'+answer.id, value: answer.id);
                        label('for': 'answer_'+answer.id, answer.content);
                    }
                    input(type: 'submit', value: g.message(code: 'poll.plugin.vote.button'));
                }
            }
        }

        out << writer.toString();
    }
}
