(ns fneeid)

(defn identify [question function positive negative]
  (fn [input truth]
    (let [resfn (if truth positive negative)
          result (remove #(= truth (not (function %))) input)]
      (if (fn? resfn)
        (partial resfn result)
        result))))
