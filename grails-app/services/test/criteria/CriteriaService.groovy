package test.criteria

class CriteriaService {
    static transactional = true

    /**
     * Search "likes" with integer in array
     */
    public List<Like> searchWithCriteriaIntegerArray(Integer number) {
        def result = Like.withCriteria {
            pgContains 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with long in array
     */
    public List<Like> searchWithCriteriaLongArray(Long number) {
        def result = Like.withCriteria {
            pgContains 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with string in array
     */
    public List<Like> searchWithCriteriaStringArray(String movie) {
        def result = Like.withCriteria {
            pgContains 'favoriteMovies', movie
        }

        return result
    }
    
    /**
     * Search "likes" with n integers in array
     */
    public List<Like> searchWithCriteriaIntegerArray(List<Integer> number) {
        def result = Like.withCriteria {
            pgContains 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with n longs in array
     */
    public List<Like> searchWithCriteriaLongArray(List<Long> number) {
        def result = Like.withCriteria {
            pgContains 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with n strings in array
     */
    public List<Like> searchWithCriteriaStringArray(List<String> movie) {
        def result = Like.withCriteria {
            pgContains 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search with a join
     */
    public List<User> searchStringWithJoin(String movie) {
        def results = User.withCriteria {
            like {
                pgContains 'favoriteMovies', movie
            }
        }

        return results
    }

    /**
     * Search with a join and an 'or'
     */
    public List<User> searchStringOrIntergetWithJoin(String movie, Integer number) {
        def results = User.withCriteria {
            like {
                or {
                    pgContains 'favoriteMovies', movie
                    pgContains 'favoriteNumbers', number
                }
            }
        }

        return results
    }
}