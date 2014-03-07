(ns spid-docs.homeless
  "Where functions that have nowhere to live huddle together around a fire.")

(defn wrap-utf-8 [handler]
  "This function works around the fact that Ring simply chooses the default JVM
  encoding for the response encoding. This is not desirable, we always want to
  send UTF-8."
  (fn [request]
    (when-let [response (handler request)]
      (if (.contains (get-in response [:headers "Content-Type"]) ";")
        response
        (if (string? (:body response))
          (update-in response [:headers "Content-Type"] #(str % "; charset=utf-8"))
          response)))))

(defn update-vals [m f]
  "Returns a new map with `f` applied to all values in `m`."
  (into {} (for [[k v] m] [k (f v)])))

(defn min* [vals]
  "Like min, but takes a list - and 0 elements is okay."
  (when (seq vals) (apply min vals)))

(defn subs* [s len]
  "Like subs, but safe - ie, doesn't barf on too short."
  (if (> (count s) len)
    (subs s len)
    s))

(defn fewest-preceding-spaces [lines]
  "Find the lowest number of spaces that all lines have as a common
   prefix. Except, don't count empty lines."
  (->> lines
       (remove #(empty? %))
       (map #(count (re-find #"^ +" %)))
       (min*)))

(defn chop-off-common-whitespace [lines]
  "Given a block of code, if all lines are indented, this removes the
   preceeding whitespace that is common to all lines."
  (let [superflous-spaces (fewest-preceding-spaces lines)]
    (map #(subs* % superflous-spaces) lines)))
