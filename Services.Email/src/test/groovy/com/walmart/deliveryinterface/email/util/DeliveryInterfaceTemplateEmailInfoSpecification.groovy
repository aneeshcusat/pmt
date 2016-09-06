package com.walmart.deliveryinterface.email.util;

import static org.junit.Assert.*

import javax.mail.internet.MimeMessage

import spock.lang.Specification

import com.walmart.deliveryinterface.configuration.DeliveryInterfaceApplicationConfiguration

class DeliveryInterfaceTemplateEmailInfoSpecification extends Specification {

    DeliveryInterfaceTemplateEmailInfo testObj
    MimeMessage messageMock = Mock()
    Map<String, Object> templateParametersMock = Mock()
    DeliveryInterfaceTemplateEmailInfo deliveryInterfaceTemplateEmailInfoMock = Mock()
    DeliveryInterfaceApplicationConfiguration deliveryInterfaceApplicationConfigurationMock = Mock()
    def setup(){
        testObj = new DeliveryInterfaceTemplateEmailInfo(deliveryInterfaceApplicationConfiguration:deliveryInterfaceApplicationConfigurationMock)
    }

    Map emptyMapMock = Mock()
    Map mapMock = new HashMap<String, String>()

    def "check setters and getters"(){
        given:
        deliveryInterfaceApplicationConfigurationMock.getConfigSettings() >> emptyMapMock

        when:
        testObj.setMailBcc("bcc")
        testObj.setMailCc("cc")
        testObj.setMailFrom("from")
        testObj.setMailSubject("subject")
        testObj.setMailTo("to")
        testObj.setMimeMessage(messageMock)
        testObj.setTemplateParameters(templateParametersMock)
        testObj.setVelocityTemplateName("templatename")
        testObj.copyPropertiesTo(deliveryInterfaceTemplateEmailInfoMock)

        then:
        testObj.getMailBcc() == "bcc"
        testObj.getMailCc() == "cc"
        testObj.getMailFrom() == "from"
        testObj.getMailSubject() == "subject"
        testObj.getMailTo() == "to"
        testObj.getMimeMessage() == messageMock
        testObj.getTemplateParameters() == templateParametersMock
        testObj.getVelocityTemplateName() == "templatename"
    }

    def "check setters and getters if the value is coming form configuration"(){
        given:
        mapMock.put("emailFrom", "emailFrom")
        mapMock.put("emailTo", "emailTo")
        mapMock.put("emailCc", "emailCc")
        mapMock.put("emailBcc", "emailBcc")
        mapMock.put("emailSubject", "emailSubject")
        deliveryInterfaceApplicationConfigurationMock.getConfigSettings() >> mapMock

        when:
        testObj.setMailBcc("bcc")
        testObj.setMailCc("cc")
        testObj.setMailFrom("from")
        testObj.setMailSubject("subject")
        testObj.setMailTo("to")
        testObj.setMimeMessage(messageMock)
        testObj.setTemplateParameters(templateParametersMock)
        testObj.setVelocityTemplateName("templatename")
        testObj.copyPropertiesTo(deliveryInterfaceTemplateEmailInfoMock)

        then:
        testObj.getMailBcc() == "emailBcc"
        testObj.getMailCc() == "emailCc"
        testObj.getMailFrom() == "emailFrom"
        testObj.getMailSubject() == "emailSubject"
        testObj.getMailTo() == "emailTo"
        testObj.getMimeMessage() == messageMock
        testObj.getTemplateParameters() == templateParametersMock
        testObj.getVelocityTemplateName() == "templatename"
    }
}
