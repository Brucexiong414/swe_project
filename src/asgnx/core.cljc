(ns asgnx.core
  (:require [clojure.string :as string]
            [clojure.core.async :as async :refer [go chan <! >!]]
            [asgnx.kvstore :as kvstore
             :refer [put! get! list! remove!]]))

;; project to build a self-service question and answer machine to
;; make this process more fairly and conveniently, which would potentially
;; increase students' study efficiency.
;; This project is built upon the text messaging application.
;; Students can directly send their message to register the office hour,
;; and get the information about the line.
;; TA or professor can notify the next student in the line.
;; The registration of student is sorted by the time they were asked to
;; ensure the correct and fair order of answering those questions.


;; Do not edit!
;; A def for the course home page URL.
(def cs4278-brightspace "https://brightspace.vanderbilt.edu/d2l/home/85892")


;; Do not edit!
;; A map specifying the instructor's office hours that is keyed by day of the week.
(def instructor-hours {"tuesday"  {:start    8
                                   :end      10
                                   :location "the chairs outside of the Wondry"}

                       "thursday" {:start    8
                                   :end      10
                                   :location "the chairs outside of the Wondry"}})

;; underlying data structure that store the phone number
;; of student who registered the office hour for a class
(def Queue [])

;; This is a helper function that you might want to use to implement
;; `cmd` and `args`.
(defn words [msg]
  (if msg
      (string/split msg #" ")
      []))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the first word in a text
;; message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg]
  (first (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg]
  (rest (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg]
  (hash-map :cmd (cmd msg) :args (args msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to prefix the first of the args
;; in a parsed message with "Welcome " and return the result.
;;
;; Example:
;;
;; (welcome {:cmd "welcome" :args ["foo"]}) => "Welcome foo"
;;
;; See the welcome-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn welcome [pmsg]
  (str "Welcome " (first (pmsg :args))))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the CS 4278 home page.
;; Use the `cs4278-brightspace` def to produce the output.
;;
;; See the homepage-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn homepage [_]
  cs4278-brightspace)


;; Asgn 1.
;;
;; @Todo: Fill in this function to convert from 0-23hr format
;; to AM/PM format.
;;
;; Example: (format-hour 14) => "2pm"
;;
;; See the format-hour-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn format-hour [h]
  (if (= h 12)
    (str h "pm")
    (if (= h 0)
      (str (+ h 12) "am")
      (if (< h 12)
        (str h "am")
        (str (- h 12) "pm")))))



;; Asgn 1.
;;
;; @Todo: This function should take a map in the format of
;; the values in the `instructor-hours` map (e.g. {:start ... :end ... :location ...})
;; and convert it to a string format.
;;
;; Example:
;; (formatted-hours {:start 8 :end 10 :location "the chairs outside of the Wondry"}))
;; "from 8am to 10am in the chairs outside of the Wondry"
;;
;; You should use your format-hour function to implement this.
;;
;; See the formatted-hours-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn formatted-hours [hours]
  (str "from " (format-hour (hours :start)) " to " (format-hour (hours :end)) " in " (hours :location)))

;; Asgn 1.
;;
;; @Todo: This function should lookup and see if the instructor
;; has office hours on the day specified by the first of the `args`
;; in the parsed message. If so, the function should return the
;; `formatted-hours` representation of the office hours. If not,
;; the function should return "there are no office hours on that day".
;; The office hours for the instructor should be obtained from the
;; `instructor-hours` map.
;;
;; You should use your formatted-hours function to implement this.
;;
;; See the office-hours-for-day-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn office-hours [{:keys [args cmd]}]
  (if (contains? instructor-hours (first args))
    (formatted-hours (instructor-hours (first args)))
    "there are no office hours on that day"))

;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg]
  {:action :send :to to :msg msg})

;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msgs that takes
;; takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
  (map (fn [person] (action-send-msg person msg)) people))

;; Asgn 2.
;;
;; @Todo: Create a function called action-insert that takes
;; a list of keys in a `ks` parameter, a value to bind to that
;; key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; vound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
;;
(defn action-insert [ks v]
  {:action :assoc-in :ks ks :v v})

;; Asgn 2.
;;
;; @Todo: Create a function called action-inserts that takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
;;
;; In other words, this invocation:
;;
;; (action-inserts [:foo :bar] [:a :b :c] 32)
;;
;; would be equivalent to this:
;;
;; [(action-insert [:foo :bar :a] 32)
;;  (action-insert [:foo :bar :b] 32)
;;  (action-insert [:foo :bar :c] 32)]
;;
(defn action-inserts [prefix ks v]
  (map (fn [suff] (action-insert (conj prefix suff) v)) ks))

;; Asgn 2.
;;
;; @Todo: Create a function called action-remove that takes
;; a list of keys in a `ks` parameter and returns a map with
;; the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks]
  {:action :dissoc-in :ks ks})

;; This function add the TA to a particular class
;; and store its number and the class name in the expert map
(defn experts-register [experts topic id info]
  [(action-insert [:expert topic id] info)])

;; Asgn 3.
;;
;; @Todo: Create a function called "experts-unregister"
;; that takes the current application `state`, a `topic`
;; and the expert's `id` (e.g., unique name) and then
;; removes the expert from the list of experts on that topic.
;; Look at the associated test to see the expected function signature.
;;
;; Your function should NOT directly change the application state
;; to unregister them but should instead return a list of the
;; appropriate side-effects (above) to make the registration
;; happen (hint: action-remove).
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn experts-unregister [experts topic id]
  [(action-remove [:expert topic id])])

(defn experts-question-msg [experts question-words]
  (str "Asking " (count experts) " expert(s) for an answer to: \""
       (string/join " " question-words) "\""))

;; This is a helper function that calculates the number of people before one
;; particular student in the input
(defn query-info [user-id]
  (def res -1)
  (loop [x 0]
    (when (not= (get Queue x) user-id)
      (def res x)(recur (+ x 1)))) (def res (+ 1 res)) res)

;; This function enqueue one student and return to the student who
;; register the office hour the current queue information
(defn ask-experts [experts {:keys [args user-id]}]
  (def Queue (conj Queue user-id))
  (cond
    (nil? experts) [[] "There are no TAs on that class."]
    :else [[] (str user-id " successfully register for an office hour slot on " (first args) ", " (query-info user-id) " people before you.")]))


;; This is function will return the number of people before one
;; particular student in the input
(defn query-student [{:keys [args user-id]}]
  (str (query-info user-id) " people before you."))

;; This is the function that TA use to update the queue by dequeing the
;; first student and send back the confirmation message back to the TA
(defn notify []
  (def Queue (into [] (rest Queue)))
  "Your message was sent.")

;; function that notify the next student in the queue so that
;; this student will know that it is his or her turn to go to
;; visit the office hour
(defn answer-question [conversation {:keys [args user-id]}]
  (if (= 0 (count Queue))
    [[] "The Queue is Empty."]
    [[(action-send-msg (get Queue 0) "It's your turn.")] (notify)]))

;; This function adds TA to a class
(defn add-expert [experts {:keys [args user-id]}]
  [(experts-register experts (first args) user-id {:foo "bar"})
   (str user-id " is now a teaching assistant on " (first args) ".")])

;; Don't edit!
(defn stateless [f]
  (fn [_ & args]
    [[] (apply f args)]))


;; map that map the command to the corresponding function
(def routes {"default"  (stateless (fn [& args] "Unknown command."))
             "welcome"  (stateless welcome)
             "homepage" (stateless homepage)
             "office"   (stateless office-hours)
             "TA"   add-expert
             "query" (stateless query-student)
             "register"  ask-experts
             "notify"   answer-question})


;; Don't edit!
(defn experts-on-topic-query [state-mgr pmsg]
  (let [[topic]  (:args pmsg)]
    (list! state-mgr [:expert topic])))


;; Don't edit!
(defn conversations-for-user-query [state-mgr pmsg]
  (let [user-id (:user-id pmsg)]
    (get! state-mgr [:conversations user-id])))


;; Don't edit!
(def queries
  {"expert" experts-on-topic-query
   "ask"    experts-on-topic-query
   "answer" conversations-for-user-query})


;; Don't edit!
(defn read-state [state-mgr pmsg]
  (go
    (if-let [qfn (get queries (:cmd pmsg))]
      (<! (qfn state-mgr pmsg))
      {})))


;; Asgn 1.
;;
;; @Todo: This function should return a function (<== pay attention to the
;; return type) that takes a parsed message as input and returns the
;; function in the `routes` map that is associated with a key matching
;; the `:cmd` in the parsed message. The returned function would return
;; `welcome` if invoked with `{:cmd "welcome"}`.
;;
;; Example:
;;
;; (let [msg {:cmd "welcome" :args ["bob"]}]
;;   (((create-router {"welcome" welcome}) msg) msg) => "Welcome bob"
;;
;; If there isn't a function in the routes map that is mapped to a
;; corresponding key for the command, you should return the function
;; mapped to the key "default".
;;
;; See the create-router-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn create-router [routes]
  (fn [pmsg] (if (contains? routes (pmsg :cmd))
               (routes (pmsg :cmd))
               (routes "default"))))



;; Don't edit!
(defn output [o]
  (second o))


;; Don't edit!
(defn actions [o]
  (first o))


;; Don't edit!
(defn invoke [{:keys [effect-handlers] :as system} e]
  (go
    (println "    Invoke:" e)
    (if-let [action (get effect-handlers (:action e))]
      (do
        (println "    Invoking:" action "with" e)
        (<! (action system e))))))


;; Don't edit!
(defn process-actions [system actions]
  (go
    (println "  Processing actions:" actions)
    (let [results (atom [])]
      (doseq [action actions]
        (let [result (<! (invoke system action))]
          (swap! results conj result)))
      @results)))


;; Don't edit!
(defn handle-message
  "
    This function orchestrates the processing of incoming messages
    and glues all of the pieces of the processing pipeline together.
    The basic flow to handle a message is as follows:
    1. Create the router that will be used later to find the
       function to handle the message
    2. Parse the message
    3. Load any saved state that is going to be needed to process
       the message (e.g., querying the list of experts, etc.)
    4. Find the function that can handle the message
    5. Call the handler function with the state from #3 and
       the message
    6. Run the different actions that the handler returned...these actions
       will be bound to different implementations depending on the environemnt
       (e.g., in test, the actions aren't going to send real text messages)
    7. Return the string response to the message
  "
  [{:keys [state-mgr] :as system} src msg]
  (go
    (println "=========================================")
    (println "  Processing:\"" msg "\" from" src)
    (let [rtr    (create-router routes)
          _      (println "  Router:" rtr)
          pmsg   (assoc (parsed-msg msg) :user-id src)
          _      (println "  Parsed msg:" pmsg)
          state  (<! (read-state state-mgr pmsg))
          _      (println "  Read state:" state)
          hdlr   (rtr pmsg)
          _      (println "  Hdlr:" hdlr)
          [as o] (hdlr state pmsg)
          _      (println "  Hdlr result:" [as o])
          arslt  (<! (process-actions system as))
          _      (println "  Action results:" arslt)]
      (println "=========================================")
      o)))
