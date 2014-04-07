green='\033[0;32m'
NC='\033[0m' # No Color
echo "${green}Running full suite of tests...${NC}"
JVM_OPTS="-Dspid.freeze.assets=true -Dspid.freeze.pages=true" lein with-profile test midje
