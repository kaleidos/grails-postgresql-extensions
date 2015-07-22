package net.kaleidos.hibernate.postgresql.hstore

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.FIELD])
@GroovyASTTransformationClass(["net.kaleidos.hibernate.postgresql.hstore.HstoreASTTransformation"])
public @interface Hstore {
}