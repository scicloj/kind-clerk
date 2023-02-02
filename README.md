# kind-clerk

This library adapts the [Clerk](https://clerk.vision/) tool to support the [Kindly](https://scicloj.github.io/kindly/) convention for Clojure literate programming.

It is part of an ongoing attemt to create a common ground for Clojure learning resources that would work across different tools.

[![Clojars Project](https://img.shields.io/clojars/v/org.scicloj/kind-clerk.svg)](https://clojars.org/org.scicloj/kind-clerk)

## Usage

After calling the `setup!` function at the relevant namespace,
```clj
(require '[scicloj.kind-clerk.api :as kind-clerk])
(kind-clerk/setup!)
```
one can just keep using Clerk the way they like.

* Values which have kind information provided by Kindly will be displayed accordingly. 
* Other values will be displayed the usual way Clerk treats them.

See the [example project](./examples/example-project).
