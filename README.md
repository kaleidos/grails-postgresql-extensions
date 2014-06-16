Grails Postgresql Extensions
============================

[![Still maintained](http://stillmaintained.com/lmivan/grails-postgresql-extensions.png)](http://stillmaintained.com/lmivan/grails-postgresql-extensions)
[![Build Status](https://travis-ci.org/kaleidos/grails-postgresql-extensions.svg?branch=master)](https://travis-ci.org/kaleidos/grails-postgresql-extensions)
[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/kaleidos/grails-postgresql-extensions/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
[![Coverage Status](https://coveralls.io/repos/kaleidos/grails-postgresql-extensions/badge.png?branch=master)](https://coveralls.io/r/kaleidos/grails-postgresql-extensions?branch=master)

This is a grails plugin that provides hibernate user types to use postgresql native types such as arrays, hstores, json,... from a Grails application. It also provides new criterias to query this new native types.

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
        * [Is Empty or Contains](#is-empty-or-contains)
        * [Equals](#equals)
        * [Not Equals](#not-equals)
  * [Hstore](#hstore)
    * [Grails 2.2.5 and 2.3.1+](#grails-225-and-231)
    * [Old Grails versions](#old-grails-versions)
    * [Using Hstore](#using-hstore)
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

The plugin supports the definition of `Integer`, `Long`, `Float`, `Double`, `String`, and `Enum` arrays in your domain classes.

The EnumArrayType behaves almost identical to IntegerArrayType in that it stores and retrieves an array of ints. The difference, however, is that this is used with an Array of Enums, rather than Ints. The Enums are serialized to their ordinal value before persisted to the database. On retrieval, they are then converted back into their original Enum type.

```groovy
import net.kaleidos.hibernate.usertype.ArrayType

class Like {
    Integer[] favoriteNumbers = []
    Long[] favoriteLongNumbers = []
    Float[] favoriteFloatNumbers = []
    Double[] favoriteDoubleNumbers = []
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
        favoriteNumbers type:ArrayType, params: [type: Integer]
        favoriteLongNumbers type:ArrayType, params: [type: Long]
        favoriteFloatNumbers type:ArrayType, params: [type: Float]
        favoriteDoubleNumbers type:ArrayType, params: [type: Double]
        favoriteMovies type:ArrayType, params: [type: String]
        favoriteJuices type:ArrayType, params: [type: Juice]
    }
}
```

Now you can create domain objects using lists (or arrays) of integers, longs and strings and when you save the object it will be stored as an postgresql array:

```groovy
def like1 = new Like(favoriteNumbers:[5, 17, 9, 6],
                     favoriteLongNumbers:[123, 239, 3498239, 2344235],
                     favoriteFloatNumbers:[0.3f, 0.1f],
                     favoriteDoubleNumbers:[100.33d, 44.11d],
                     favoriteMovies:["Spiderman", "Blade Runner", "Starwars"],
                     favoriteJuices:[Like.Juice.ORANGE, Like.Juice.GRAPE])
like1.save()
```

And now, with `psql`:

```
=# select * from like;

 id |  favorite_long_numbers    |  favorite_float_numbers   |  favorite_double_numbers  |        favorite_movies                 | favorite_numbers | favorite_juices
----+---------------------------+---------------------------+---------------------------+----------------------------------------+------------------+----------------
  1 | {123,239,3498239,2344235} | {0.3,0.1}                 | {100.33,44.11}            | {Spiderman,"Blade Runner",Starwars}    | {5,17,9,6}       | {0,2}
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

#### Is Empty or Contains

This criteria is a mix of the `pgContains` and `pgIsEmpty`. Sometimes you have to execute 'pgContains' criteria if the list has elements or a 'pgIsEmpty' if the list is empty. It could be something like this:

```groovy
def numbers = ... // A list with zero or more elements
def result = Like.withCriteria {
    if (numbers) {
        pgArrayContains 'favoriteNumbers', numbers
    } else {
        pgArrayIsEmpty 'favoriteMovies'
    }
}
```

With `pgIsEmptyOrContains` you can write the previous code as follows:

```groovy
def numbers = ... // A list with zero or more elements
def result = Like.withCriteria {
    pgArrayIsEmptyOrContains 'favoriteNumbers', numbers
}
```

#### Equals

With this criteria you can get all the rows that are equal to a value. To use it just use the new criteria `pgArrayEquals`:

```groovy
def result = Like.withCriteria {
    pgArrayEquals 'favoriteNumbers', numbers
}
```

#### Not Equals

With this criteria you can get all the rows that are not equal to a value. To use it just use the new criteria `pgArrayNotEquals`:

```groovy
def result = Like.withCriteria {
    pgArrayNotEquals 'favoriteNumbers', numbers
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

#### Grails 2.2.5 and 2.3.1+

Depending on the version of Grails you're using, you have two different options to configure the mapping. For new versions of Grails you only have to define the domain class with a `Map` attribute and use the Hibernate user type `HstoreMapType`.

```groovy
import net.kaleidos.hibernate.usertype.HstoreMapType

class TestHstore {

    Map testAttributes

    static mapping = {
        testAttributes type: HstoreMapType
    }
}
```

#### Old Grails versions

Until [GRAILS-10335](http://jira.grails.org/browse/GRAILS-10335) has been fixed it hasn't been possible to override neither a Map, List, Set nor a Bag in a Domain class using a custom Hibernate user type.

If you define the previous domain class and execute `grails schema-export` you will get the following database schema:

```sql
create table test_hstore (id int8 not null, version int8 not null, primary key (id));
create table test_hstore_test_attributes (test_attributes int8, test_attributes_idx varchar(255), test_attributes_elt hstore not null);
```

As you can see, Grails creates another table with the key and the value. This is not what we can get because Postgresql provides the hstore type just for this.

So what can we do? We create the class [HstoreDomainType](https://github.com/kaleidos/grails-postgresql-extensions/blob/master/src/groovy/net/kaleidos/hibernate/postgresql/hstore/HstoreDomainType.groovy) to use it in domain classes instead of Map. This class, using Groovy Metaprogramming handles the `Map` type.
Now, with this AST Transformation `net.kaleidos.hibernate.postgresql.hstore.Hstore` (implemented [here](https://github.com/kaleidos/grails-postgresql-extensions/blob/master/src/groovy/net/kaleidos/hibernate/postgresql/hstore/HstoreASTTransformation.groovy)) we convert the properties with type `Map` to properties with type `HstoreDomainType`. And this is how the magic works :-)

Now if you annotate the property and execute again `grails schema-export` you'll get the right ddl (note the `hstore` type for the property `test_attribute`):

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
        testAttributes type: HstoreType
    }
}
```

```sql
create table test_hstore (id int8 not null, version int8 not null, test_attributes hstore not null, primary key (id));
```

Note that you only have to define the property as `Map`, annotate with `@Hstore` and define the Hibernate user type `HstoreType`.

#### Using Hstore

Now you can create and instance of the domain class. Due to a limitation of the Hstore Postgresql type you can only store Strings as key and value.

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

Collaborations are appreciated :-)


Release Notes
-------------

* [0.9](https://github.com/kaleidos/grails-postgresql-extensions/issues?milestone=1) - 16/Jun/2014 - Add new array criterias: pgArrayEquals, pgArrayNotEquals
* 0.8.1 - 24/Apr/2014 - Fix NPE when array is null.
* 0.8 - 24/Apr/2014 - Added support for Double and Float arrays. Refactored the ArrayType to be used as a parametrized type.
* 0.7 - Unreleased - New HstoreMapType and update plugin to Grails 2.2.5.
* 0.6.8 - 22/Apr/2014 - Fix NPE in HstoreType.
* 0.6.7 - 14/Feb/2014 - Support Java Arrays in criterias.
* 0.6.6 - 14/Feb/2014 - New criteria pgArrayIsEmptyOrContains.
* 0.6.5 - 13/Feb/2014 - Fix bug deleting instances with Hstore type. Thanks to Manuel Unno Vio!
* 0.6.4 - 30/Jan/2014 - Convert automatically the keys of Hstore to string.
* 0.6.3 - 19/Jan/2014 - Display the class name during startup when detecting a hstore property.
* 0.6.2 - Unreleased - Refactor some tests.
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

