package net.kaleidos.hibernate.hstore

import grails.plugin.spock.*
import spock.lang.*

import test.hstore.TestHstore
import test.hstore.TestMap
import net.kaleidos.hibernate.postgresql.Hstore

class PostgresqlHstoreDomainIntegrationSpec extends IntegrationSpec {

    void 'save a domain class with a map'() {
        setup:
            def testMap = new TestHstore(kk:"hola", pene:data)
            // testMap.doc = data

            // Why the following lines don't work?
            // def testMap = new TestHstore(doc:data)
            // def testMap = new TestHstore("doc.map":data)

        when:
            testMap.save()

        then:
            testMap.hasErrors() == false
            testMap.pene.size() == 1
            testMap.pene.foo == "bar"
            testMap.kk == "hola"

        where:
            data = [foo:"bar"]
    }

    void 'save a domain class with a null map'() {
        setup:
            def testMap = new TestHstore()
            testMap.doc = [foo:"bar"]
            testMap.doc2 = null

        when:
            testMap.save()

        then:
            testMap.hasErrors() == false
            testMap.doc2 == null
    }
    @IgnoreRest 
    void 'save a domain class with a map 2'() {
        setup:
            // OK!!! v1
            //  def hst = new Hstore(map:data)
            //  def testMap = new TestMap(hstore:hst)
            
            // OK!!! v1
            //def testMap = new TestMap(hstore:new Hstore(map:data))

            // v2
            def testMap = new TestMap(testAttributes: new Hstore(data))
            println " > " + testMap.testAttributes

        when:
            testMap.save()

        then:
            // v1
//            testMap.hasErrors() == false
//            testMap.hstore != null
//            testMap.hstore.map.size() == 1
//            testMap.hstore.map.foo == "bar"
        
            // v2
            println testMap.errors
            testMap.hasErrors() == false
            testMap.testAttributes != null
            testMap.testAttributes.size() == 1
            testMap.testAttributes.foo == "bar"
    
        where:
            data = [foo:"bar"]
    }

}
