(ns spid-docs.homeless
  "Where functions that have nowhere to live huddle together around a fire.")

(defn wrap-utf-8
  "This function works around the fact that Ring simply chooses the default JVM
  encoding for the response encoding. This is not desirable, we always want to
  send UTF-8."
  [handler]
  (fn [request]
    (when-let [response (handler request)]
      (if (.contains (get-in response [:headers "Content-Type"]) ";")
        response
        (if (string? (:body response))
          (update-in response [:headers "Content-Type"] #(str % "; charset=utf-8"))
          response)))))

(defn update-vals
  "Returns a new map with `f` applied to all values in `m`."
  [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn min*
  "Like min, but takes a list - and 0 elements is okay."
  [vals]
  (when (seq vals) (apply min vals)))

(defn subs*
  "Like subs, but safe - ie, doesn't barf on too short."
  [s len]
  (if (> (count s) len)
    (subs s len)
    s))

(defn fewest-preceding-spaces
  "Find the lowest number of spaces that all lines have as a common
   prefix. Except, don't count empty lines."
  [lines]
  (->> lines
       (remove #(empty? %))
       (map #(count (re-find #"^ +" %)))
       (min*)))

(defn chop-off-common-whitespace
  "Given a block of code, if all lines are indented, this removes the
   preceeding whitespace that is common to all lines."
  [lines]
  (let [superflous-spaces (fewest-preceding-spaces lines)]
    (map #(subs* % superflous-spaces) lines)))
