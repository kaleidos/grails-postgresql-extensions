package test.criteria

class PgOverlapsCriteriaTestService {
    static transactional = false

    /**
     * Search overlaps "likes" with integer in array
     */
    public List<Like> overlapsIntegerArray(Integer number) {
        def result = Like.withCriteria {
            pgOverlaps 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search overlaps "likes" with n integers in array
     */
    public List<Like> overlapsIntegerArray(List<Integer> numbers) {
        def result = Like.withCriteria {
            pgOverlaps 'favoriteNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with long in array
     */
    public List<Like> overlapsLongArray(Long number) {
        def result = Like.withCriteria {
            pgOverlaps 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search overlaps "likes" with n longs in array
     */
    public List<Like> overlapsLongArray(List<Long> numbers) {
        def result = Like.withCriteria {
            pgOverlaps 'favoriteLongNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with string in array
     */
    public List<Like> overlapsStringArray(String movie) {
        def result = Like.withCriteria {
            pgOverlaps 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search overlaps "likes" with n strings in array
     */
    public List<Like> overlapsStringArray(List<String> movie) {
        def result = Like.withCriteria {
            pgOverlaps 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search overlaps with a join
     */
    public List<User> overlapsStringWithJoin(List<String> movies) {
        def results = User.withCriteria {
            like {
                pgOverlaps 'favoriteMovies', movies
            }
        }

        return results
    }

    /**
     * Search overlaps with a join and an 'or'
     */
    public List<User> overlapsStringOrIntergetWithJoin(List<String> movies, List<Integer> numbers) {
        def results = User.withCriteria {
            like {
                and {
                    pgOverlaps 'favoriteMovies', movies
                    pgOverlaps 'favoriteNumbers', numbers
                }
            }
        }

        return results
    }
}