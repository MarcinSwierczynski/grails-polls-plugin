package org.grails.plugins.polls

class PollPluginController {

    def pollService

    def submit = {
        def answerIds = params.id;
        def answers = answerIds.collect { id -> Answer.get(id) }

        if (!answers || answers.any {answer -> answer == null}) {
            render template: 'error'
            return
        }

        def pollId = answers.first().poll.id;

        List results = answers.collect { answer ->  pollService.increaseVotes(answer) } 
        if (results && results.every {answer -> answer != null}) {
            chain action: 'results', params: [id: pollId]
        }
        else {
            render template: 'error'
        }
    }

    def results = {
        def poll = Poll.get(params.id)

        if (!poll) {
            render template: 'error'
            return
        }

        render template: 'results', model: [poll: poll]
    }
}
