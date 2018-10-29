(ns connecttomap.core
    (:require
      [wscljs.client :as ws]
      ))


(enable-console-print!)



(def canvas (.getElementById js/document "canvas"))
(def ctx (.getContext canvas "2d"))
(def green (new js/Image))
(def red (new js/Image))
(def artmap (new js/Image))
(set! (.-src green) "images/green.png")
(set! (.-src red) "images/red.png")
(set! (.-src artmap) "images/map.png")
(set! (.-onload artmap) (fn [] (.drawImage ctx artmap 0 0 1980 1020)))



(def handlers {:on-message (fn [e]
                             (let [nodes (js->clj (.parse js/JSON (.-data e)) :keywordize-keys true)]
                               (.drawImage ctx artmap 0 0 1980 1020)
                               (doseq [node nodes] (if (:result node)
                                                     (.drawImage ctx green (:x node) (:y node) 10 10)
                                                     (.drawImage ctx red (:x node) (:y node) 10 10)))))

               :on-open    #(prn "Opening a new connection")
               :on-close   #(prn "Closing a connection")})

(def socket (ws/create "ws://localhost:80/ws" handlers))


(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
