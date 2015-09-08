package grails.postgresql.extensions

import grails.plugins.Plugin
import net.kaleidos.hibernate.postgresql.criteria.ArrayCriterias
import net.kaleidos.hibernate.postgresql.criteria.HstoreCriterias
import net.kaleidos.hibernate.postgresql.criteria.JsonCriterias
import net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType

class GrailsPostgresqlExtensionsGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.1 > *"
    // resources that are excluded from plugin packaging
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

    // URL to the plugin's documentation
    def documentation = "https://github.com/kaleidos/grails-postgresql-extensions/blob/master/README.md"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "Kaleidos", url: "http://kaleidos.net" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "Alonso Torres", email: "alonso.javier.torres@gmail.com" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GITHUB", url: "https://github.com/kaleidos/grails-postgresql-extensions/issues" ]

    // Online location of the plugin's browseable source code.
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

    void doWithDynamicMethods() {
        new ArrayCriterias()
        new HstoreCriterias()
        new JsonCriterias()
    }

    void doWithApplicationContext() {
        for (domainClass in grailsApplication.domainClasses) {
            decorateConstructor(domainClass.clazz.name, domainClass.metaClass)
        }
    }
}
