eventCompileStart = { target ->
    compileAST(postgresqlExtensionsPluginDir, classesDirPath)
}

def compileAST(def srcBaseDir, def destDir) {
    ant.sequential {
        eventListener.triggerEvent("StatusUpdate", "Precompiling AST Transformations...")
        eventListener.triggerEvent("StatusUpdate", "src ${srcBaseDir} ${destDir}")
        path id: "grails.compile.classpath", compileClasspath
        def classpathId = "grails.compile.classpath"
        mkdir dir: destDir
        groovyc(destdir: destDir,
            srcDir: "$srcBaseDir/src/groovy/net/kaleidos/hibernate/postgresql/hstore",
            classpathref: classpathId,
            verbose: grailsSettings.verboseCompile,
            stacktrace: "yes",
            encoding: "UTF-8")
        eventListener.triggerEvent("Done precompiling AST Transformations!")
    }
}
