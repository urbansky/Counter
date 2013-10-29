dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
  development {
    dataSource {
      pooled = true
      driverClassName = "org.h2.Driver"
      username = "sa"
      password = ""
      dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
      url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
    }
  }
  test {
      dataSource {
          dbCreate = "update"
          url = "jdbc:hsqldb:mem:testDb"
      }
  }
  production {
      dataSource {
          dbCreate = "update"
          url = "jdbc:hsqldb:file:prodDb;shutdown=true"
      }
  }
}
