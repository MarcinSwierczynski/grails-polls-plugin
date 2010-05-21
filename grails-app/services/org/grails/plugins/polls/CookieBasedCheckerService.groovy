package org.grails.plugins.polls

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest

class CookieBasedCheckerService {

    def hasVoted(HttpServletRequest request, def pollId) {
        def cookieName = ConfigurationHolder.config.org.grails.plugins.polls.cookie.name;
        def cookie = request.cookies.find {it.name == cookieName};

        if (!cookie) {
            return false;
        }

        def hasVoted = cookie.value.contains(pollId.toString());

        return hasVoted;
    }

    def markAsVoted(HttpServletRequest request, HttpServletResponse response, def pollId) {
        def cookieName = ConfigurationHolder.config.org.grails.plugins.polls.cookie.name;
        def cookie = request.cookies.find {it.name == cookieName};

        def oldCookieContent = cookie?.value ?: '';
        def newCookieContent = oldCookieContent + ',' + pollId;
        def newCookie = new Cookie(cookieName, newCookieContent);
        newCookie.maxAge = ConfigurationHolder.config.org.grails.plugins.polls.cookie.validation.days * 24 * 60 * 60;
        response.addCookie(newCookie);
    }
}
