(ns fulcro-pet.client.ui
  (:require [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))


(def data
  {:project/name "Fukcro pet project"
   :project/boards [{:board/name "Kanban"
                     :board/task-lists [{:task-list/name "Backlog"
                                         :task-list/tasks [{:task/name        "Set up pet project"
                                                            :task/description "Create pet project with Fulcro with 
                                                                                basic client."
                                                            :task/status      "New" }
                                                                   
                                                           {:task/name        "Add mutations"
                                                            :task/description "Implement transactional operations 
                                                                                        updating the client DB"
                                                            :task/status      "New"}
                                         
                                                           {:task/name        "Add remoting"
                                                            :task/description "Implement basic server with EQL remoting"
                                                            :task/status      "New"}]}
                                       {:task-list/name "ToDo"
                                        :task-list/tasks []}
                                       {:task-list/name "Doing"
                                        :task-list/tasks []}
                                       {:task-list/name "Done"
                                        :task-list/tasks []}]}] })

;; -----------------------------------------------------------
;; Task component
;; -----------------------------------------------------------
(defsc Task [this {:task/keys [id name description status]}]
  (dom/div
   (dom/p "Task: "       name)
   (dom/p "Description: " description)
   (dom/p "Status: "      status)))

(def ui-task (comp/factory Task))


(defsc Root [this props]
  (dom/div
   (dom/h2 "CroPet")
   (dom/label "Fulcro pet project")
   (dom/hr)
   (dom/b (get-in data [:project/boards 0 :board/task-lists 0 :task-list/name]))
   (ui-task (get-in data [:project/boards 0 :board/task-lists 0 :task-list/tasks 0]))))
