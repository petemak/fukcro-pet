(ns fulcro-pet.client.ui
  (:require [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))


;; -----------------------------------------------------------
;; Initial data describles shape of the data
;; project -> boards (kanban, etc) -> task lists (backlog, doing, done) -> tasks (do something)
;; -----------------------------------------------------------
(def data
  {:project/name "Fulcro - Pet Project"
   :project/boards [{:board/name "Kanban Board"
                     :board/lists [{:list/name "Backlog"
                                    :list/description "New or deferred tasks"
                                    :list/tasks [{:task/name        "Add mutations"
                                                  :task/description "Implement transactional operations on client DB"
                                                  :task/status      "New"}
                                                 {:task/name        "Add remoting"
                                                  :task/description "Implement basic server with EQL remoting"
                                                  :task/status      "New"}]}

                                   {:list/name "To Do"
                                    :list/description "Scheduled for the current sprint"
                                    :list/tasks [{:task/name        "Add callbacks"
                                                  :task/description "Add on-click functions for actions as computed data"
                                                  :task/status      "Scheduled"}
                                                 {:task/name        "Add querries"
                                                  :task/description "Add co-located querries for each UI components"
                                                  :task/status      "Scheduled"}]}

                                   {:list/name "Doing"
                                    :list/description "Currently doing"
                                    :list/tasks [{:task/name        "Add intitial state"
                                                  :task/description "Add initial state for UI compent tree"
                                                  :task/status      "In progress"}]}

                                   {:list/name "Done"
                                    :list/description "Completed"
                                    :list/tasks [{:task/name        "Commit initial project"
                                                  :task/description "Commit working base project to Github"
                                                  :task/status      "Done!" }
                                                 {:task/name        "Set up pet project"
                                                  :task/description "Create a working basic app using Fulcrologic's Fulcro"
                                                  :task/status      "Done!" }]}]}] })

;; -----------------------------------------------------------
;; Task component
;; -----------------------------------------------------------
(defsc Task [this {:task/keys [id name description status]} {:keys [onclick]}]
  {:query [:task/id :task/name :task/description :task/status]
   :initial-state (fn [{:task/keys [id name description status]}]
                    {:task/id id
                     :task/name name
                     :task/description description
                     :task/status status})}
  (dom/div {:style {:padding "2px"}}
   (dom/table {:style {:textAlign "left" :width "70%" :backgroundColor "#7FCDCD"}}
    (dom/tbody
     (dom/tr
      (dom/th {:style {:width "20%"}} "Task:")
      (dom/td name))
     (dom/tr
      (dom/th "Description:")
      (dom/td description))
     (dom/tr
      (dom/th "Status:")
      (dom/td status))
     (dom/tr 
      (dom/td  {:colspan "2"  :align "right"}
        (dom/button {:onClick #(onclick name)} "x" )))))))

(def ui-task (comp/factory Task {:keyfn :task/name}))


;; -----------------------------------------------------------
;; TaskList componenent maps over Task components
;; -----------------------------------------------------------
(defsc TaskList [this {:list/keys [name tasks description]}]
  {:query [:list/name :list/description {:list/tasks (comp/get-query Task)}]
   :initial-state (fn [{:list/keys [name tasks description]}]
                    {:list/name name
                     :list/description description
                     :list/tasks (map #(comp/get-initial-state Task %) tasks)})}
  (let [onclick (fn [txt] (println "Task " name ": " txt " clicked"))] 
    (dom/div
     (dom/hr)
     (dom/b {:style {:color "blue"}} name)
     (dom/p description ": " (count tasks))
     (map (fn [t] (ui-task (comp/computed t {:onclick onclick}))) tasks)
     (dom/hr))))

(def ui-tasklist (comp/factory TaskList {:keyfn :list/name}))


;; -----------------------------------------------------------
;; Board component dispalys task list components
;; -----------------------------------------------------------
(defsc Board [this {:board/keys [name lists]}]
  {:query [:board/name {:board/lists (comp/get-query TaskList)}]
   :initial-state (fn [{:board/keys [name lists]} ]
                    {:board/name name
                     :board/lists (map #(comp/get-initial-state TaskList %) lists)})}
  (dom/div
   (map ui-tasklist lists)))

(def ui-board (comp/factory Board))     


;; -----------------------------------------------------------
;; Root component dispalys a board
;; -----------------------------------------------------------
(defsc Root [this {:keys [name board]}]
  {:query [:name {:board (comp/get-query Board)}]
   :initial-state (fn [params]
                    {:name (:project/name data)
                     :board (comp/get-initial-state Board (get-in data [:project/boards 0]))})}
  (dom/div
   (dom/h2 "Cro-Pet")
   (dom/h5 name)
   (dom/hr)
   (ui-board board)))
