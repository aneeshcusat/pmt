package com.walmart.deliveryinterface.email;

import static org.junit.Assert.*

import javax.mail.internet.MimeMessage

import org.springframework.mail.javamail.JavaMailSender

import spock.lang.Specification

import com.walmart.deliveryinterface.email.util.DeliveryInterfaceTemplateEmailInfo
import com.walmart.deliveryinterface.email.velocity.DeliveryInterfaceEmailTemplateRendererService

class DeliveryInterfaceEmailSenderSpecification extends Specification {
    DeliveryInterfaceEmailSender testObj
    JavaMailSender javaMailSenderMock = Mock()
    DeliveryInterfaceEmailTemplateRendererService deliveryInterfaceEmailTemplateRendererServiceMock = Mock()
    DeliveryInterfaceTemplateEmailInfo emailInfoMock = Mock()
    MimeMessage mimeMessageMock = Mock()
    def setup() {
        testObj = new DeliveryInterfaceEmailSender(javaMailSender:javaMailSenderMock,deliveryInterfaceEmailTemplateRendererService:deliveryInterfaceEmailTemplateRendererServiceMock)
    }

    def "test send email"(){

        given:
        javaMailSenderMock.createMimeMessage() >>mimeMessageMock

        when:
        testObj.sendEmail(emailInfoMock)

        then:
        1*emailInfoMock.setMimeMessage(mimeMessageMock)
    }
}
