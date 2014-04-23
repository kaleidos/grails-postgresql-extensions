package test.criteria.array

class PgIsNotEmptyCriteriaTestService {
    static transactional = false

    /**
     * Search "non empty" integer arrays
     */
    public List<Like> searchNonEmptyIntegerArray() {
        def result = Like.withCriteria {
            pgArrayIsNotEmpty 'favoriteNumbers'
        }

        return result
    }

    /**
     * Search "non empty" long arrays
     */
    public List<Like> searchNonEmptyLongArray() {
        def result = Like.withCriteria {
            pgArrayIsNotEmpty 'favoriteLongNumbers'
        }

        return result
    }

    /**
     * Search "non empty" Float arrays
     */
    public List<Like> searchNonEmptyFloatArray() {
        def result = Like.withCriteria {
            pgArrayIsNotEmpty 'favoriteFloatNumbers'
        }

        return result
    }

    /**
     * Search "non empty" Double arrays
     */
    public List<Like> searchNonEmptyDoubleArray() {
        def result = Like.withCriteria {
            pgArrayIsNotEmpty 'favoriteDoubleNumbers'
        }

        return result
    }

    /**
     * Search "non empty" string arrays
     */
    public List<Like> searchNonEmptyStringArray() {
        def result = Like.withCriteria {
            pgArrayIsNotEmpty 'favoriteMovies'
        }

        return result
    }

    /**
     * Search "non empty" enum arrays
     */
    public List<Like> searchNonEmptyEnumArray() {
        def result = Like.withCriteria {
            pgArrayIsNotEmpty 'favoriteJuices'
        }

        return result
    }

    /**
     * Search "non empty" arrays with a join
     */
    public List<User> searchNonEmptyStringArrayWithJoin() {
        def results = User.withCriteria {
            like {
                pgArrayIsNotEmpty 'favoriteMovies'
            }
        }

        return results
    }


}
