package org.grails.plugins.polls

class PollController {

	def scaffold = true

	def pollService;

	def exampleResults = {
		pollService.createPoll("Is that cool?", ["Yes", "No", "Not sure"]);
		Poll poll = pollService.getLatestPoll();

		increaseVotesBy(poll.answers[0], 1);
		increaseVotesBy(poll.answers[1], 1);
		increaseVotesBy(poll.answers[2], 1);
	}

	private void increaseVotesBy(def answer, int upTo) {
		(1..upTo).each {pollService.increaseVotes(answer)};
	}
}
