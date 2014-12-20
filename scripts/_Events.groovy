eventCompileStart = { target ->
    compileAST(postgresqlExtensionsPluginDir, classesDirPath)
}

void compileAST(srcBaseDir, destDir) {
    ant.sequential {
        eventListener.triggerEvent("StatusUpdate", "Precompiling AST Transformations...")
        eventListener.triggerEvent("StatusUpdate", "src $srcBaseDir $destDir")

        String classpathId = "grails.compile.classpath"

        path id: classpathId, compileClasspath

        mkdir dir: destDir
        groovyc(
            destdir: destDir,
            srcDir: "$srcBaseDir/src/groovy/net/kaleidos/hibernate/postgresql/hstore",
            classpathref: classpathId,
            verbose: grailsSettings.verboseCompile,
            stacktrace: "yes",
            encoding: "UTF-8")

        eventListener.triggerEvent("Done precompiling AST Transformations!")
    }
}
