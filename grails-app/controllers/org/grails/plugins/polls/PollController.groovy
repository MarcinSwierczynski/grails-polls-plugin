package org.grails.plugins.polls

class PollController {

	def scaffold = true

	def pollService;

	def exampleResults = {
		pollService.createPoll("Is that cool?", ["Yes", "No", "Not sure"]);
		Poll poll = pollService.getLatestPoll();

        poll.isMultiple = true;
        poll.save();

		increaseVotesBy(poll.answers[0], 6);
		increaseVotesBy(poll.answers[1], 5);
		increaseVotesBy(poll.answers[2], 2);
	}

	private void increaseVotesBy(def answer, int upTo) {
		(1..upTo).each {pollService.increaseVotes(answer)};
	}
}
