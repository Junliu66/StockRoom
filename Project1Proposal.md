
### Project 1 Proposal ###

#### Stockroom Database ####

__Team Composition:__<br>
Chunlei Li<br>
Stefano Mauri	<br>
Christian Wookey<br>
Junliu Zhang	<br>
Andre Zhu<br>
<br>
10/04/2017<br>

---

#### Table of Contents ####

* 1 Project Overview 
  * 1.1 Abstract
  * 1.2 Target Customer
  * 1.3 Search Words
  * 1.4 Scope and Objectives
* 2 Team and Constraints
  * 2.1 Team Profile
  * 2.2 Challenges
  * 2.3 Assumptions and Constraints
* 3 Deliverables and Milestones
 * 3.1 Project Deliverables
 * 3.2 Schedule and Budget Summary


---

## 1.0	Project Overview
### 1.1 Abstract


*Tracking the lifecycle and quantity of parts used in a manufacturing company.*

This project will allow a small manufacturing company keep track and maintain their stockroom, by warning them of shortages in their supply or informing them of when their stocks are running low so they can avoid shortages in the future. If your company gets an order for a product to be built, it makes it easier for an employee to kit the necessary parts for manufacturing, warn the Purchasing Department of missing parts for that product that need to be purchased and notify the Shipping and Receiving of where the received parts are needed before the rest gets put in the stockroom. Allows Manufacturers to request the extra parts they need and gain visibility on when they are unable to finish their products. Finally, our project can also give a supervisor an "at a glance" view of the company by showing what parts are coming in, what products are being made, and what is ready to be shipped.


### 1.2.	Target Customer 

Our target customer would be owners of small to medium sized manufacturing companies, or any company that has to maintain and keep track of a stockroom for their business. Our project would like to have custom UIs for the different roles that the users might have within the company, such as a page for purchasing, shipping and receiving, stockroom manager, manufacturers, and supervisors.

### 1.3.	Search Words 

stockroom manager, inventory management, inventory system, inventory management software, inventory control software, inventory, stockroom, manufacturing company software, manufacturing

### 1.4.	Scope and Objectives 

- A MySQL Database
- A Graphic User Interface
- Sanitizing and handling user inputs
- Creating complex and scalable SQL queries
- Creating individual UIs for Purchasing, Shipping and Receiving, Manufacturers, Kitters, Stockroom and Supervisors

---

## 2.0	Team and Constraints

### 2.1.	Team profile<br>

***Chunlei Li***<br>
**Qualifications:**:I finished Java 1A classes，also had an experience work at a company that really need some good software to help with their stock managing. That’s why i feel like it will be super helpful if we could create the software that can deal with the similar issue that may happen in any companies. It will definitely save lots of manfully work and lower the miscounting or recording on any similar stocking system of  the companies, such as libraries, community services, manufactories.
 <br>
**Strengths**: I believe I have a strong sense of responsibility, especially when we work as a team. Also, great communication skill. I could learn new things efficiently with full of energy. I always have a positive attitude to life and like new challenges, because it feels fantastic when i could figure difficulty problems out.
<br>

***Stefano Mauri***	<br>
**Qualifications:**: Experience in Java, MySQL and HTML<br>
**Strengths**: Personal experience working as a stockroom manager and in shipping and receiving for a small business.<br>

***Christian Wookey***<br>
**Qualifications:**: Programming Experience: Objective-C, C, C++, Python, Lua. Design Experience: Adobe Photoshop, Krita, Adobe Illustrator, iOS design standards.<br>
**Strengths**: I have experience programming for UI/UX design. I do graphical design on a semi-regular basis. I have experience working in an Amazon Prime warehouse, which uses a virtual inventory system similar to the one we are implementing.<br>

***Junliu Zhang***	<br>
**Qualifications:**: I have taken some Java courses before. So I basically know how to use Java for programming. I also learned knowledge about quality assurance, which is quite related to software engineering. Moreover, I had some project experience in the school and company which gave me the idea of how the project works. My accountant experience also let me understand the importance of shipping and tracking products for a company and provided me the insight of how to make  stockroom system better.<br>
**Strengths**: I’m a quick learner and work well under pressure. I’m responsible to my work and take every small part seriously. Honestly I’m good at working on some small parts and able to find some small mistakes on them. I’m a very gentle and thoughtful person and I believe I can be a good team member as well. <br>


***Andre Zhu***	<br>
**Qualifications:**:I have a decent amount of computer science coursework and a couple of projects under my belt. I have experience with MySQL and databases in general, which should be useful for this project in particular.
**Strengths**:I do well in program design and implementation, as well as working until something is finished once I have started it. 

### 2.1.	Challenges

- One of the main challenges we will face is creating good scalable SQL queries that will work with larger databases that the customer might use.

- Sanitizing inputs so we are not vulnerable to SQL injections.

- Ideal goal is find a company could try to use our program do the test.

- Time frame seems short, so we have to allocate our team wisely based on our background and strength.

- It is our first time to work together, we need time to have a better understanding of each other also finish the project perfectly.


### 2.2.	Assumptions and Constraints
####Assumptions:
***Meetings***<br>
 - Team will be meeting twice before and after class.
    - Tuesdays, 1:00 - 2:00 PM, Library
    - Thursday, 4:00 - 5:00 PM, Library
 - Scheduled library meetings and additional/emergency meetings may fluctuate based on the Team’s project progress.
    - If the Team finishes the weekly task before the scheduled deadline, scheduled library meetings may be cancelled (with a unanimous vote from the entire Team).
    - If a team member misses more than the maximum meetings allotted, the rest of the Team will convene and decide if further action (termination) is appropriate.
    - If a team member misses ANY meeting without timely notice to the rest of the team, Bita will be notified and individual point will be deducted from that team member’s score for that weekly task.

***Attendance***<br>
- Team members must attend every class and meeting.
    - Team member may miss no more than one class meeting AND one library meeting. (with timely notice to the rest of the Team) during the duration of the assignment.
      If a team member is not available to physically attend a meeting, they may attend the meeting via video chat (eg. Google Hangouts, Skype) without penalization.

***Communication***<br>
- Team will be using Canvas mail tool, Google Hangouts and group texts to communicate.
    - Team members must send out a message to the rest of the Team once they have completed their job.
    - Team members must respond to messages requiring a response within one hour (unless a timely notice is given to the rest of the Team).
    - If the Team decides an additional/emergency meeting is in order, then the meeting can be conducted either physically, in person, or virtually, by means of phone calls or video chats.
***Deadlines***<br>
- Deadlines are posted on the team schedule: https://goo.gl/4uPGc7
    - Each team member must meet deadlines when submitting drafts, giving feedback ,to other team members, and turning deliverables in to Bita..
    - If a team member misses one deadline, a warning will be given, and that team member will be required to submit feedback for the next task 24 hours in advance from the following deadline.
    - If a team member misses two deadlines, the rest of the Team will convene and decide if further action (termination) is appropriate.
    - If a team member misses more than one deadline, the rest of the Team will convene and decide if further action (termination) is appropriate.

***Files***<br>
- Team will be sharing files using Google Drive.
    - Google Drive:https://goo.gl/DoS5ef

***Backup and Recovery***<br>
- Requirements for task not met
    - If the requirements of the task submitted to Bita are not met and bounced back, an additional meeting (physical or virtual) must be held to discuss and fix all issues with the task, and re-submit as soon as possible.
- Team member is uncommitted
    - If a team member is constantly missing deadlines, unresponsive to messages, and not attending meetings, the rest of the Team will convene and decide if further action (termination) is appropriate.
- Team member is uncooperative
    - If a team member is unwilling to consider other team members’ ideas and/or input, or is exhibiting negative behavior/attitudes towards the Team and the assignment, the rest of the Team will convene and decide if further action (termination) is appropriate.


***IN WITNESS WHEREOF, the parties hereto have executed this assumptions on the date first above written.***<br>
1. Chunlei Li
2. Stefano Mauri	
3. Christian Wookey
4. Junliu Zhang	
5. Andre Zhu

***Constraints***<br>
* Non-Technical Constraints:
  * Time Limits: 5 weeks total for the project to be done
  * Budget: project need to be done under lowest cost
  * Database: need data of shipping, receiving and inventory from company 

* Techinical Constraints:
  * Tools: MySQL for database of stockroom, IntelliJ
  * Operating System: version Windows 2000/xp or above


---

## 3.0	Deliverables and Milestones

### 3.1.	Project Deliverables 


Deliverables include:
*	Software System Project Proposal.
*	[design document]
*	[user manual instructions]
*	[presentations]
*	[UML overview of different components in your system]
*	[implementation code]
*	[unit tests and test suites (see NOTE below)]
*	[This list may include product deployment activities that are necessary for the successful use of the system your team develops - we may contact some companies see if they would love to try it.]

 *<sup><a name="footnote_4">4</a></sup>Note: This list may be modified over the weeks until the final submission.* 

NOTE: Required by final submission of project.


### 3.2.	Schedule and Budget Summary 



#### Milestones

| Item                                      | Date             |
| :-----------------------------------------|:-----------------|
| Project Proposal                          | October 4, 2017  |
| Proposal Presentation                     | October 5, 2017  |
| [MySQL Database & User Interface]         | October 08, 2017 |
| [User inputs & SQL queries]               | October 15, 2017 |
| Purchasing, Shipping and Receiving, Manufacturers Components | October 22, 2017 |
| Creating individual UIs Kitters, Stockroom and Supervisors   | October 29, 2017 |
| Finalize the details                      | October 31, 2017 | 
| Demonstration and Delivery                | November 2, 2017 |
