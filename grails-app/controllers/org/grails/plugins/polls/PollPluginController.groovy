package org.grails.plugins.polls

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
            chain action: 'results', params: [id: pollId, hasVoted: true]
            return;
        }

        List results = answers.collect { answer ->  pollService.increaseVotes(answer) }
        if (results && results.every {answer -> answer != null}) {
            cookieBasedCheckerService.markAsVoted(request, response, pollId);
            chain action: 'results', params: [id: pollId, hasVoted: false];
        } else {
            render template: 'error'
        }
    }

    def results = {
        def poll = Poll.get(params.id)
        boolean hasVoted = params.hasVoted ? new Boolean(params.hasVoted) : false;

        if (!poll) {
            render template: 'error'
            return;
        }

        render template: 'results', model: [poll: poll, hasVoted: hasVoted], plugin: 'polls'
    }

}
