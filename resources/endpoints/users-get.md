:introduction

List users. All query parameters are user properties that will be used to search
for users. When sending multiple query parameters, the API performs an `AND`
search. Some parameters are used to fuzzy match, while others perform exact
matches. See the parameter list for details on which does what.

All regular expression queries use
[Pearl Compatible Regular Expression](http://en.wikipedia.org/wiki/Perl_Compatible_Regular_Expressions)
syntax, as implemented in
[PHP](http://www.php.net/manual/en/reference.pcre.pattern.syntax.php) and other
places.
