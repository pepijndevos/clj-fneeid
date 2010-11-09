(ns fneeid)

(defn ask
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

(let [q (ask "Are you working on a scalar?"
          '#{aget aset alenght amap areduce map reduce reductions apply for doseq doall loop}
          (ask "Is it a Java array?"
            '#{aget aset alenght amap areduce}
            nil
            (ask "Do you want to iterate over the scalar?"
              '#{map reduce doseq for loop}
              nil
              nil))
          (ask "Is it a boolean?"
            '#{boolean? if when if-not if-let when-let when-not}
            nil
            nil))]
  (def graph (with-meta (partial q (keys (ns-publics 'clojure.core))) (meta q))))

(loop [q graph]
  (if (fn? q)
    (do
      (print (:question (meta q)) "[yn] ")
      (flush)
      (recur (q (#{"y" "yes"} (read-line)))))
    (println q)))
