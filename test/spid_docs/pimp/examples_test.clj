(ns spid-docs.pimp.examples-test
  (:require [test-with-files.core :refer [with-files public-dir]]
            [spid-docs.pimp.examples :refer :all]
            [midje.sweet :refer :all]))

(fact
 "It extracts examples based on language specific delimiters."
 (create-example :php "/sso/index.php" "Example" "
abc
/** Example */
def
ghi
/**/
jkl
") => "<?php // index.php
def
ghi")

(fact
 "It adjusts indentation."
 (create-example :java "/sso/src/main/java/Index.java" "Example" "
abc
    /** Example */
    private String def() {
        return ghi;
    }

    private String ghi() {
        return jkl;
    }
    /**/
mno
") => "private String def() {
    return ghi;
}

private String ghi() {
    return jkl;
}")

(with-files [["/example-repo/examples/java/getting-started/src/main/GettingStarted.java" "
class GettingStarted {
    /** Example */
    private String def() {
        return ghi;
    }
    /**/
    private String ghi() {
        return jkl;
    }
}
"]]

  (with-redefs [examples-dir (str public-dir "/example-repo/examples/")]

    (fact "It inlines in markup"
          (inline-examples "<h2>Java</h2>
<spid-example lang=\"java\"
              src=\"/getting-started/src/main/GettingStarted.java\"
              title=\"Example\"/>") => "<h2>Java</h2>
<pre><code class=\"java\">private String def() {
    return ghi;
}</code></pre>")))
