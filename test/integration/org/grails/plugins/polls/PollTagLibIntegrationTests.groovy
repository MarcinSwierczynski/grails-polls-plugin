package org.grails.plugins.polls

/**
 * @author Marcin Świerczyński
 * Date: 2010-04-29 @ 12:42:20
 */
class PollTagLibIntegrationTests extends GroovyTestCase {
    PollTagLib pollTagLib;

    protected void setUp() {
        pollTagLib = new PollTagLib();
    }

    void testShouldHasProperAnswersAndSize() {
        createAndRetrievePoll('Question1', ['A1', 'A2']);
        String chartImgTag = pollTagLib.pollResults(type: 'p3', width: '250', height: '100');
        assertTrue(chartImgTag.contains('chl=A1|A2'));
        assertTrue(chartImgTag.contains('chs=250x100'));
    }

    void testShouldSeeChangedVotes() {
        Poll p1 = createAndRetrievePoll('Question1', ['A1', 'A2']);

        increaseVotes(p1, 10);
        String chartImgTag = pollTagLib.pollResults(type: 'p3', width: '250', height: '100');
        assertTrue(chartImgTag.contains('chd=t:10,10'));

        increaseVotes(p1,1);
        chartImgTag = pollTagLib.pollResults(type: 'p3', width: '250', height: '100');
        assertTrue(chartImgTag.contains('chd=t:11,11'));
        assertFalse(chartImgTag.contains('chd=t:10,10'));
    }

    private Poll increaseVotes(Poll poll, int votesCount) {
        poll.answers.each {it.votes += votesCount};
        poll.save();
        return poll;
    }

    private Poll createAndRetrievePoll(String question, List<String> answers) {
		Poll p = new Poll(question: question, startDate: new Date());
        answers.each {p.addToAnswers(new Answer(content: it))};
        p.save();
        return p;
	}

}
