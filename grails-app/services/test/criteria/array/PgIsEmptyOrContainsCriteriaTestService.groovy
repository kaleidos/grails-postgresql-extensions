package test.criteria.array

class PgIsEmptyOrContainsCriteriaTestService {
    static transactional = false

    /**
     * Search "likes" with n integers in array
     */
    public List<Like> searchWithCriteriaIntegerArray(List<Integer> number) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with n longs in array
     */
    public List<Like> searchWithCriteriaLongArray(List<Long> number) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with n strings in array
     */
    public List<Like> searchWithCriteriaStringArray(List<String> movie) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search "likes" with n enums in array
     */
    public List<Like> searchWithCriteriaEnumArray(List<Like.Juice> juice) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteJuices', juice
        }

        return result
    }

    /**
     * Search with a join
     */
    public List<User> searchStringWithJoin(List<String> movies) {
        def results = User.withCriteria {
            like {
                pgArrayIsEmptyOrContains 'favoriteMovies', movies
            }
        }

        return results
    }
}