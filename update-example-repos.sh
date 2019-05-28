BRANCH='master'

if [ ! -d "resources/example-repos" ]; then
    echo "$(tput setf 2)Fetching example code repositories...$(tput sgr0)"
    mkdir resources/example-repos
else
    echo "$(tput setf 2)Updating example code repositories...$(tput sgr0)"
fi

if [ ! -d "resources/example-repos/curl" ]; then
    git clone -b $BRANCH https://github.com/spid-tech-docs/spid-curl-examples resources/example-repos/curl
else
    cd resources/example-repos/curl
    git pull --rebase
    cd ../../..
fi
