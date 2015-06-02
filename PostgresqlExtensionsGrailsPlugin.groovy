import net.kaleidos.hibernate.postgresql.criteria.ArrayCriterias
import net.kaleidos.hibernate.postgresql.criteria.HstoreCriterias
import net.kaleidos.hibernate.postgresql.criteria.JsonCriterias
import net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType

class PostgresqlExtensionsGrailsPlugin {
    def version = "4.5.0"
    def grailsVersion = "2.0 > *"
    def pluginExcludes = [
        "grails-app/controllers/**",
        "grails-app/domain/**",
        "grails-app/i18n/**",
        "grails-app/services/**",
        "grails-app/taglib/**",
        "grails-app/utils/**",
        "grails-app/views/**",
        "web-app/**"
    ]

    def title = "Grails Postgresql Extensions Plugin"
    def author = "Iván López"
    def authorEmail = "lopez.ivan@gmail.com"
    def description = '''\
Provides Hibernate user types to support for Postgresql Native Types like Array, HStore, JSON, JSONB,... as well as new criterias to query this native types
'''

    def documentation = "https://github.com/kaleidos/grails-postgresql-extensions/blob/master/README.md"
    def license = "APACHE"
    def organization = [ name: "Kaleidos", url: "http://kaleidos.net" ]
    def developers = [ [ name: "Alonso Torres", email: "alonso.javier.torres@gmail.com" ]]
    def issueManagement = [ system: "GITHUB", url: "https://github.com/kaleidos/grails-postgresql-extensions/issues" ]
    def scm = [ url: "https://github.com/kaleidos/grails-postgresql-extensions" ]

    // TODO: Extract to utils class or service
    private decorateConstructor(className, metaclass) {
        def hstoreProperties = []
        metaclass.properties.each { prop->
            if (prop.type == HstoreDomainType) {
                println "[PostgresqlExtensions] Adding property ${className}.${prop.name} as a hstore property"
                hstoreProperties << prop.name
            }
        }

        if (hstoreProperties.size() > 0) {
            def constructor = metaclass.retrieveConstructor(Map)
            metaclass.constructor = { Map m ->
                hstoreProperties.each { name->
                    m[name] = new HstoreDomainType(m[name])
                }
                return constructor.newInstance(m)
            }
        }
    }

    def doWithDynamicMethods = { ctx ->
        new ArrayCriterias()
        new HstoreCriterias()
        new JsonCriterias()
    }

    def doWithApplicationContext = { ctx ->
        for (domainClass in application.domainClasses) {
            decorateConstructor(domainClass.clazz.name, domainClass.metaClass)
        }
    }
}
