package test.criteria.array

class PgArrayTestSearchService {

    static transactional = false

    List<Like> search(String field, String criteriaName, Object value) {
        Like.withCriteria {
            "${criteriaName}" field, value
        }
    }

    List<Like> search(String field, String criteriaName) {
        Like.withCriteria {
            "${criteriaName}" field
        }
    }

    List<User> searchWithJoin(String field, String criteriaName, Object value) {
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

    List<User> searchWithJoinByStringOrInteger(String criteriaName, Object value1, Object value2) {
        User.withCriteria {
            like {
                or {
                    "${criteriaName}" 'favoriteMovies', value1
                    "${criteriaName}" 'favoriteNumbers', value2
                }
            }
        }
    }

    List<User> searchWithJoinAnd(String criteriaName, Object value1, Object value2) {
        User.withCriteria {
            like {
                and {
                    "${criteriaName}" 'favoriteMovies', value1
                    "${criteriaName}" 'favoriteNumbers', value2
                }
            }
        }
    }
}
