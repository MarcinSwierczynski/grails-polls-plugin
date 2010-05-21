package org.grails.plugins.polls

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Cookie
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PollPluginController {

    def pollService;
    def cookieBasedCheckerService;

    def submit = {
        def answerIds = params.id;
        def answers = answerIds.collect { id -> Answer.get(id) }

        if (!answers || answers.any {answer -> answer == null}) {
            render template: 'error'
            return false;
        }

        def pollId = answers.first().poll.id;
        
        if (cookieBasedCheckerService.hasVoted(request, pollId)) {
            chain action: 'results', params: [id: pollId]
            return;
        }

        List results = answers.collect { answer ->  pollService.increaseVotes(answer) }
        if (results && results.every {answer -> answer != null}) {
            cookieBasedCheckerService.markAsVoted(request, response, pollId);
            chain action: 'results', params: [id: pollId];
        } else {
            render template: 'error'
        }
    }

    def results = {
        def poll = Poll.get(params.id)

        if (!poll) {
            render template: 'error'
            return;
        }

        render template: 'results', model: [poll: poll]
    }

}
