package org.grails.plugins.polls

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class Poll {

	static hasMany = [answers: Answer]

	String question
	Date startDate
	Date endDate
	Date dateCreated
	boolean active = true

	static constraints = {
		question(blank: false)
		startDate(nullable: false)
		endDate(nullable: true,
				  validator: {val, obj ->
					  if (val?.compareTo(obj.startDate) < 0) {
						  //start have to be before end
						  return false
					  }
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
	}
}
