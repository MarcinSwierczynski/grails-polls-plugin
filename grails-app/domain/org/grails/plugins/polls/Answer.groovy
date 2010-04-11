package org.grails.plugins.polls

class Answer {

    static belongsTo = [poll : Poll]

    String content
    int votes = 0
    
    static constraints = {
        content(blank: false)
    }
}
