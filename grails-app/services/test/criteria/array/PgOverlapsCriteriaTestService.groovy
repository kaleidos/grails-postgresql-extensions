package test.criteria.array

class PgOverlapsCriteriaTestService {
    static transactional = false

    /**
     * Search overlaps "likes" with integer in array
     */
    public List<Like> overlapsIntegerArray(Integer number) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteNumbers', number
        }

        return result
    }

    /**
     * Search overlaps "likes" with n integers in array
     */
    public List<Like> overlapsIntegerArray(List<Integer> numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with n integers in array
     */
    public List<Like> overlapsIntegerArray(Integer[] numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with long in array
     */
    public List<Like> overlapsLongArray(Long number) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteLongNumbers', number
        }

        return result
    }

    /**
     * Search overlaps "likes" with n longs in array
     */
    public List<Like> overlapsLongArray(List<Long> numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteLongNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with n longs in array
     */
    public List<Like> overlapsLongArray(Long[] numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteLongNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with float in array
     */
    public List<Like> overlapsFloatArray(Float number) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteFloatNumbers', number
        }

        return result
    }

    /**
     * Search overlaps "likes" with n float in array
     */
    public List<Like> overlapsFloatArray(List<Float> numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteFloatNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with n Floats in array
     */
    public List<Like> overlapsFloatArray(Float[] numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteFloatNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with Double in array
     */
    public List<Like> overlapsDoubleArray(Double number) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteDoubleNumbers', number
        }

        return result
    }

    /**
     * Search overlaps "likes" with n Doubles in array
     */
    public List<Like> overlapsDoubleArray(List<Double> numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteDoubleNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with n Doubles in array
     */
    public List<Like> overlapsDoubleArray(Double[] numbers) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteDoubleNumbers', numbers
        }

        return result
    }

    /**
     * Search overlaps "likes" with string in array
     */
    public List<Like> overlapsStringArray(String movie) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search overlaps "likes" with n strings in array
     */
    public List<Like> overlapsStringArray(List<String> movie) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search overlaps "likes" with n strings in array
     */
    public List<Like> overlapsStringArray(String[] movie) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteMovies', movie
        }

        return result
    }

    /**
     * Search overlaps "likes" with enum in array
     */
    public List<Like> overlapsEnumArray(Like.Juice juice) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteJuices', juice
        }

        return result
    }

    /**
     * Search overlaps "likes" with n enums in array
     */
    public List<Like> overlapsEnumArray(List<Like.Juice> juices) {
        def result = Like.withCriteria {
            pgArrayOverlaps 'favoriteJuices', juices
        }

        return result
    }

    /**
     * Search overlaps with a join
     */
    public List<User> overlapsStringWithJoin(List<String> movies) {
        def results = User.withCriteria {
            like {
                pgArrayOverlaps 'favoriteMovies', movies
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
                    pgArrayOverlaps 'favoriteMovies', movies
                    pgArrayOverlaps 'favoriteNumbers', numbers
                }
            }
        }

        return results
    }
}
