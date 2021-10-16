(ns fulcro-pet.client.ui
  (:require [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))


(def data
  {:project/name "Fulcro pet project"
   :project/boards [{:board/name "Kanban Board"
                     :board/buckets [{:bucket/name "Backlog"
                                      :bucket/tasks [{:task/name        "Add mutations"
                                                      :task/description "Implement transactional operations on client DB"
                                                      :task/status      "New"}
                                                     {:task/name        "Add remoting"
                                                      :task/description "Implement basic server with EQL remoting"
                                                      :task/status      "New"}]
                                      :bucket/description "New or deffered tasks"}
                                     {:bucket/name "To Do"
                                      :bucket/tasks [{:task/name        "Add callbacks"
                                                      :task/description "Add on-click functions for actions as computed data"
                                                      :task/status      "Scheduled"}
                                                     {:task/name        "Add querries"
                                                      :task/description "Add co-located querries for each UI components"
                                                      :task/status      "Scheduled"}
                                                     ]
                                      :bucket/description "Scheduled for the current sprint"}
                                     {:bucket/name "Doing"
                                      :bucket/tasks [{:task/name        "Add intitial state"
                                                      :task/description "Add initial state for UI compent tree"
                                                      :task/status      "In progress"}]
                                      :bucket/description "Currently doing"}
                                     {:bucket/name "Done"
                                      :bucket/tasks [{:task/name        "Commit initial project"
                                                      :task/description "Commit working base project to Github"
                                                      :task/status      "Done!" }
                                                     {:task/name        "Set up pet project"
                                                      :task/description "Create a working basic app using Fulcrologic's Fulcro"
                                                      :task/status      "Done!" }]
                                      :bucket/description "Completed"}]}] })

;; -----------------------------------------------------------
;; Task component
;; -----------------------------------------------------------
(defsc Task [this {:task/keys [id name description status]}]
  (dom/div
   (dom/p "Task: "        name)
   (dom/p "Description: " description)
   (dom/p "Status: "      status)))

(def ui-task (comp/factory Task))


;; -----------------------------------------------------------
;; Bucket componenent maps over Task components
;; -----------------------------------------------------------
(defsc Bucket [this {:bucket/keys [name tasks description]}]
  (dom/div
   (dom/hr)
   (dom/b {:style {:color "blue"}} name)
   (dom/p description ": " (count tasks))
   (map ui-task tasks)
   (dom/hr)))

(def ui-bucket (comp/factory Bucket))


;; -----------------------------------------------------------
;; Board component dispalys Busket components
;; -----------------------------------------------------------
(defsc Board [this {:board/keys [name buckets]}]
  (dom/div
   (map ui-bucket buckets)))

(def ui-board (comp/factory Board))


;; -----------------------------------------------------------
;; Root component dispalys a board
;; -----------------------------------------------------------
(defsc Root [this props]
  (dom/div
   (dom/h2 "CroPet")
   (dom/label (get-in data [:project/boards 0 :board/name]))
   (dom/hr)
   (ui-board (get-in data [:project/boards 0]))))
