package org.grails.plugins.polls

import grails.test.*
import java.text.SimpleDateFormat

class PollIntegrationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testBlankQuestion() {
        def poll = new Poll()

        assertFalse poll.validate()
        assertTrue poll.hasErrors()
    }

    void testDateValidatingWithEndDateNull() {
        def poll = new Poll(question: 'Sample question')
        def df = new SimpleDateFormat("yyyy-MM-dd")
        poll.startDate = df.parse('2010-04-24')

        assertTrue 'Should return true', poll.validate()
        assertFalse 'Has no error', poll.hasErrors()
    }

    void testEndDateIsNotBeforeStartDate() {
        def poll = new Poll(question: 'Sample question')
        def df = new SimpleDateFormat("yyyy-MM-dd")
        poll.startDate = df.parse('2010-04-24')
        poll.endDate = df.parse('2010-04-23')

        assertFalse 'Poll is not valid', poll.validate()
        assertTrue 'Poll has errors', poll.hasErrors()

        def badField = poll.errors.getFieldError('endDate')
        assertNotNull "I'm expecting to find an error on the endDate field", badField

    }
}
