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

## Writing documentation

Check out
[the wiki](https://github.com/spid-tech-docs/spid-tech-docs/wiki).
First time around? Start with the
[general overview](https://github.com/spid-tech-docs/spid-tech-docs/wiki/general-overview).

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

## Developing the site

The documentation is built in Clojure using various libraries. The main
structured of the app is thoroughly described in
[Building static sites in Clojure with Stasis](http://cjohansen.no/building-static-sites-in-clojure-with-stasis),
a tutorial on building sites on the same basic principle.

### Running the tests

You can run all the tests with `./run-integration-tests.sh`, or you
can start autotesting on a faster subset of the tests with
`./autotest.sh`
