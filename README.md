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

## Status

kind-clerk does not support the full Kindly semantics. 

While Kindly allows to infer kinds in the context of evalating a given *form* with a given resulting *value*, the current adapter can only rely on the value, not the form.

This means that some Kindly-compliant namespaces would not work through Clerk.

For example, the following code would not work correctly, since it provides the kind information through the form's metadata (rather than through the resulting value)
```clj
(defn my-hiccup-generating-function []
  [:div [:p "hello"]])

^:kind/hiccup
(my-hiccup-generating-function)
```
