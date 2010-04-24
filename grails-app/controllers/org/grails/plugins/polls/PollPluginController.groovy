package org.grails.plugins.polls

class PollPluginController {

    def pollService

    def submit = {
        def answer = Answer.get(params.id)

        if (!answer) {
            render template: 'error'
            return
        }

        def result = pollService.increaseVotes(answer)
        if (result) {
            chain action: 'results', params: [id: answer.poll.id]
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
