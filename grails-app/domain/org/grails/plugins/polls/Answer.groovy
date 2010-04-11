package org.grails.plugins.polls

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class Answer {

    static belongsTo = [poll : Poll]

    String content
    int votes = 0
    
    static constraints = {
        content(blank: false)
    }

    static mapping = {
        def config = ConfigurationHolder.config
        if(config.grails.polls.answer.table) {
            table config.grails.polls.answer.table.toString()
        }
        else {
            table "answers"
        }
    }
}
