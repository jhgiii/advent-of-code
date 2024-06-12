(defproject aoc "1.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [digest "1.4.10"]]

  :main ^:skip-aot aoc.core
  :resource-paths ["shared" "resources"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
