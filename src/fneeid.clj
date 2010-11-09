(ns fneeid)

(defn identify 
  "It's complicated"
  [question function positive negative]
  (with-meta
    (fn [input truth]
      (let [resfn (if truth positive negative)
            result (filter (if truth function (complement function)) input)]
        (if (fn? resfn)
          (with-meta (partial resfn result) (meta resfn))
          result)))
    {:question question}))

(let [q (identify "Are you working on a scalar?"
                  #(some #{'coll} (flatten (:arglists (meta (resolve %)))))
                  nil
                  nil)]
  (def graph (with-meta (partial q (keys (ns-publics 'clojure.core))) (meta q))))

(loop [q graph]
  (if (fn? q)
    (do
      (print (:question (meta q)) "[yn] ")
      (flush)
      (recur (q (#{"y" "yes"} (read-line)))))
    (println q)))
