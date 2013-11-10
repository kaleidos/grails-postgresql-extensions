eventCompileStart = { target ->
    compileAST(postgresqlExtensionsPluginDir, classesDirPath)
}

def compileAST(def srcBaseDir, def destDir) {
    ant.sequential {
        println "Precompiling AST Transformations ..."
        println "src ${srcBaseDir} ${destDir}"
        path id: "grails.compile.classpath", compileClasspath
        def classpathId = "grails.compile.classpath"
        mkdir dir: destDir
        groovyc(destdir: destDir,
            srcDir: "$srcBaseDir/src/groovy/net/kaleidos/hibernate/postgresql/hstore",
            classpathref: classpathId,
            verbose: grailsSettings.verboseCompile,
            stacktrace: "yes",
            encoding: "UTF-8")
        println "Done precompiling AST Transformations!"
    }
}
