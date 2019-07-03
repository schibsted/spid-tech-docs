# Schibsted account API Documentation [![Build Status](https://travis-ci.org/schibsted/spid-tech-docs.svg?branch=master)](https://travis-ci.org/schibsted/spid-tech-docs)

The Schibsted account API Documentation is a Clojure app that pulls information
from the API itself and merges it with examples and meta-data stored
in this repository. The site can be browsed/developed live, or
exported to static files.

## Up and running

1. **Install Leiningen**

   1. Download the [lein script](https://raw.github.com/technomancy/leiningen/stable/bin/lein)
   2. Place it on your $PATH where your shell can find it (eg. `~/bin`)
   3. Set it to be executable (`chmod a+x ~/bin/lein`)
   4. Run it (`lein`) and it will download the self-install package

2. **Fetch and run the web server**

   ```sh
   git clone git@github.com:schibsted/spid-tech-docs.git
   cd spid-tech-docs
   lein ring server
   ```

   That should pop up a browser with the Schibsted account API documentation.
   Replace `server` with `server-headless` if you would prefer to open
   your own browser window.

Note: There's no need to restart the server after making changes.

## Writing documentation

Check out
[the wiki](https://github.com/schibsted/spid-tech-docs/wiki).
First time around? Start with the
[general overview](https://github.com/schibsted/spid-tech-docs/wiki/general-overview).

You might also want to check out [a summary of this wiki in presentation form](http://schibsted.github.io/spid-tech-docs/), or even [a video of that presentation](https://docs.spid.no/display/GI/Tech+docs+intro).

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
structure of the app is thoroughly described in
[Building static sites in Clojure with Stasis](http://cjohansen.no/building-static-sites-in-clojure-with-stasis),
a tutorial on building sites on the same basic principle.

### Running the tests

You can run all the tests with `./run-integration-tests.sh`, or you
can start autotesting on a faster subset of the tests with
`./autotest.sh`

Note that nodejs is used to create SVG sequence diagrams. The latest compatible
version of nodejs is `0.10.0`. Use `nvm` to manage different versions of nodejs locally 
and make sure that you use version `0.10.0` when running the integration tests.
