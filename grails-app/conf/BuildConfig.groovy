grails.project.work.dir = "target/work"

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'
    legacyResolve true

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        runtime 'org.postgresql:postgresql:9.2-1004-jdbc4', {
            export = false
        }

        compile 'com.google.code.gson:gson:2.2.4'

        // Coveralls plugin
        build 'org.apache.httpcomponents:httpcore:4.3.2'
        build 'org.apache.httpcomponents:httpclient:4.3.2'
        build 'org.apache.httpcomponents:httpmime:4.3.3'
    }

    plugins {
        build ':release:3.0.1', ':rest-client-builder:2.0.3', {
            export = false
        }

        build ":coveralls:0.1.3", {
            export = false
        }

        test ":code-coverage:1.2.7", {
            export = false
        }

        compile ":guard:2.0.0", {
            export = false
        }

        runtime ":hibernate4:4.3.5.3", {
            export = false
        }
    }
}
