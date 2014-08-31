# toml-parser

A parser for TOML in Scala.  TOML is a simple configuration language (https://github.com/toml-lang/toml).


## Use

### get

```
def get(key: String): Option[Any]
```

Get value regardless of type.  Value will return `None` if key is not present.

### optional

```
def optional[A](key: String): Option[A]
def opt[A](key: String): Option[A]
```

Get value as given type `A`.  Value will return `None` if key is not present or if key is present with a type other than `A`.

### require

```
def require[A](key: String): A
def req[A](key: String): A
```

Get value as given type `A`.  Value will throw an exception if key is not present or if key is present with a type other than `A`.

### seq (shorthand)

```
def seq[A: ClassTag](key: String): Seq[A]
```

Shorthand for `optional[Seq[A]](key).getOrElse(Seq.empty)`.


## Building

1. Clone github repository
2. `$ sbt compile`

At this point you might want to `publish` or `publish-local` if you intend to use it.

## Testing

1. `$sbt test`