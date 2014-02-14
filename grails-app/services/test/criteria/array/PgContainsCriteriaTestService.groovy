package test.criteria.array

class PgContainsCriteriaTestService {
    static transactional = false

    /**
     * Search "likes" with integer in array
     */
    public List<Like> searchWithCriteriaIntegerArray(Integer number) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with long in array
     */
    public List<Like> searchWithCriteriaLongArray(Long number) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with string in array
     */
    public List<Like> searchWithCriteriaStringArray(String movie) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search "likes" with enum in array
     */
    public List<Like> searchWithCriteriaEnumArray(Like.Juice juice) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteJuices', juice
        }

        return result
    }


    /**
     * Search "likes" with n integers in array
     */
    public List<Like> searchWithCriteriaIntegerArray(List<Integer> number) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with n longs in array
     */
    public List<Like> searchWithCriteriaLongArray(List<Long> number) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with n strings in array
     */
    public List<Like> searchWithCriteriaStringArray(List<String> movie) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search "likes" with n enums in array
     */
    public List<Like> searchWithCriteriaEnumArray(List<Like.Juice> juice) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteJuices', juice
        }

        return result
    }

    /**
     * Search "likes" with array of integer in array
     */
    public List<Like> searchWithCriteriaIntegerArray(Integer[] number) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with array of long in array
     */
    public List<Like> searchWithCriteriaLongArray(Long[] number) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search "likes" with array of string in array
     */
    public List<Like> searchWithCriteriaStringArray(String[] movie) {
        def result = Like.withCriteria {
            pgArrayContains 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search with a join
     */
    public List<User> searchStringWithJoin(String movie) {
        def results = User.withCriteria {
            like {
                pgArrayContains 'favoriteMovies', movie
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
                    pgArrayContains 'favoriteMovies', movie
                    pgArrayContains 'favoriteNumbers', number
                }
            }
        }

        return results
    }
}