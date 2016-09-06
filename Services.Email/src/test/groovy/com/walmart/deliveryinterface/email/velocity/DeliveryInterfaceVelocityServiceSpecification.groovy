package com.walmart.deliveryinterface.email.velocity;

import static org.junit.Assert.*

import org.apache.velocity.Template

import spock.lang.Specification

class DeliveryInterfaceVelocityServiceSpecification extends Specification {
    DeliveryInterfaceVelocityService testObj
    Template templateMock = Mock()

    def setup() {
        testObj = Spy()
    }

    def "evaluateStringTest without place holder"() {
        Map<String, Object> dataMock = ["TEST":"World!"]
        when:
        String evaluatedString = testObj.evaluateString("Hello",dataMock)

        then:
        evaluatedString.equals("Hello") == true
    }

    def "evaluateStringTest"() {
        Map<String, Object> dataMock = ["TEST":"World!"]
        when:
        String evaluatedString = testObj.evaluateString("Hello \${TEST}",dataMock)

        then:
        evaluatedString.equals("Hello World!") == true
    }

    def "evaluateTemplateTest"() {
        Map<String, Object> dataMock = ["TEST":"World!"]

        given:
        testObj.getTemplate("template_url") >> templateMock

        when:
        def evaluatedTemplate = testObj.evaluateTemplate("template_url", dataMock)

        then:
        evaluatedTemplate != null
    }

    def "test get template" () {

        when:
        testObj.getTemplate("/velocity/templates/email/deliverySupportNotification.vm")

        then:
        true
    }
}
