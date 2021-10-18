(ns fulcro-pet.client.ui
  (:require [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))


(def data
  {:project/name "Fulcro Pet Project"
   :project/boards [{:board/name "Kanban Board"
                     :board/buckets [{:bucket/name "Backlog"
                                      :bucket/tasks [{:task/name        "Add mutations"
                                                      :task/description "Implement transactional operations on client DB"
                                                      :task/status      "New"}
                                                     {:task/name        "Add remoting"
                                                      :task/description "Implement basic server with EQL remoting"
                                                      :task/status      "New"}]
                                      :bucket/description "New or deferred tasks"}
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
  {:initial-state (fn [{:task/keys [id name description status]}]
                    {:task/id id
                     :task/name name
                     :task/description description
                     :task/status status})}
  (dom/div {:style {:padding "2px"}}
   (dom/table {:style {:text-align "left" :width "70%" :background-color "#7FCDCD"}}
    (dom/tr
     (dom/th {:style {:width "20%"}} "Task:")
     (dom/td name))
    (dom/tr
     (dom/th "Description:")
     (dom/td description))
    (dom/tr
     (dom/th "Status:")
     (dom/td status)))))

(def ui-task (comp/factory Task))


;; -----------------------------------------------------------
;; Bucket componenent maps over Task components
;; -----------------------------------------------------------
(defsc Bucket [this {:bucket/keys [name tasks description]}]
  {:initial-state (fn [{:bucket/keys [name tasks description]}]
                    {:bucket/name name
                     :bucket/tasks (map #(comp/get-initial-state Task %) tasks)
                     :bucket/description description})}
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
  {:initial-state (fn [{:board/keys [name buckets]} ]
                    {:board/name name
                     :board/buckets (map #(comp/get-initial-state Bucket %) buckets)})}
  (dom/div
   (map ui-bucket buckets)))

(def ui-board (comp/factory Board))


;; -----------------------------------------------------------
;; Root component dispalys a board
;; -----------------------------------------------------------
(defsc Root [this {:keys [name board]}]
  {:initial-state (fn [params]
                    {:name (:project/name data)
                     :board (comp/get-initial-state Board (get-in data [:project/boards 0]))})}
  (dom/div
   (dom/h2 "Cro-Pet")
   (dom/h5 name)
   (dom/hr)
   (ui-board board)))
