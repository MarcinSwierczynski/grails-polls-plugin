package org.grails.plugins.polls

class Poll {

    static hasMany = [answers: Answer]

    String question
    Date start
    Date end
    Date dateCreated
    boolean active = true

    static constraints = {
        question(blank: false)
        start(nullable: false,
            validator: {val, obj ->
                if (!obj?.end)
                    return true
                if (val?.compareTo(obj.end) > 0) {
                    //start have to be before end
                    return false
                }
            }
        )
        end(nullable: true)
    }

    static mapping = {
        question type: 'text'
    }
}
