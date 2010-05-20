package org.grails.plugins.polls

import grails.test.ControllerUnitTestCase

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
        def pollControl = mockFor(PollService);
        pollControl.demand.increaseVotes(2..2) {answer -> answer.votes++; answer};
        this.controller.pollService = pollControl.createMock();

        def poll = new Poll(id: 1, question: "Some question?", startDate: new Date());
        mockDomain(Answer, [new Answer(content: "A1", poll: poll), new Answer(content: "A2", poll: poll)]);

        this.controller.params.id = [1,2];

        this.controller.submit();

        assertEquals "results", this.controller.chainArgs.action;
        assertEquals poll.id, this.controller.chainArgs.params.id;
    }

    //TODO: tests for cookies handling
}
