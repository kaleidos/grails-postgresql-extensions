# Grails Postgresql Extensions

[![Still maintained](http://stillmaintained.com/lmivan/grails-postgresql-extensions.png)](http://stillmaintained.com/lmivan/grails-postgresql-extensions)
[![Build Status](https://travis-ci.org/kaleidos/grails-postgresql-extensions.svg?branch=master)](https://travis-ci.org/kaleidos/grails-postgresql-extensions)

This is a grails plugin that provides hibernate user types to use Postgresql native types such as Array, Hstore, Json,
Jsonb... from a Grails application. It also provides new criterias to query this new native types.

Currently the plugin supports array, hstore, json and jsonb fields as well as some query methods.
More native types and query methods will be added in the future.

* [Installation](#installation)
* [Configuration](#configuration)
* [Native Types](#native-types)
  * [Arrays](#arrays)
    * [Example](#example)
    * [Criterias](#criterias)
        * [Contains](#contains)
        * [Is contained](#is-contained)
        * [Overlaps](#overlaps)
        * [Is Empty](#is-empty)
        * [Is Not Empty](#is-not-empty)
        * [Is Empty or Contains](#is-empty-or-contains)
        * [Equals](#equals)
        * [Not Equals](#not-equals)
        * [Ilike](#ilike)
  * [Hstore](#hstore)
    * [Using Hstore](#using-hstore)
    * [Criterias](#hstore-criterias)
        * [Contains Key](#contains-key)
        * [Contains](#contains)
        * [Is Contained](#is-contained)
        * [ILike Value](#ilike-value)
  * [JSON](#json)
    * [Using json](#using-json)
    * [Criterias](#criterias)
        * [Has field value](#has-field-value)
  * [JSONB](#jsonb)
  * [Order](#order)
    * [Random order](#random-order)
    * [Sql formula](#sql-formula)
* [Authors](#authors)
* [Release Notes](#release-notes)


## Installation

The Grails 3 version only supports Hibernate 4.X. In `build.gradle` add the `jcenter` repository and the following
dependency to install the plugin:


```groovy
repositories {
    ...
    jcenter()
    ...
}

dependencies {
    ...
    compile 'org.grails.plugins:postgresql-extensions:<version>'
    ...
}
```

Please note that you also have to install the Grails Hibernate 4 plugin and the Postgresql jdbc driver. You can see
all available Postgresql jdbc libraries versions at [MVN Repository](http://mvnrepository.com/artifact/org.postgresql/postgresql).

```groovy
dependencies {
    ...
    compile 'org.grails.plugins:hibernate:4.3.10.4'
    provided 'org.postgresql:postgresql:9.4-1203-jdbc4'
    ...
}
```

## Configuration

After install the plugin you have to use a new Postgresql Hibernate Dialect in your application. Add it to the
`application.yml` file:

```yaml
---
dataSource:
    pooled: true
    jmxExport: true
    driverClassName: org.postgresql.Driver
    username: user
    password: password
    url: jdbc:postgresql://localhost:5432/db_name
    dbCreate: update

hibernate:
    dialect: net.kaleidos.hibernate.PostgresqlExtensionsDialect
```

If you just only add the dialect, hibernate will create a new sequence for every table to generate the sequential ids
used for the primary keys instead of a global sequence for all your tables.

You can also deactivate this behaviour and create only one unique sequence for all the tables with the following
property in your datasource definition:

```yaml
dataSource:
  postgresql:
    extensions:
      sequence_per_table: false
}
```


## Native Types

### Arrays

The plugin supports the definition of `Integer`, `Long`, `Float`, `Double`, `String`, and `Enum` arrays in your domain
classes.

The `Enum` arrays behaves almost identical to `Integer` arrays in that they store and retrieve an array of ints. The
difference, however, is that this is used with an Array of Enums, rather than Ints. The Enums are serialized to their
ordinal value before persisted to the database. On retrieval, they are then converted back into their original `Enum`
type.

#### Example

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

Now you can create domain objects using lists (or arrays) of integers, longs and strings and when you save the object
it will be stored as an postgresql array:

```groovy
def myLikes = new Like(favoriteNumbers: [5, 17, 9, 6],
                     favoriteLongNumbers: [123, 239, 3498239, 2344235],
                     favoriteFloatNumbers: [0.3f, 0.1f],
                     favoriteDoubleNumbers: [100.33d, 44.11d],
                     favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"],
                     favoriteJuices: [Like.Juice.ORANGE, Like.Juice.GRAPE])
myLikes.save()
```

And now, with `psql`:

```
=# select * from like;

 id |  favorite_long_numbers    |  favorite_float_numbers   |  favorite_double_numbers  |        favorite_movies                 | favorite_numbers | favorite_juices
----+---------------------------+---------------------------+---------------------------+----------------------------------------+------------------+----------------
  1 | {123,239,3498239,2344235} | {0.3,0.1}                 | {100.33,44.11}            | {Spiderman,"Blade Runner",Starwars}    | {5,17,9,6}       | {0,2}
```

#### Criterias

The plugin also includes some hibernate criterias to use in your queries. Please check the
[services](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/grails-app/services/test/criteria/array)
and the [tests](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/src/integration-test/groovy/net/kaleidos/hibernate/array)
created to see all usage examples.

You can also check the official [Postgresql Array operators](http://www.postgresql.org/docs/9.4/static/functions-array.html#ARRAY-OPERATORS-TABLE).

##### Contains

With this criteria you can get all the rows that contain all the values in the array field. To use it just use the new
criteria `pgArrayContains`:

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

With this criteria you can get all the rows that are contained by the values. To use it just use the new criteria
`pgArrayIsContainedBy`:

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

With this criteria you can get all the rows that contains any of the values. To use it just use the new criteria
`pgArrayOverlaps`:

```groovy
def result = Like.withCriteria {
    pgArrayOverlaps 'favoriteNumbers', numbers
}
```

#### Is Empty

With this criteria you can get all the rows that contains an-empty array in the selected field. To use it just use the
new criteria `pgArrayIsEmpty`:

```groovy
def result = Like.withCriteria {
    pgArrayIsEmpty 'favoriteMovies'
}
```

#### Is Not Empty

With this criteria you can get all the rows that contains a not empty array in the selected field. To use it just use
the new criteria `pgArrayIsNotEmpty`:

```groovy
def result = Like.withCriteria {
    pgArrayIsNotEmpty 'favoriteMovies'
}
```

#### Is Empty or Contains

This criteria is a mix of the `pgContains` and `pgIsEmpty`. Sometimes you have to execute 'pgContains' criteria if the
list has elements or a 'pgIsEmpty' if the list is empty. It could be something like this:

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

With this criteria you can get all the rows that are not equal to a value. To use it just use the new criteria
`pgArrayNotEquals`:

```groovy
def result = Like.withCriteria {
    pgArrayNotEquals 'favoriteNumbers', numbers
}
```

#### ILike

With this criteria you can get all the rows that are ilike to a value. To use it just use the new criteria `pgArrayILike`.

It only can be used on arrays of string.

It uses the ilike syntaxis, so you can do for example:

```groovy
def result = Like.withCriteria {
    pgArrayILike 'favoriteMovies', "%tarwar%"
}
```


### Hstore

The first thing you need to do is install hstore support in Postgresql. In Debian/Ubuntu you have to install the
`postgresql-contrib` package:

```
sudo apt-get install postgresql-contrib-9.4
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

#### Using Hstore

You only have to define the domain class with a `Map` attribute and use the Hibernate user type `HstoreMapType`.

```groovy
import net.kaleidos.hibernate.usertype.HstoreMapType

class TestHstore {

    Map testAttributes
    String anotherProperty

    static mapping = {
        testAttributes type: HstoreMapType
    }
}
```

Now you can create and instance of the domain class. Due to a limitation of the Hstore Postgresql type you can only
store Strings as key and value.

```groovy
def instance = new TestHstore(testAttributes: [foo: "bar"], anotherProperty: "Groovy Rocks!")
instance.save()

def instance2 = new TestHstore(testAttributes: [xxx: 1, zzz: 123], anotherProperty: "")
instance2.save()
```


```
=# select * from test_hstore;
 id | version | another_property | test_attributes
----+---------+------------------+-----------------
  1 |       0 | Groovy Rocks!    | "foo"=>"bar"
  2 |       0 |                  | "xxx"=>"1", "zzz"=>"123"
```


#### Hstore Criterias

The following criteria operations are available to query rows using the Hstore custom type. You can
check the [services](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/grails-app/services/test/criteria/hstore)
and the [tests](https://github.com/kaleidos/grails-postgresql-extensions/tree/master/src/integration-test/groovy/net/kaleidos/hibernate/hstore)
created to see all usage examples.

You can also check the official [Postgresql Hstore operators](http://www.postgresql.org/docs/9.4/static/hstore.html).

##### Contains Key

With this operation you can search for rows that contain an Hstore with the key passed as parameter.

```groovy
def wantedKey = "my-custom-key"
def result = MyDomain.withCriteria {
    pgHstoreContainsKey "attributes", wantedKey
}
```

##### Contains

You can search for data that contains certain pairs of _key_ and _value_.

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
    pgHstoreIsContained 'testAttributes', ["1": "a", "2": "b"]
}
```
The example above returns the rows that contains elements like:

```
testAttributes = ["1": "a"]
testAttributes = ["2": "b"]
testAttributes = ["1": "a", "2": "b"]
```
This criteria can also be used to look for exact matches.

##### ILike Value

With this operation you can search for rows that contain an Hstore in which any value matches (ilike) to the parameter.
It uses the ilike syntaxis, so you can do for example:

```groovy
def wantedValue = "%my-value%"
def result = MyDomain.withCriteria {
    pgHstoreILikeValue "attributes", wantedKey
}
```

### JSON

To define a json field you only have to define a `Map` field and use the `JsonMapType` hibernate user type.

```groovy
import net.kaleidos.hibernate.usertype.JsonMapType

class TestMapJson {
    Map data

    static constraints = {
    }
    static mapping = {
        data type: JsonMapType
    }
}
```

#### Using Json

Now you can create and instance of the domain class:

```groovy
def instance = new TestMapJson(data: [name: "Iván", age: 35, hasChilds: true, childs: [[name: 'Judith', age: 8], [name: 'Adriana', age: 5]]])
instance.save()
```


```
=# select * from test_map_json;

 id | version | data
----+---------+-------------------------------------------------------------------------------------------------------------
  1 |       0 | {"hasChilds":true,"age":35,"name":"Iván","childs":[{"name":"Judith","age":8},{"name":"Adriana","age":5}]}
```

As you can see the plugin converts to Json automatically the attributes and the lists in the map type.


#### Criterias

The plugin provides some criterias to query json fields. You can check the official
[Postgresql Json functions and operators](http://www.postgresql.org/docs/9.4/static/functions-json.html) in case you
need additional ones.

##### Has field value

With this criteria you can check if a json field contains some _value_ in some _key_. To use it just use the criteria
`pgJsonHasFieldValue`:


```groovy
def obj1 = new TestMapJson(data: [name: 'Iván', lastName: 'López']).save(flush: true)
def obj2 = new TestMapJson(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
def obj3 = new TestMapJson(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true)

def result = TestMapJson.withCriteria {
    pgJsonHasFieldValue 'data', 'name', 'Iván'
}
```

The previous criteria will return all the rows that have a `name` attribute in the json field `data` with the value
`Iván`. In this example `obj1` and `obj3`.



### JSONB

Since postgresql-extensions version 4.4.0 it is possible to use [Postgresql Jsonb](http://www.postgresql.org/docs/9.4/static/datatype-json.html)
instead of just json. You need to use at least Postgresql 9.4.

To define a jsonb field you only have to define a `Map` field and use the `JsonbMapType` hibernate user type.

```groovy
import net.kaleidos.hibernate.usertype.JsonbMapType

class TestMapJsonb {
    Map data

    static constraints = {
    }
    static mapping = {
        data type: JsonbMapType
    }
}
```

The same criterias implemented for Json are valid for Jsonb.


### Order

#### Random order

Sometimes you need to get some results ordered randomly from the database. Postgres provides a native function to do
that. So you can write something like this:

```sql
select * from foo order by random();
```

The plugin now offers a new order method to do this random sorting:

```groovy
import static net.kaleidos.hibernate.order.OrderByRandom.byRandom

class MyService {
    List<TestMapJsonb> orderByRandom() {
        return TestMapJsonb.withCriteria {
            order byRandom()
        }
    }
}
```

#### Sql formula

You may need to do a more complex sorting. Imagine that you have a table with a `jsonb` column and you want to order
by a field in that json. Using sql you can write:

```sql
select * from foo order by (data->'name') desc
```

With the plugin you can do the same with a new order method called `sqlFormula`:

```groovy
import static net.kaleidos.hibernate.order.OrderBySqlFormula.sqlFormula

class MyService {
    List<TestMapJsonb> orderByJson() {
        return TestMapJsonb.withCriteria {
            order sqlFormula("(data->'name') desc")
        }
    }
}
```

It's important to note that the "raw" sql is appended to the criteria, so you need to be sure that it's valid because
if not you'll get a sql error during runtime.


## Authors

You can send any questions to:

- Iván López: lopez.ivan@gmail.com ([@ilopmar](https://twitter.com/ilopmar))
- Alonso Torres: alonso.javier.torres@gmail.com ([@alotor](https://twitter.com/alotor))

Collaborations are appreciated :-)


## Release Notes

Version | Date        | Comments
------- | ------------| ---------
4.6.1   | 02/0ct/2015 | Plugin migrated to Grails 3
4.6.1   | 21/Sep/2015 | Hibernate 4.x. Fix [#76](https://github.com/kaleidos/grails-postgresql-extensions/issues/76).
4.6.0   | 08/Sep/2015 | Hibernate 4.x. Add support to order by a sql formula and by random. Fix [#72](https://github.com/kaleidos/grails-postgresql-extensions/issues/72).
4.5.0   | 02/Jun/2015 | Hibernate 4.x. GR8Conf Hackergarten! Merge PRs: [#62](https://github.com/kaleidos/grails-postgresql-extensions/pull/62), [#66](https://github.com/kaleidos/grails-postgresql-extensions/pull/66), [#67](https://github.com/kaleidos/grails-postgresql-extensions/pull/67), [#68](https://github.com/kaleidos/grails-postgresql-extensions/pull/68), [#69](https://github.com/kaleidos/grails-postgresql-extensions/pull/69)
3.4.0   | 02/Jun/2015 | Hibernate 3.x. GR8Conf Hackergarten! Add Jsonb support for Hibernate 3.x [#64](https://github.com/kaleidos/grails-postgresql-extensions/issues/64)
4.4.0   | 15/Mar/2015 | Hibernate 4.x. Add support for Jsonb.
3.3.0   | 18/Aug/2014 | Hibernate 3.x. Fix [#49](https://github.com/kaleidos/grails-postgresql-extensions/issues/49). Configure sequence per table or a global sequence for all tables.
4.3.0   | 17/Aug/2014 | Hibernate 4.x. Fix [#49](https://github.com/kaleidos/grails-postgresql-extensions/issues/49). Configure sequence per table or a global sequence for all tables.
3.2.0   | 02/Aug/2014 | Hiberate 3.x. pgJsonHasFieldValue criteria.
4.2.0   | 28/Jul/2014 | Hiberate 4.x. pgJsonHasFieldValue criteria.
3.1.0   | 25/Jul/2014 | Add JSON support for Hibernate 3.x. It's now possible to store and read domain classes with map types persisted to json.
4.1.0   | 24/Jul/2014 | Add JSON support. It's now possible to store and read domain classes with map types persisted to json.
4.0.0   | 18/Jul/2014 | Version compatible with Hibernate 4.x.
3.0.0   | 18/Jul/2014 | Version compatible with Hibernate 3.x.
[0.9](https://github.com/kaleidos/grails-postgresql-extensions/issues?milestone=1) | 16/Jun/2014 | Add new array criterias: pgArrayEquals, pgArrayNotEquals.
0.8.1   | 24/Apr/2014 | Fix NPE when array is null.
0.8     | 24/Apr/2014 | Added support for Double and Float arrays. Refactored the ArrayType to be used as a parametrized type.
0.7     | Unreleased  | New HstoreMapType and update plugin to Grails 2.2.5.
0.6.8   | 22/Apr/2014 | Fix NPE in HstoreType.
0.6.7   | 14/Feb/2014 | Support Java Arrays in criterias.
0.6.6   | 14/Feb/2014 | New criteria pgArrayIsEmptyOrContains.
0.6.5   | 13/Feb/2014 | Fix bug deleting instances with Hstore type. Thanks to Manuel Unno Vio!
0.6.4   | 30/Jan/2014 | Convert automatically the keys of Hstore to string.
0.6.3   | 19/Jan/2014 | Display the class name during startup when detecting a hstore property.
0.6.2   | Unreleased  | Refactor some tests.
0.6.1   | 28/Nov/2013 | Update postgresql jdbc driver to version 9.2 and do not export hibernate plugin.
0.6     | 21/Nov/2013 | Use a more complete Hstore parser. Thanks to Moritz Kobel!
0.5.1   | 10/Nov/2013 | Change base directory to compile AST before the plugin classes. Thanks to Moritz Kobel!
0.5     | 08/Nov/2013 | Add criteria operation for Hstore types.
0.4.1   | Unreleased  | Compile AST before the project itself.
0.4     | 28/Oct/2013 | Add support to Hstore. It's only possible to save and get, but no queries has been implemented.
0.3     | 18/Sep/2013 | Add support to define the schema name for the sequences.
0.2     | 25/Aug/2013 | Support for arrays of Enums with automatic serialization/deserialization to ordinal integer value. Thanks to Matt Feury!
0.1.1   | 22/Jul/2013 | Some refactors of the code. No functionality added.
0.1     | 16/Jul/2013 | Initial version of the plugin with support for integer, long and string array types and criterias pgArrayContains, pgArrayIsContainedBy, pgArrayOverlaps, pgArrayIsEmpty and pgArrayIsNotEmpty.
