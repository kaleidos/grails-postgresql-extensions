package test.criteria.array

class PgIsEmptyOrContainsCriteriaTestService {
    static transactional = false

    /**
     * Search "likes" with n integers in array
     */
    public List<Like> searchWithCriteriaIntegerArray(List<Integer> numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n integers in array
     */
    public List<Like> searchWithCriteriaIntegerArray(Integer[] numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n longs in array
     */
    public List<Like> searchWithCriteriaLongArray(List<Long> numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteLongNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n longs in array
     */
    public List<Like> searchWithCriteriaLongArray(Long[] numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteLongNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n floats in array
     */
    public List<Like> searchWithCriteriaFloatArray(List<Float> numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteFloatNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n floats in array
     */
    public List<Like> searchWithCriteriaFloatArray(Float[] numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteFloatNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n doubles in array
     */
    public List<Like> searchWithCriteriaDoubleArray(List<Double> numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteDoubleNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n longs in array
     */
    public List<Like> searchWithCriteriaDoubleArray(Double[] numbers) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteDoubleNumbers', numbers
        }

        return result
    }

    /**
     * Search "likes" with n strings in array
     */
    public List<Like> searchWithCriteriaStringArray(List<String> movies) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteMovies', movies
        }

        return result
    }

    public List<Like> searchWithCriteriaStringArray(String[] movies) {
        def result = Like.withCriteria {
            pgArrayIsEmptyOrContains 'favoriteMovies', movies
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
