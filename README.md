Grails Postgresql Extensions
============================

This is a grails plugin to use postgresql native elements such as arrays, hstores, json,... from a Grails application. For this first version only support for arrays and some query methods has been implemented. More query methods and more types will be added in the future.

* [Installation](#installation)
* [Configuration](#configuration)
* [Native Types](#native-types)
  * [Arrays](#arrays)
    * [Criterias](#criterias)
        * [Contains](#contains)
        * [Is contained](#is-contained)
        * [Overlaps](#overlaps)
        * [Is Empty](#is-empty)
        * [Is Not Empty](#is-not-empty)
* [Authors](#authors)
* [Release Notes](#release-notes)


Installation
------------

In `BuildConfig` and:

```groovy
compile ":postgresql-extensions:0.1"
```

Configuration
-------------

After install the plugin you have to use a new Postgresql Hibernate Dialect in your application. Add it to the `DataSource.groovy` file:

```groovy
development {
    dataSource {
        dbCreate = ""
        driverClassName = "org.postgresql.Driver"
        dialect = "net.kaleidos.hibernate.PostgresqlExtensionsDialect"
        url = "jdbc:postgresql://localhost:5432/db-name"
        username = "user"
        password = "password"
    }
}
```

If you just only add the dialect, hibernate will create a new sequence for every table to generate the sequential ids instead of a global sequence for all your tables.


## Native Types

### Arrays

The plugin supports the definition of `Integer`, `Long`, `String`, and `Enum` arrays in your domain classes.

The EnumArrayType behaves almost identical to IntegerArrayType in that it stores and retrieves an array of ints. The difference, however, is that this is used with an Array of Enums, rather than Ints. The Enums are serialized to their ordinal value before persisted to the database. On retrieval, they are then converted back into their original Enum type.

```groovy
import net.kaleidos.hibernate.usertype.IntegerArrayType
import net.kaleidos.hibernate.usertype.LongArrayType
import net.kaleidos.hibernate.usertype.StringArrayType
import net.kaleidos.hibernate.usertype.IdentityEnumArrayType

class Like {
    Integer[] favoriteNumbers = []
    Long[] favoriteLongNumbers = []
    String[] favoriteMovies = []
    Juice[] favoriteJuices = []

    static enum Juice {
        ORANGE(0),
        APPLE(1),
        GRAPE(2)

        private final int value
        Juice(int value)  { this.value = value }
        public int value() { return this.value }
        public int getId() { return this.value }

        public String getName() { this.toString() }

        // Enum Constructors are not publicly visible
        // so we have to loop through the values and find the desired one
        public static withId(int value) { Juice.values().find { it.value() == value } }
    }

    static mapping = {
        favoriteNumbers type:IntegerArrayType
        favoriteLongNumbers type:LongArrayType
        favoriteMovies type:StringArrayType
        favoriteJuices type:IdentityEnumArrayType, params:[enumClass: Juice]
    }
}
```

Now you can create domain objects using lists of integers, longs and strings and when you save the object it will be stored as an postgresql array:

```groovy
def like1 = new Like(favoriteNumbers:[5, 17, 9, 6],
                     favoriteLongNumbers:[123, 239, 3498239, 2344235],
                     favoriteMovies:["Spiderman", "Blade Runner", "Starwars"],
                     favoriteJuices:[Like.Juice.ORANGE, Like.Juice.GRAPE])
like1.save()
```

And now, with `psql`:

```
=# select * from like;

 id |  favorite_long_numbers    |        favorite_movies                 | favorite_numbers | favorite_juices
----+-------------------------- +----------------------------------------+------------------+----------------
  1 | {123,239,3498239,2344235} | {Spiderman,"Blade Runner",Starwars}    | {5,17,9,6}       | {0,2}
```

#### Criterias

The plugin also include some hibernate criterias to use in your queries. Please check the [services](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/grails-app/services/test/criteria/array) and the [tests](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/test/integration/net/kaleidos/hibernate/array) created to see all usage examples.
You can also check the official [Postgresql Array operators](http://www.postgresql.org/docs/9.2/static/functions-array.html#ARRAY-OPERATORS-TABLE).

##### Contains

With this criteria you can get all the rows that contains all the values in the array field. To use it just use the new criteria `pgArrayContains`:

```groovy
// number can be just a value...
def number = 3
def result = Like.withCriteria {
    pgArrayContains 'favoriteNumbers', number
}

// ...or a list
def numbers = [5, 17]
def result = Like.withCriteria {
    pgArrayContains 'favoriteNumbers', numbers
}

// If using enums, pass the enum right through
def juices = Like.Juice.ORANGE
def result = Like.withCriteria {
    pgArrayContains 'favoriteJuices', juices
}

```

#### Is contained

With this criteria you can get all the rows that are contained by the values. To use it just use the new criteria `pgArrayIsContainedBy`:

```groovy
// movie can be just a string or a list
def movie = "Starwars" // or movie = ["Starwars"]
def result = Like.withCriteria {
    pgArrayIsContainedBy 'favoriteMovies', movie
}

// The plugin also support joins
def movies = ["Starwars", "Matrix"]
def results = User.withCriteria {
    like {
        pgArrayIsContainedBy 'favoriteMovies', movies
    }
}
```

#### Overlaps

With this criteria you can get all the rows that contains any of the values. To use it just use the new criteria `pgArrayOverlaps`:

```groovy
def result = Like.withCriteria {
    pgArrayOverlaps 'favoriteNumbers', numbers
}
```

#### Is Empty

With this criteria you can get all the rows that contains an-empty array in the selected field. To use it just use the new criteria `pgArrayIsEmpty`:

```groovy
def result = Like.withCriteria {
    pgArrayIsEmpty 'favoriteMovies'
}
```

#### Is Not Empty

With this criteria you can get all the rows that contains a not empty array in the selected field. To use it just use the new criteria `pgArrayIsNotEmpty`:

```groovy
def result = Like.withCriteria {
    pgArrayIsNotEmpty 'favoriteMovies'
}
```



Authors
-------

You can send any questions to:

- Iván López: lopez.ivan@gmail.com
- Alonso Torres: alonso.javier.torres@gmail.com
- Matt Feury: mattfeury@gmail.com

Collaborations are appreciated :-)


Release Notes
-------------

* 0.1.1 - 22/Jul/2013 - Some refactors of the code. No functionality added.
* 0.1 - 16/Jul/2013 - Initial version of the plugin with support for integer, long and string array types and criterias pgArrayContains, pgArrayIsContainedBy, pgArrayOverlaps, pgArrayIsEmpty and pgArrayIsNotEmpty.

