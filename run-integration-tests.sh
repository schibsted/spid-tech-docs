echo "$(tput setf 2)Running full suite of tests...$(tput sgr0)"
JVM_OPTS="-Dspid.freeze.assets=true -Dspid.freeze.pages=true" lein with-profile test midje
