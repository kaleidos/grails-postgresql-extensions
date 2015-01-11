dataSource {
    pooled = true
    dbCreate = 'none'
    dialect = 'net.kaleidos.hibernate.PostgresqlExtensionsDialect'
    driverClassName = 'org.postgresql.Driver'
    username = 'pg_extensions'
    password = 'pg_extensions'
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
}

environments {
    development {
        dataSource {
            url = 'jdbc:postgresql://localhost/pg_extensions'
            logSql = true
        }
    }
    test {
        dataSource {
            dbCreate = 'create-drop'
            url = 'jdbc:postgresql://localhost/pg_extensions_test'
            logSql = false
        }
    }
}
