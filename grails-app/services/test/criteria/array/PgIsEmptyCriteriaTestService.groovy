package test.criteria.array

class PgIsEmptyCriteriaTestService {
    static transactional = false

    /**
     * Search "empty" integer arrays
     */
    public List<Like> searchEmptyIntegerArray() {
        def result = Like.withCriteria {
            pgArrayIsEmpty 'favoriteNumbers'
        }

        return result
    }

    /**
     * Search "empty" long arrays
     */
    public List<Like> searchEmptyLongArray() {
        def result = Like.withCriteria {
            pgArrayIsEmpty 'favoriteLongNumbers'
        }

        return result
    }

    /**
     * Search "empty" string arrays
     */
    public List<Like> searchEmptyStringArray() {
        def result = Like.withCriteria {
            pgArrayIsEmpty 'favoriteMovies'
        }

        return result
    }

    /**
     * Search "empty" enum arrays
     */
    public List<Like> searchEmptyEnumArray() {
        def result = Like.withCriteria {
            pgArrayIsEmpty 'favoriteJuices'
        }

        return result
    }

    /**
     * Search "empty" arrays with a join
     */
    public List<User> searchEmptyStringArrayWithJoin() {
        def results = User.withCriteria {
            like {
                pgArrayIsEmpty 'favoriteMovies'
            }
        }

        return results
    }


}