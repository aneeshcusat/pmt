package com.walmart.deliveryinterface.email.velocity;

import static org.junit.Assert.*

import org.apache.velocity.app.VelocityEngine

import spock.lang.Specification

class VelocityInitServiceSpecification extends Specification {
    VelocityInitService testObj

    def setup() {
        testObj = new VelocityInitService()
    }

    def "return velocity engine" () {

        when:
        def result = testObj.getVelocityEngine()
        testObj.setTemplateDirPath("path")
        then:
        result instanceof VelocityEngine
        testObj.getTemplateDirPath() == "path"
    }
}
