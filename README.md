Grails Postgresql Extensions
============================

[![Still maintained](http://stillmaintained.com/lmivan/grails-postgresql-extensions.png)](http://stillmaintained.com/lmivan/grails-postgresql-extensions)
[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/lmivan/grails-postgresql-extensions/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

This is a grails plugin to use postgresql native elements such as arrays, hstores, json,... from a Grails application.

Currently the plugin supports arrays and hstore and some query methods has been implemented. More native types and query methods will be added in the future.

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
  * [Hstore](#hstore)
    * [Criterias](#hstore-criterias)
        * [Contains Key](#contains-key)
        * [Contains](#contains-1)
        * [Is Contained](#is-contained-1)
* [Authors](#authors)
* [Release Notes](#release-notes)


Installation
------------

In `BuildConfig` and:

```groovy
compile (":postgresql-extensions:<version>") {
    excludes "hibernate"
}
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

### Hstore

The first thing you need to do is install hstore support in Postgresql. In Debian/Ubuntu you have to install the `postgresql-contrib` package:

```
sudo apt-get install postgresql-contrib-9.2
```

Once the package is installed in the system you have to create the extension in the database you want to use hstore into:

```
CREATE EXTENSION hstore;
```

You can test that the hstore extension is correctly installed running:

```
=# SELECT 'foo=>bar, xxx=>yyy'::hstore;
           hstore
----------------------------
 "foo"=>"bar", "xxx"=>"yyy"
(1 row)
```

Now you can create a domain class

```groovy
import net.kaleidos.hibernate.postgresql.hstore.Hstore
import net.kaleidos.hibernate.usertype.HstoreType

class TestHstore {

    @Hstore
    Map testAttributes

    String anotherProperty

    static constrains = {
        anotherProperty nullable: false
    }

    static mapping = {
        testAttributes type:HstoreType
    }
}
```

Note that you only have to define the property as `Map`, annotate with `@Hstore` and define the Hibernate user type `HstoreType`. Now you can create and instance of the domain class. Due to a limitation of the Hstore Postgresql type you can only store Strings as key and value.

```groovy
def instance = new TestHstore(testAttributes:[foo:"bar"], anotherProperty:"Groovy Rocks!")
instance.save()

def instance2 = new TestHstore(testAttributes:[xxx:1, zzz:123], anotherProperty:"")
instance2.save()
```


```
=# select * from test_hstore;
 id | version | another_property | test_attributes
----+---------+------------------+-----------------
  1 |       0 | Groovy Rocks!    | "foo"=>"bar"
  2 |       0 |                  | "xxx"=>"1", "zzz"=>"123"
```


#### HSTORE Criterias

We have added the following criteria operations to query rows using the Hstore custom type. You can
check the [services](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/grails-app/services/test/criteria/hstore)
and the [tests](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/test/integration/net/kaleidos/hibernate/hstore)
to help you to developp your own criterias.

You can also check the official [Postgresql Hstore operators](http://www.postgresql.org/docs/9.0/static/hstore.html).

##### Contains Key

With this operation you can search for rows that contains an Hstore with the key passd as parameter.

```groovy
def wantedKey = "my-custom-key"
def result = MyDomain.withCriteria {
    pgHstoreContainsKey "attributes", wantedKey
}
```

##### Contains

You can search for data that contains certain pairs of (key,value)
```groovy
def result = Users.withCriteria {
    pgHstoreContains 'configuration', ["language": "es"]
}
```

##### Is Contained

The operation is contained can be used when looking for rows that has all the elements in the map
passed as parameter.

```groovy
def result = TestHstore.withCriteria {
    pgHstoreIsContained 'testAttributes', ["1" : "a", "2" : "b"]
}
```
The example above returns the rows that contains elements like:
```
testAttributes = ["1" : "a"]
testAttributes = ["2" : "b"]
testAttributes = ["1" : "a", "2" : "b"]
```
This criteria can also be used to look for exact matches

Authors
-------

You can send any questions to:

- Iván López: lopez.ivan@gmail.com ([@ilopmar](https://twitter.com/ilopmar))
- Alonso Torres: alonso.javier.torres@gmail.com ([@alotor](https://twitter.com/alotor))
- Matt Feury: mattfeury@gmail.com ([@soundandfeury](https://twitter.com/soundandfeury))

Collaborations are appreciated :-)


Release Notes
-------------

* 0.6.2 - Unreleased - Refactor some tests
* 0.6.1 - 28/Nov/2013 - Update postgresql jdbc driver to version 9.2 and do not export hibernate plugin.
* 0.6 - 21/Nov/2013 - Use a more complete Hstore parser. Thanks to Moritz Kobel!
* 0.5.1 - 10/Nov/2013 - Change base directory to compile AST before the plugin classes. Thanks to Moritz Kobel!
* 0.5 - 08/Nov/2013 - Add criteria operation for Hstore types.
* 0.4.1 - Unreleased - Compile AST before the project itself.
* 0.4 - 28/Oct/2013 - Add support to Hstore. It's only possible to save and get, but no queries has been implemented.
* 0.3 - 18/Sep/2013 - Add support to define the schema name for the sequences.
* 0.2 - 25/Aug/2013 - Support for arrays of Enums with automatic serialization/deserialization to ordinal integer value. Thanks to Matt Feury!
* 0.1.1 - 22/Jul/2013 - Some refactors of the code. No functionality added.
* 0.1 - 16/Jul/2013 - Initial version of the plugin with support for integer, long and string array types and criterias pgArrayContains, pgArrayIsContainedBy, pgArrayOverlaps, pgArrayIsEmpty and pgArrayIsNotEmpty.

