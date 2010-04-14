package org.grails.plugins.polls

/**
 * @author Marcin Świerczyński
 * Date: 2010-04-14 @ 21:19:38
 */
class PollServiceIntegrationTests extends GroovyTestCase {
	def pollService;

	String question = "Is Grails a valuable framework?";
	List<String> answers = ["Yes", "No", "I'm not sure"];

	void testShouldSavePollFromStrings() {
		Poll poll = createAndRetrievePoll(question, answers);

		assertNotNull(poll);
		assertEquals(question, poll.question);
		assertEquals(answers.size(), poll.answers.size());
		assertEquals(answers[0], poll.answers[0].content);
	}

	void testShouldIncreaseAnswerVotes() {
		Poll poll = createAndRetrievePoll(question, answers);
		Answer answer = poll.answers[0];
		pollService.increaseVotes(answer);

		Answer votedAnswer = Answer.findById(answer.id);
		
		assertEquals(1, votedAnswer.votes);
	}

	void testShouldRetrieveLatestPoll() {
		createAndRetrievePoll(question, answers);
		Poll secondPoll = createAndRetrievePoll("Another question", ["A1", "A2"]);

		Poll latestPoll = pollService.getLatestPoll();

		assertNotNull(latestPoll);
		assertTrue(latestPoll.active);
		assertEquals(secondPoll.question, latestPoll.question);
		assertEquals(secondPoll.answers[0].content, latestPoll.answers[0].content);
	}

	private Poll createAndRetrievePoll(String question, List<String> answers) {
		pollService.createPoll(question, answers);
		Poll poll = Poll.findByQuestion(question)
		return poll
	}

}

