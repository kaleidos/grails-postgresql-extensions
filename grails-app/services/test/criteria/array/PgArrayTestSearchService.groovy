package test.criteria.array

class PgArrayTestSearchService {

    static transactional = false

    List<Like> search(String field, String criteriaName, value) {
        Like.withCriteria {
            "${criteriaName}" field, value
        }
    }

    List<Like> search(String field, String criteriaName) {
        Like.withCriteria {
            "${criteriaName}" field
        }
    }

    List<User> searchWithJoin(String field, String criteriaName, value) {
        User.withCriteria {
            like {
                "${criteriaName}" field, value
            }
        }
    }

    List<User> searchWithJoin(String field, String criteriaName) {
        User.withCriteria {
            like {
                "${criteriaName}" field
            }
        }
    }

    List<User> searchWithJoinByStringOrInteger(Map params, String criteriaName) {
        User.withCriteria {
            like {
                or {
                    params.each { entry->
                        "${criteriaName}" (entry.key, entry.value)
                    }
                }
            }
        }
    }

    List<User> searchWithJoinAnd(Map params, String criteriaName) {
        User.withCriteria {
            like {
                and {
                    params.each { entry->
                        "${criteriaName}" (entry.key, entry.value)
                    }
                }
            }
        }
    }
}
