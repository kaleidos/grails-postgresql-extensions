package test.criteria

import java.util.List;

class PgIsContainedByCriteriaTestService {
    static transactional = true
    
        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByInteger(Integer number) {
            def result = Like.withCriteria {
                pgIsContainedBy 'favoriteNumbers', number
            }
    
            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByInteger(List<Integer> numberList) {
            def result = Like.withCriteria {
                pgIsContainedBy 'favoriteNumbers', numberList
            }
    
            return result
        }
        
        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByLong(Long number) {
            def result = Like.withCriteria {
                pgIsContainedBy 'favoriteLongNumbers', number
            }
    
            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByLong(List<Long> numberList) {
            def result = Like.withCriteria {
                pgIsContainedBy 'favoriteLongNumbers', numberList
            }
    
            return result
        }
        
        /**
         * Search for "likes" which elements are equals to the parameter
         */
        public List<Like> searchIsContainedByString(String movie) {
            def result = Like.withCriteria {
                pgIsContainedBy 'favoriteMovies', movie
            }
    
            return result
        }

        /**
         * Search for "likes" contained by the parameter
         */
        public List<Like> searchIsContainedByString(List<String> movieList) {
            def result = Like.withCriteria {
                pgIsContainedBy 'favoriteMovies', movieList
            }
    
            return result
        }
        
        /**
         * Search with a join
         */
        public List<User> searchIsContainedByWithJoin(List<String> movie) {
            def results = User.withCriteria {
                like {
                    pgIsContainedBy 'favoriteMovies', movie
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
                        pgIsContainedBy 'favoriteMovies', movies
                        pgIsContainedBy 'favoriteNumbers', numbers
                    }
                }
            }
    
            return results
        }
}
