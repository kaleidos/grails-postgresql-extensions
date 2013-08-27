package net.kaleidos.hibernate.postgresql.hstore

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class HstoreASTTransformation extends AbstractASTTransformation {

    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (!nodes) return
        if (!nodes[0] || !nodes[1]) return
        if (!(nodes[0] instanceof AnnotationNode)) return

        FieldNode field = nodes[1]
        if (field.originType.typeClass.name != java.util.Map.class.name) {
            sourceUnit.addError(new SyntaxException("It is only possible to annotate Map fields with @Hstore annotation", nodes[0].lineNumber + 1, nodes[0].columnNumber))
        } else {
            ClassNode classOwner = field.owner
            println "[PostgresqlExtensions] Converting ${classOwner}.${field.name} to Hstore type" 
            field.type = new ClassNode(net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType)
        }
    }
}