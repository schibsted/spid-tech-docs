(ns spid-docs.examples-test
  (:require [spid-docs.examples :refer :all]
            [midje.sweet :refer :all]))

(fact
 "It extracts examples based on language specific delimiters."
 (create-example :php "/sso/index.php" "Example" "
abc
/// Example
def
ghi
///
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
