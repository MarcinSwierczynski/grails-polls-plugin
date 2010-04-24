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
                return val?.after(obj.startDate)
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

    def addAnswers(List<Answer> answers) {
        answers.each {answer -> this.addToAnswers(answer)};
    }

    String formatAnswersAsTxt() {
        def answersContent = answers.collect {it.content};
        return answersContent.join("|");
    }

    String formatVotesAsTxt() {
        def answersVote = answers.collect {it.votes};
        return "t:" + answersVote.join(",");
    }
}
