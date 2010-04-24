package org.grails.plugins.polls

import grails.test.*
import java.text.SimpleDateFormat

class PollTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDateValidatingWithEndDateNull() {
        def poll = new Poll(question: 'Sample question')
        def df = new SimpleDateFormat("yyyy-MM-dd")
        poll.startDate = df.parse('2010-04-24')

        assertTrue 'Should return true', poll.validate()
    }
}
