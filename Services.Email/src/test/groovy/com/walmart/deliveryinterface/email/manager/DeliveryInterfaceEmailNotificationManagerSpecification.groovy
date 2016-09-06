package com.walmart.deliveryinterface.email.manager;

import static org.junit.Assert.*
import spock.lang.Specification

import com.walmart.deliveryinterface.configuration.DeliveryInterfaceApplicationConfiguration
import com.walmart.deliveryinterface.dashboard.manager.DeliveryInterfaceDashboardNotificationServices
import com.walmart.deliveryinterface.dataaccess.constants.OrderExceptionStates
import com.walmart.deliveryinterface.dataaccessobject.datatransferobject.DeliveryOrderItem
import com.walmart.deliveryinterface.email.DeliveryInterfaceEmailSender
import com.walmart.deliveryinterface.email.util.DeliveryInterfaceTemplateEmailInfo

class DeliveryInterfaceEmailNotificationManagerSpecification extends Specification {
    DeliveryInterfaceEmailNotificationManager testObj
    DeliveryInterfaceEmailSender deliveryInterfaceEmailSenderMock = Mock()
    DeliveryInterfaceTemplateEmailInfo deliveryInterfaceTemplateEmailInfoMock = Mock()
    DeliveryInterfaceApplicationConfiguration deliveryInterfaceApplicationConfigurationMock = Mock()
    DeliveryInterfaceDashboardNotificationServices dashboardNotificationServicesMock = Mock()

    Object objectMock = Mock()
    DeliveryOrderItem deliveryOrderItemMock = Mock()
    Date dateMock = Mock()

    def setup() {
        testObj = Spy()
        testObj.setDashboardNotificationServices(dashboardNotificationServicesMock)
        testObj.setDeliveryInterfaceApplicationConfiguration(deliveryInterfaceApplicationConfigurationMock)
        testObj.setDeliveryInterfaceEmailSender(deliveryInterfaceEmailSenderMock)
        testObj.setDeliveryInterfaceTemplateEmailInfo(deliveryInterfaceTemplateEmailInfoMock)
    }

    def "sendEmailNotificationForError - positive flow"() {
        given:
        deliveryInterfaceApplicationConfigurationMock.isEmailEnabed() >> true
        deliveryOrderItemMock.getLastModifiedDate() >> dateMock
        deliveryOrderItemMock.getVendorName() >> "vendor_name"
        deliveryOrderItemMock.getOrderId() >> "orderid"
        deliveryOrderItemMock.getClubId() >> "4969"
        deliveryOrderItemMock.getCreatedDate() >> dateMock
        deliveryInterfaceApplicationConfigurationMock.getHostName() >> "host"
        deliveryInterfaceApplicationConfigurationMock.getPortNumber() >> 4560
        deliveryInterfaceApplicationConfigurationMock.getProtocol() >> "http"

        when:
        testObj.sendEmailNotificationForError(OrderExceptionStates.FAILED_OMS_INTEGRATION,"",deliveryOrderItemMock)

        then:
        1 * deliveryInterfaceTemplateEmailInfoMock.setMailSubject(_)
        1 * deliveryInterfaceEmailSenderMock.sendEmail(_)
    }

    def "sendEmailNotificationForError - positive flow without datamap"() {
        Map <String,Object> dataMapMock
        given:
        deliveryInterfaceApplicationConfigurationMock.isEmailEnabed() >> true
        deliveryOrderItemMock.getLastModifiedDate() >> dateMock
        deliveryOrderItemMock.getVendorName() >> "vendor_name"
        deliveryOrderItemMock.getOrderId() >> "orderid"
        deliveryOrderItemMock.getClubId() >> "4969"
        deliveryOrderItemMock.getCreatedDate() >> dateMock
        deliveryInterfaceApplicationConfigurationMock.getHostName() >> "host"
        deliveryInterfaceApplicationConfigurationMock.getPortNumber() >> 4560
        deliveryInterfaceApplicationConfigurationMock.getProtocol() >> "http"

        when:
        testObj.sendEmailNotificationForError(OrderExceptionStates.FAILED_OMS_INTEGRATION,dataMapMock,deliveryOrderItemMock)

        then:
        1 * deliveryInterfaceTemplateEmailInfoMock.setMailSubject(_)
        1 * deliveryInterfaceEmailSenderMock.sendEmail(_)
        testObj.getDeliveryInterfaceApplicationConfiguration()
        testObj.getDeliveryInterfaceTemplateEmailInfo()
        testObj.getDeliveryInterfaceEmailSender()
        testObj.getDashboardNotificationServices()
    }
}
