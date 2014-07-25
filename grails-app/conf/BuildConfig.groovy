grails.project.work.dir = "target/work"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    legacyResolve true // whether to do a secondary resolve on plugin installation, not advised but set here for backwards compatibility
    repositories {
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        runtime("org.postgresql:postgresql:9.2-1004-jdbc4") {
            export = false
        }

        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"

        compile 'com.google.code.gson:gson:2.2.4'

        // Coveralls plugin
        build 'org.apache.httpcomponents:httpcore:4.3.2'
        build 'org.apache.httpcomponents:httpclient:4.3.2'
        build 'org.apache.httpcomponents:httpmime:4.3.3'
    }

    plugins {
        build ":tomcat:$grailsVersion",
                ":release:2.2.0",
                ":rest-client-builder:1.0.3",
                ":coveralls:0.1.3", {
            export = false
        }

        test ":code-coverage:1.2.7", {
            export = false
        }

        test ":spock:0.7", {
            exclude "spock-grails-support"
        }

        compile ":guard:1.0.7", {
            export = false
        }

        runtime ":hibernate:$grailsVersion", {
            export = false
        }
    }
}
