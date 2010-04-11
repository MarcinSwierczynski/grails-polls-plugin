package org.grails.plugins.polls

class Poll {

    static hasMany = [answers: Answer]

    String question
    Date startDate
    Date endDate
    Date dateCreated
    boolean active = true

    static constraints = {
        question(blank: false)
        startDate(nullable: false,
            validator: {val, obj ->
                if (!obj?.endDate)
                    return true
                if (val?.compareTo(obj.endDate) > 0) {
                    //start have to be before end
                    return false
                }
            }
        )
        endDate(nullable: true)
    }

    static mapping = {
        question type: 'text'
    }
}
