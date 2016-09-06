package com.walmart.deliveryinterface.email.velocity;

import static org.junit.Assert.*

import javax.mail.internet.MimeMessage

import spock.lang.Specification

import com.walmart.deliveryinterface.email.util.DeliveryInterfaceTemplateEmailInfo
import com.walmart.deliveryinterface.email.util.EmailMessage

class DeliveryInterfaceEmailTemplateRendererServiceSpecification extends Specification {
    DeliveryInterfaceEmailTemplateRendererService testObj
    DeliveryInterfaceVelocityService deliveryInterfaceVelocityServiceMock = Mock()
    DeliveryInterfaceTemplateEmailInfo emailInfoMock = Mock()
    EmailMessage emailMessageMock = Mock()
    MimeMessage mimeMessageMock = Mock()
    Object objectMock = Mock()

    def setup() {
        testObj = Spy()
        testObj.setDeliveryInterfaceVelocityService(deliveryInterfaceVelocityServiceMock)
    }

    def "createMessage - positive flow"() {
        String[] emailTo = {"email_to"}
        given:
        emailInfoMock.getTemplateParameters() >> ["template_key":objectMock]
        emailInfoMock.getVelocityTemplateName() >> "template_url"
        emailInfoMock.getMailSubject() >> "email_subject"
        deliveryInterfaceVelocityServiceMock.evaluateTemplate(_, emailInfoMock.getTemplateParameters()) >> "template_body"
        deliveryInterfaceVelocityServiceMock.evaluateString(_, emailInfoMock.getTemplateParameters()) >> "evaluated_string"
        emailInfoMock.getMailTo() >> "email_to"
        emailInfoMock.getMimeMessage() >> mimeMessageMock
        emailInfoMock.getMailFrom() >> "email_from"
        emailInfoMock.getMailCc() >> "cc"
        emailInfoMock.getMailBcc() >> "bcc"

        when:
        def mimeMessagePreparator = testObj.createMessage(emailInfoMock, emailTo)

        then:
        mimeMessagePreparator.prepare(mimeMessageMock);
        mimeMessagePreparator != null
    }

    def "createMessage - negative flow"() {
        String[] emailTo
        given:
        deliveryInterfaceVelocityServiceMock.evaluateTemplate(_, emailInfoMock.getTemplateParameters()) >> "template_body"
        deliveryInterfaceVelocityServiceMock.evaluateString(_, emailInfoMock.getTemplateParameters()) >> "evaluated_string"

        when:
        def mimeMessagePreparator = testObj.createMessage(null, emailTo)

        then:
        mimeMessagePreparator == null
    }

    def "createMessage - positive flow2"() {
        String[] emailTo
        given:
        deliveryInterfaceVelocityServiceMock.evaluateTemplate(_, emailInfoMock.getTemplateParameters()) >> "template_body"
        deliveryInterfaceVelocityServiceMock.evaluateString(_, emailInfoMock.getTemplateParameters()) >> "evaluated_string"

        when:
        def mimeMessagePreparator = testObj.createMessage(emailInfoMock, emailTo)
        testObj.getDeliveryInterfaceVelocityService()

        then:
        mimeMessagePreparator != null
    }
}
