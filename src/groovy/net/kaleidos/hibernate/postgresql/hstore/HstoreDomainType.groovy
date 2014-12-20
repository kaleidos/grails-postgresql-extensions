package net.kaleidos.hibernate.postgresql.hstore

class HstoreDomainType {
    Map dataStore

    HstoreDomainType(Map data) {
        dataStore = data
    }

    def methodMissing(String name, args) {
        def method = Map.metaClass.getMetaMethod(name, args)
        return method.invoke(dataStore, args)
    }

    def propertyMissing(String name) {
        this.@dataStore[name]
    }

    def propertyMissing(String name, value) {
        this.@dataStore[name] = value
    }

    String toString() {
        dataStore.toString()
    }
}
