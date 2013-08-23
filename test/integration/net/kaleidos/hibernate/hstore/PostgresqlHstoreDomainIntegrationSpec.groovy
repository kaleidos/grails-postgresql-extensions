package net.kaleidos.hibernate.hstore

import grails.plugin.spock.*
import spock.lang.*

import test.hstore.TestHstore
import test.hstore.TestMap
import net.kaleidos.hibernate.postgresql.Hstore

class PostgresqlHstoreDomainIntegrationSpec extends IntegrationSpec {
    public static int D = 1
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
            testMap.pene.size() == D
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
    
//    private decorateConstructor(metaclass) {
//        def hstoreProperties = []
//        metaclass.properties.each { prop->
//            if (prop.type == net.kaleidos.hibernate.postgresql.Hstore) {
//                hstoreProperties << prop.name
//            }
//        }
//        
//        if (hstoreProperties.size() > 0) {
//            def constructor = metaclass.retrieveConstructor(Map)
//            println "Machacando el constructor ${constructor}"
//            metaclass.constructor = { Map m ->
//                hstoreProperties.each { name->
//                    m[name] = new net.kaleidos.hibernate.postgresql.Hstore(m[name])
//                }
//                return constructor.newInstance(m)
//            }
//        }
//    }
    
    @IgnoreRest 
    void 'save a domain class with a map 2'() {
        setup:
//            def zz1 = test.hstore.TestMap.class.getMetaClass()
//            def zz2 = Class.forName("test.hstore.TestMap").getMetaClass()
//            
//            println "1> ${zz1}"
//            println "2> ${zz2}"
//            
//            decorateConstructor(test.hstore.TestMap.metaClass)
//            decorateConstructor(org.codehaus.groovy.grails.commons.GrailsMetaClassUtils.getRegistry().getMetaClass(test.hstore.TestMap))
//            TestMap.doStuff()
            def testMap = new TestMap(testAttributes: data, kk: "badass")
            
            
            println " > " + testMap.testAttributes

        when:
            testMap.save()

        then:
            println testMap.errors
            testMap.hasErrors() == false
            testMap.testAttributes != null
            testMap.testAttributes.size() == 1
            testMap.testAttributes.foo == "bar"
            testMap.kk == "badass"
    
        where:
            data = [foo:"bar"]
    }

}
