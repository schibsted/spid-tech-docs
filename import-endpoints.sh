echo "$(tput setf 2)Importing endpoints...$(tput sgr0)"
lein run -m spid-docs.import-endpoints/import-endpoints
