package org.grails.plugins.polls

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class Poll {

    List answers;
    static hasMany = [answers: Answer]

    String question
    Date startDate
    Date endDate
    Date dateCreated
    boolean active = true

    static constraints = {
        question(blank: false, unique: true)
        startDate(nullable: false)
        endDate(nullable: true,
            validator: {val, obj ->
                if(val == null) {
                    return true
                }
                if (val?.compareTo(obj.startDate) < 0) {
                    //start have to be before end
                    return false
                }
                return true;
            }
        )
    }

    static mapping = {
        def config = ConfigurationHolder.config
        if (config.grails.polls.poll.table) {
            table config.grails.polls.poll.table.toString()
        }
        else {
            table "polls"
        }

        question type: 'text'

        answers fetch:"join";
    }

    def addAnswers(List<Answer> givenAnswers) {
        givenAnswers.each {this.addToAnswers(it)};
    }

    List getAnswers() {
        return answers.collect {it.content};
    }

    List getVotes() {
        return answers.collect {it.votes};
    }
}
