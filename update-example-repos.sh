BRANCH='master'

if [ ! -d "resources/example-repos" ]; then
    echo "$(tput setf 2)Fetching example code repositories...$(tput sgr0)"
    mkdir resources/example-repos
else
    echo "$(tput setf 2)Updating example code repositories...$(tput sgr0)"
fi

