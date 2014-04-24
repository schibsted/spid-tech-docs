# SPiD API Documentation

The SPiD API Documentation is a Clojure app that pulls information
from the API itself and merges it with examples and meta-data stored
in this repository. The site can be browsed/developed live, or
exported to static files.

## Up and running

1. **Install Leiningen**

   If you use homebrew:

   ```sh
   brew update && brew install leiningen
   ```

   Otherwise follow the instructions in the
   [Leiningen](https://github.com/technomancy/leiningen#leiningen)
   readme.

2. **Manually install some dependencies**

   Because the Java SDK is not yet in maven central, you have to clone and
   install it manually. The same goes for the Clojure SDK, since it relies
   on the Java SDK.

   Follow the instructions in the [spid-sdk-clojure README](https://github.com/spid-tech-docs/spid-sdk-clojure).

   This will change once the SDKs are in Maven Central.

3. **Fetch the git submodules**

   The example code as well as syntax highlighting themes are pulled in as
   submodules.

   ```sh
   git submodule update --init
   ```

4. **Run the web server**

   ```sh
   lein ring server
   ```

   That should pop up a browser with the SPiD API documentation.
   Replace `server` with `server-headless` if you would prefer to open
   your own browser window.

There is a local cached copy of the endpoint descriptions in the repo. In order
to refresh it, and use all available features, you will need to put your API
credentials in `resources/config.edn`. Copy `resources/config.sample.edn` and
fill in the placeholders.

## Exporting the site

If you want a static export of the site, `cd` into the root of the project, then
run:

```sh
lein build-site
```

The resulting site in `./dist` is ready for use and can be `scp`-ed directly to
a server. Note that it doesn't work well served from `file://` because it
uses absolute URLs. Just use `python -m SimpleHTTPServer` in the directory to
set up a local web server for it.

## Writing documentation

Documentation is stored in two main formats:

1. [Markdown](http://daringfireball.net/projects/markdown/syntax) is used for
   articles/guides as well as most blobs of text describing various aspects of
   API endpoints etc. Many
   [Github extensions](https://help.github.com/articles/github-flavored-markdown)
   are supported by [the parser](https://github.com/sirthias/pegdown).

2. [edn](https://github.com/edn-format/edn) is used for structured meta-data
   and content that is cross-linked or otherwise used across multiple places in
   the documentation. **edn** is like JSON on steroids. While JSON is
   "JavaScript Object Notation", edn is "Extensible Data Notation". It
   supports booleans, nil (null in JSON), strings, integers, floats, lists,
   vectors (arrays in JSON), maps (objects in JSON), sets, keywords (used for
   "special strings", like keys in maps, ids, etc) and more.

All documentation lives in the `resources/` directory.

### resources/public

Files in this directory will be served as is. Put anything you like here. Please
do not put HTML files that duplicate markup from the main site. If you want to
create a new page, look into creating an article instead of dumping a file here.

### resources/articles

.md files in this directory will be available as web pages with a URL
corresponding to the filename. `resources/articles/getting-started.md` is turned
into the page `/getting-started/`.

These markdown files support a few custom extensions: tabs and example
code. Details below.

#### Tabs

To create tabbed content, add a header starting with the string `:tabs`. Then
add individual tabs in sub-headers starting with the string `:tab`. End the tabbed
section with a header of the same level as you started and the text `/:tabs`. An
example visualizes how this works:

```md
## :tabs A tab container

### :tab PHP

Something about PHP in here.

### :tab Java

Something about Java in here

## /:tabs
```

Note that the sub-headings must be one level below the tabs heading,
and that the opening `:tabs` and closing `/:tabs` must be in the same
heading level.

#### Example code

One of the goals of the SPiD documentation is for it to be fully automated and
verifiable. This means that code examples must stem from real code-bases that
can be run and tested. All code used in the docs live in the example code git
submodule (at [resources/example-repos](./resources/example-repos)). To insert a
snippet of code from this repository into your pages, you can use the
`<spid-example>` custom extension.

First, the code example to be extracted must be commented. The syntax varies by
language. For Java and PHP, it looks like this:

```java
/** Fetch user information and add to session */
@RequestMapping("/create-session")
String createSession(@RequestParam String code, HttpServletRequest request) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
    SPPClient client = createUserClient(code);
    JSONObject user = client.GET("/me").getJSONObject();
    HttpSession session = request.getSession();
    session.setAttribute("user", user);
    session.setAttribute("client", client);
    return "redirect:/";
}
/**/
```

Start with a comment `/** ... */` and end the example with `/**/`. Then refer to
this example in the markdown page with the path to this file and the leading
comment:

```
<spid-example lang="java"
              src="/sso/src/main/java/SingleSignOnController.java"
              title="Fetch user information and add to session"/>
```

We refer to code snippets with the full comment string instead of an
ID because this keeps the sample code nice and readable for those who
clone it directly.

### resources/endpoints

These are markdown files that describe additional meta-data about endpoints.
The files have a tiny sliver of structure through the use of sectioning keys:

```text
:description

The endpoint description
```

The file is sectioned into key/value pairs where the colon-prefixed name is the
key, and all text up until the next key is the value.

### Other files in resources

In addition to these directories, there are various files that have specific
functionality.

#### resources/apis.edn

This file describes the service/api categorization. The ids of services and apis
defined in this file can be used to categorize endpoints in
`resources/endpoints/*.edn`.

#### resources/types.edn

Types used across many endpoints. Many types are defined inline for the
endpoints where they are used. If a type is used in more than one endpoint, this
is the place to define it, to enable cross-linking.

Types that don't have descriptions will not be linked. This is used for basic
types like strings and numbers, where additional explanation is not necessary.

## Developing the documentation

The documentation is built in Clojure using various libraries. The main
structured of the app is thoroughly described in
[Building static sites in Clojure with Stasis](http://cjohansen.no/building-static-sites-in-clojure-with-stasis),
a tutorial on building sites on the same basic principle.

### Running the tests

You can run all the tests with `./run-integration-tests.sh`, or you
can start autotesting on a faster subset of the tests with
`./autotest.sh`
