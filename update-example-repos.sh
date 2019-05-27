BRANCH='master'

if [ ! -d "resources/example-repos" ]; then
    echo "$(tput setf 2)Fetching example code repositories...$(tput sgr0)"
    mkdir resources/example-repos
else
    echo "$(tput setf 2)Updating example code repositories...$(tput sgr0)"
fi

if [ ! -d "resources/example-repos/clj" ]; then
    git clone -b $BRANCH https://github.com/spid-tech-docs/spid-clj-examples resources/example-repos/clj
else
    cd resources/example-repos/clj
    git pull --rebase
    cd ../../..
fi

if [ ! -d "resources/example-repos/curl" ]; then
    git clone -b $BRANCH https://github.com/spid-tech-docs/spid-curl-examples resources/example-repos/curl
else
    cd resources/example-repos/curl
    git pull --rebase
    cd ../../..
fi

if [ ! -d "resources/example-repos/java" ]; then
    git clone -b $BRANCH https://github.com/schibsted/spid-java-examples resources/example-repos/java
else
    cd resources/example-repos/java
    git pull --rebase
    cd ../../..
fi

if [ ! -d "resources/example-repos/js" ]; then
    git clone -b $BRANCH https://github.com/spid-tech-docs/spid-js-examples resources/example-repos/js
else
    cd resources/example-repos/js
    git pull --rebase
    cd ../../..
fi

