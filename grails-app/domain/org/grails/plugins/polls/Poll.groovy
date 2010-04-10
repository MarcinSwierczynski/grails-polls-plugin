package org.grails.plugins.polls

class Poll {

    static hasMany = [answers: Answer]

    static constraints = {
    }
}
