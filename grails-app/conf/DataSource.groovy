dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of '', 'create', 'create-drop','update'
            driverClassName = "org.postgresql.Driver"
            dialect = "net.kaleidos.hibernate.PostgresqlExtensionsDialect"
            url = "jdbc:postgresql://localhost:5432/pg_extensions"
            username = "pg_extensions"
            password = "pg_extensions"
            loggingSql = true
        }
    }
    test {
        dataSource {
            dbCreate = "create-drop" // one of '', 'create', 'create-drop','update'
            driverClassName = "org.postgresql.Driver"
            dialect = "net.kaleidos.hibernate.PostgresqlExtensionsDialect"
            url = "jdbc:postgresql://localhost:5432/pg_extensions_test"
            username = "pg_extensions"
            password = "pg_extensions"
            loggingSql = true
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
