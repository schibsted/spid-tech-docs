ENV["WATCHR"] = "1"

def compile_routes
  puts "Compiling routes #{Time.now}"
  `php jsonify.php > resources/cached-endpoints.json`
end

compile_routes
watch('routes.api.2.php') { compile_routes }
