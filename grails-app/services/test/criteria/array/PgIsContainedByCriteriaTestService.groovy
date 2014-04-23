package test.criteria.array

class PgIsContainedByCriteriaTestService {
    static transactional = false

        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByInteger(Integer number) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteNumbers', number
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByInteger(List<Integer> numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteNumbers', numbers
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByInteger(Integer[] numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteNumbers', numbers
            }

            return result
        }

        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByLong(Long number) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteLongNumbers', number
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByLong(List<Long> numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteLongNumbers', numbers
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByLong(Long[] numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteLongNumbers', numbers
            }

            return result
        }

        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByFloat(Float number) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteFloatNumbers', number
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByFloat(List<Float> numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteFloatNumbers', numbers
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByFloat(Float[] numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteFloatNumbers', numbers
            }

            return result
        }

        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByDouble(Double number) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteDoubleNumbers', number
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByDouble(List<Double> numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteDoubleNumbers', numbers
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByDouble(Double[] numbers) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteDoubleNumbers', numbers
            }

            return result
        }


        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByString(String movie) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteMovies', movie
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByString(List<String> movies) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteMovies', movies
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByString(String[] movies) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteMovies', movies
            }

            return result
        }

        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByEnum(Like.Juice juice) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteJuices', juice
            }

            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByEnum(List<Like.Juice> juices) {
            def result = Like.withCriteria {
                pgArrayIsContainedBy 'favoriteJuices', juices
            }

            return result
        }

        /**
         * Search with a join
         */
        public List<User> searchIsContainedByWithJoin(List<String> movie) {
            def results = User.withCriteria {
                like {
                    pgArrayIsContainedBy 'favoriteMovies', movie
                }
            }

            return results
        }

        /**
         * Search with a join and an 'or'
         */
        public List<User> searchIsContainedByStringOrInteger(List<String> movies, List<Integer> numbers) {
            def results = User.withCriteria {
                like {
                    or {
                        pgArrayIsContainedBy 'favoriteMovies', movies
                        pgArrayIsContainedBy 'favoriteNumbers', numbers
                    }
                }
            }

            return results
        }
}
