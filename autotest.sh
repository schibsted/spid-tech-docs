green='\033[0;32m'
NC='\033[0m' # No Color
echo ""
echo "${green}Starting autotest...${NC}"
echo ""
echo "To speed up the feedback loop, these changes are in effect:"
echo " - Integration tests are excluded"
echo " - Pages aren't pygmentized"
echo " - Static assets are frozen"
echo ""
echo "Remember to always ./run-integration-tests.sh before pushing your changes."
JVM_OPTS="-Dspid.skip.pygments=true -Dspid.freeze.assets=true" lein with-profile test midje :autotest :filter -slow
