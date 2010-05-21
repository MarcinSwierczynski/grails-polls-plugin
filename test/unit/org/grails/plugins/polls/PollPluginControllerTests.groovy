package org.grails.plugins.polls

import grails.test.ControllerUnitTestCase
import javax.servlet.http.Cookie

class PollPluginControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp();

        mockConfig('''
            org.grails.plugins.polls.cookie.name = "grails.plugins.polls.voted"
            org.grails.plugins.polls.cookie.validation.days = 7
        ''');
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testShouldReturnErrorMessage() {
        mockDomain(Answer);

        this.controller.params.id = 1;

        this.controller.submit();

        assertEquals "error", this.controller.renderArgs.template
    }

    void testShouldReturnResults() {
        mockPollService();

        def poll = mockPollAndAnswers();

        this.controller.params.id = [1,2];

        this.controller.submit();

        assertEquals "results", this.controller.chainArgs.action;
        assertEquals poll.id, this.controller.chainArgs.params.id;
    }

    void testShouldReturnErrorIfUserHasAlreadyVoted() {
        Poll poll = mockPollAndAnswers();

        this.controller.params.id = [1,2];

        Cookie[] cookies = [new Cookie("grails.plugins.polls.voted", ",1")].toArray();
        this.controller.request.setCookies(cookies);

        this.controller.submit();

        assertEquals "results", this.controller.chainArgs.action;
        assertEquals poll.id, this.controller.chainArgs.params.id;
    }

    void testShouldVoteIfUserHasVotedInAnotherPoll() {
        mockPollService();
        Poll poll = mockPollAndAnswers();

        this.controller.params.id = [1,2];

        Cookie[] cookies = [new Cookie("grails.plugins.polls.voted", ",2")].toArray();
        this.controller.request.setCookies(cookies);

        this.controller.submit();

        def newCookie = this.controller.response.cookies.find {it.name == "grails.plugins.polls.voted"}
        assertEquals ",2,"+poll.id, newCookie.value;
        assertEquals "results", this.controller.chainArgs.action;
        assertEquals poll.id, this.controller.chainArgs.params.id;
    }


    void testShouldVoteIfUserHasNotVoted() {
        mockPollService();
        Poll poll = mockPollAndAnswers();

        this.controller.params.id = [1,2];

        this.controller.submit();

        def newCookie = this.controller.response.cookies.find {it.name == "grails.plugins.polls.voted"}
        assertEquals ","+poll.id, newCookie.value;
        assertEquals "results", this.controller.chainArgs.action;
        assertEquals poll.id, this.controller.chainArgs.params.id;
    }

    private Poll mockPollAndAnswers() {
        def poll = new Poll(id: 1, question: "Some question?", startDate: new Date());
        mockDomain(Answer, [new Answer(content: "A1", poll: poll), new Answer(content: "A2", poll: poll)])
        return poll
    }

    private def mockPollService() {
        def pollControl = mockFor(PollService);
        pollControl.demand.increaseVotes(2..2) {answer -> answer.votes++; answer};
        this.controller.pollService = pollControl.createMock()
    }
}
