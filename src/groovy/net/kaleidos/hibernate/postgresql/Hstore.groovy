package net.kaleidos.hibernate.postgresql


class Hstore {

    // v1
    //Map map = [:]
    
    // v2
    @Delegate Map map

    

    public Hstore(Map map) {
        this.map = map
        println "A1"
    }
    // public Hstore() {
    //     println "A2"
    // }
}
