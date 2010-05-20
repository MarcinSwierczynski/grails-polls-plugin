package org.grails.plugins.polls

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Cookie
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PollPluginController {

    def pollService;
    
    def submit = {
        def answerIds = params.id;
        def answers = answerIds.collect { id -> Answer.get(id) }

        if (!answers || answers.any {answer -> answer == null}) {
            render template: 'error'
            return
        }

        def pollId = answers.first().poll.id;
        if(hasVoted(request, pollId)) {
            chain action: 'results', params: [id: pollId]
            return;
        }

        List results = answers.collect { answer ->  pollService.increaseVotes(answer) } 
        if (results && results.every {answer -> answer != null}) {
            markAsVoted(request, response, pollId);
            chain action: 'results', params: [id: pollId]
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

    def hasVoted(HttpServletRequest request, def pollId) {
        def cookieName = ConfigurationHolder.config.org.grails.plugins.polls.cookie.name;
        def cookie = request.cookies.find {it.name == cookieName};

        if(!cookie) {
            return false;
        }

        def hasVoted = cookie.value.contains(pollId.toString());

        return hasVoted;        
    }

    private void markAsVoted(HttpServletRequest request, HttpServletResponse response, def pollId) {
        def cookieName = ConfigurationHolder.config.org.grails.plugins.polls.cookie.name;
        def cookie = request.cookies.find {it.name == cookieName}; 

        def oldCookieContent = cookie?.value ?: '';
        def newCookieContent = oldCookieContent + ',' + pollId;
        def newCookie = new Cookie(cookieName, newCookieContent);
        newCookie.maxAge = ConfigurationHolder.config.org.grails.plugins.polls.cookie.validation.days * 24 * 60 * 60;
        response.addCookie(newCookie);
    }

}
