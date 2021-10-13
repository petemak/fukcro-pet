(ns fulcro-pet.client.main
  (:require [fulcro-pet.client.ui :as ui]
            [fulcro-pet.client.application :as fap]
            [com.fulcrologic.fulcro.components :as comp]
            [com.fulcrologic.fulcro.application :as app]))


;;----------------------------------------------------------
;; Application intitialisation
;;----------------------------------------------------------
(defn ^export init
  "Mounts Fulcro app and UI root component into the page element tagged app"
  []
  (app/mount! fap/app ui/Root "app")
  (js/console.log "Load: application mounted!"))


;;----------------------------------------------------------
;; Force applito re-mount and refress UI
;;----------------------------------------------------------
(defn ^export refresh
  "Reounts Fulcro app and UI root component into the page element tagged app"
  []
  (app/mount! fap/app ui/Root "app")
  (comp/refresh-dynamic-queries! fap/app)
  (js/console.log "Hot reload: applicaion mounted!"))
