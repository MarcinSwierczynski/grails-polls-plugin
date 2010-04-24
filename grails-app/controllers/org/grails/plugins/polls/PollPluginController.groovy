package org.grails.plugins.polls

class PollPluginController {

    def submit = {
        def poll = Poll.get(params.id)

        if (!poll) {
            //TODO
            return
        }
    }
}
