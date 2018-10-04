# Self-service QA application

# Project Overview
Based on the interview, I found that many students are struggling with getting help on their homeworks and average students need to spend
about half an hour getting help on their homework for each class every week. Also, professor sometimes would not respond to email quickly,
so we conclude the current question-answer process is not convenient enough. 

Consequently, we come up with a project to build a self-service quuestion and answer machine to make this process more fairly and conveniently,
which would potentially increase students' study efficiency. This project is built upon the text messaging application. Students can directly 
send their questions to the responder (professors or TAs), and the answer will be directly sent back to the student. The questions are sorted
by the time they were asked to ensure the correct and fair order of answering those questions. Also, we plan to have an urgent question 
functionality. We acknowledge that there are times that a question need to be answered quickly due to a variety of reasons. So, our application
have a separate queue for urgent questions that is marked and pinnedon the top, which are seen by responders first and can be addressed quickly. 
Every student can only have one opportunity to use the urgent question privilege each month. Also, if a question is asked multiple times, responders
have the option to send this question's QA to every student in the class.

We hope this application will streamline the class QA process and save more time for both students and responders (professors and TAs) while ensuring
the process is fair for everyone. Based on the interview, this application is positively received because the application is especially suitable for 
small and quick questions and will save more space and time for larger and more complex problems to be addressed in the office hour.


# Questions:
  1. Where do you get help if you have problems with homework?
  2. Why do you do this in this way?
  3. Did you encounter any difficulty by getting help this way?
  4. How much time is spent on getting help this way on average?
  5. How do you think that the help process on homework can be made more conveniently? 
  6. Do you think the help process is fair to everyone?
  7. what will you do if the professor's office hour conflicts with your other classes?
  8. Do you like to get the hint or answer on an Android or IOS application?
  9. Why do you like this application versus emailing the professor?
  10. what problems with piazza (class QA platform) will this app addresses? 

   
# Answers:

## Question 1: 
...Professor's office hour.
...classmate. If I can't check with with other students for any reason, I will go and see TA or professor.
...TA

## Question 2:
...Because professor's office hour is flexible, I can meet him by just setting up an appointment.
...Because I can't make it to professor's office hour as I have other classess at that time.
...Becuase TA is accessible and professor is not accessible 

## Question 3:
...Yes. There are many students asking question when I get there. So I need to wait for a long time.
...Other classmates don't know the answer, have to go to office hour to ask professor for help.
...Yes, I have other classes which has time conflict with the professor's office hour. And also,
   there are too many students waiting so I have to wait for a long time to get help.

## Question 4:
...at least half other
...10 minutes
...1 hour

## Question 5:
...The current process is convenient enough for me.
...Professor can answer have a group of students who has the same question.
...University can hire tutors who have taken the class before to answer questions.

## Question 6: 
...No, people may be on a time-sensitve situation to ask for help, while others do not.
...I think it is fair. The question should be answered in order of arrival to the professor's office.
...No, it is not fair if one student spent too much time on asking question so that others won't have opportunity to do so.

## Question 7:
...ask TA or post the question on piazza
...ask TA or talk with friends (if honor code of the classes allowed me to do so)
...ask TA 

## Question 8:  
...It is good for small problems, make more space for more complicated problem that need to be answered in person.
...It is suitable for quick questions.
...It is good for small questions.

## Question 9: 
...There are too many emails for professor, so he may not respond to your questions.
...Professor would not respond to the email quickly.
...It is easier for professor to see those question by seperating questions from other emails.

## Question 10:
...piazza don't allow students' question to be pinned on top if the question is urgent.
...I think piazza is a good platform for QA because students who have the same question don't need to ask the question again.
...piazza can't limit the number of questions student post. It is not fair if one student posts way more questions than others.

# Requirements
1. purpose
The purpose of this application is to facilitate the class QA process and save more time for students, TA and professor.

2. Intended Users
This project is a prototype for self-service question and answer application and is most useful in class settings. As such, it will be 
beneficial for professors, TAs, and college students.

3. Project Scope
This project allows students to get answer of small questions quickly from professors or TAs. Above all, we hope to provide a comfortable, fast, and fair classroom QA experiences.

4. Product perspective
This is a open source application developed using Clojure, which provides simple interface for classroom QA.

5. Product Feature
(1) Students can send their questions to professors and TAs.
(2) TAs can respond to the questions students asked.
(3) Professors and TAs have the option to broad cast one question and answer if they think the majority of the class would have the same questions.
(4) Questions shown to professor is sorted in the order by the time asked.
(5) The student has one chance per month to mark their question as urgent. As such, there are two question queues ordered by the time asked. The urgent one, which is pinned on top, and the other one is non-urgent question queue.

