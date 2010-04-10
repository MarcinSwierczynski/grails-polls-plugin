package org.grails.plugins.polls

class Answer {

    static belongsTo = Poll

    String content
    int votes = 0
    
    static constraints = {
        content(blank: false)
    }
}
