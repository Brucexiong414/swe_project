# Self-service QA application

# Project Overview
Based on the interview, I found that many students are struggling with getting help on their homeworks and average students need to spend
about half an hour getting help on their homework for each class every week. Also, professor sometimes would not respond to email quickly,
so we conclude the current question-answer process is not convenient enough. 

Consequently, we come up with a project to build a self-service quuestion and answer machine to make this process more fairly and conveniently,
which would potentially increase students' study efficiency. This project is built upon the text messaging application. Students can directly 
send their message to register the office hour, and get the information about the line. TA or professor can notify the next student in the line. The registration of student is sorted by the time they were asked to ensure the correct and fair order of answering those questions. Also, we plan to have an urgent question 
functionality. We acknowledge that there are times that a question need to be answered quickly due to a variety of reasons. So, our application have a separate queue for urgent questions that is marked and pinnedon the top, which means they can go to the office hour first and their questions be addressed quickly. 
Every student can only have one opportunity to use the urgent question privilege each month. Also, if a question is asked multiple times, responders have the option to send this question's QA to every student in the class.

We hope this application will streamline the class QA process and save more time for both students and responders (professors and TAs) while ensuring
the process is fair for everyone. Based on the interview, this application is positively received because the application is especially suitable for 
small and quick questions and will save more space and time for larger.


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
...piazza don't allow students' question to be pinned on top if the question is urgent. ...I think piazza is a good platform for QA because students who have the same question don't need to ask the question again.
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

(1) Students can register for the office hour time.
(2) students can retrieve the queue information about the office hour.
(3) Students can grade their TAs.
(4) TA can update the queue and notify other students when they finish answering the question or canceling the office hour.
(6) Registration information of students shown to professor and TA is sorted in the order by the time asked.
(7) The student has one chance twice a month to mark their registration as urgent. As such, there are two registration queue. The urgent one, which is pinned on top, and the other one is non-urgent question queue.

# Development Approach

For this project, we choose the classical software waterfall model as our process.
The complete development phases of the project is illustrated in the following figure: 

![development process](https://user-images.githubusercontent.com/31359262/46516317-aa89ab80-c82e-11e8-84e2-d13411cb6933.jpg)

First, based on the user story, we gather the basic requirements for the application, which is specified above in the requirement section. The core requirement includes: 

(1) Students can register for the office hour time.
(2) students can retrieve the queue information about the office hour.
(3) Students can grade their TAs.
(4) TA can update the queue and notify other students when they finish answering the question or canceling the office hour.
(6) Registration information of students shown to professor and TA is sorted in the order by the time asked.
(7) The student has one chance twice a month to mark their registration as urgent. As such, there are two registration queue. The urgent one, which is pinned on top, and the other one is non-urgent question queue.

Second, we begin our design. 
Data structure: A queue in sorted order by the time the student register for a office hour. A queue for urgent registration. A vector holds all student and a map that map a student's name to its corresponding phone number. A vector holds all TA's name and professor's name.
workflow: 
User Interface: Students can be added to the class when they click the ENROLL button and type in their phone number.
Students can see the number of people in queue in the page they choose to register for a office hour slot. 
TA can click the button to notify the next student to be ready for the office hour and can click the other button to cancel its office hour.
Student can check the box of "urgent question" to mark their registration for the office hour as urgent.

Possible failing condition:
(1) If one student is notified to come to the office hour and never show up, other students in the queue can never have their question answered.
They are handled by the timer feature. If a student haven't showed up in five minutes, he or she is automatically canceled. 

(2) If the TA cancels his office hour at a time close to the start of the office hour time, many students who register the office hour will not be aware of it and will be disappointed to find out that the office hour is canceled when they visit TAs' office hour.
This condition is handled by broadcasting the office hour canceling information to every student in the class to keep them informed.

Then, we will begin code our application. We will utilize test-driven software development strategy to mitigate the risk of building the wrong application. Also, we make sure our code can be broken into small parts that are easy to test and easy to change so that if some of our initial assumptions are failing, we can quickly fix the problem. 

After we finished coding, we begin our testing. We use two kinds of testing strategy, the unit test and the integrated test. Through unit testing, we can exam whether one particular function's input and output are expected and through integrated test we know if the application meet our expectation in practice and systematically uncover the error associated with the interface. By combing these two testings, we can make sure our application works in unity.


