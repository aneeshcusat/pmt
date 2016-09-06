package com.walmart.deliveryinterface.email.util;

import static org.junit.Assert.*
import spock.lang.Specification

class EmailMessageSpecification extends Specification {
    EmailMessage testObj

    def setup(){
        testObj = new EmailMessage("","")
        testObj = new EmailMessage("subject","body","htmlbody")
    }

    def "test setters and getters"(){
        expect:
        testObj.getSubject() == "subject"
        testObj.getBody() == "body"
        testObj.getHtmlBody() == "htmlbody"
    }
}
