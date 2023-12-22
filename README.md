## Contents
* [General Information](#general-information)
* [Demo](#demo)
* [Launch Yourself](#launch-yourself)
* [Current Features](#current-features)
* [Planned Features and Updates](#planned-features-and-updates)

# General Information
The aim of the project is for learning purposes, focus being on the backend. Plan is continuously learning and adding new things
([here](#planned-features-and-updates)). Focus will be mainly on the backend unless frontend changes are needed.
<br>
The project is about organizing the development process. You can do that by the use of companies, projects and teams. 
Inviting employees, managing their roles and permissions. Interacting with whiteboards and notes.

## Demo
**Coming soon!**

## Launch yourself

#### If a step isn't mentioned that means it wasn't necessary. You can always go the extra mile.

1. DB setup
   1. [Install PostgreSQL](https://www.postgresql.org/download/)
   2. [Create two roles](https://www.postgresql.org/docs/current/sql-createrole.html) - "Admin" and "Backend". 
   Give them at least "Can login" permission.
   3. (Optional) I recommend using pgAdmin for way easier time doing the next few steps
   3. [Create a database](https://www.postgresql.org/docs/current/sql-createdatabase.html) called "ProjectManager" (case is important)
   4. Do one of the two:
      - Execute the StartingPoint.sql file
      - Use the StartingPointCustom file to [restore the database](https://www.postgresql.org/docs/8.1/backup.html)

2. Clone the repository

3. Backend setup
   1. [Install Java](https://www.oracle.com/cis/java/technologies/downloads/#java17)
   2. Open the project using your preferred IDE
   3. Provide the needed environment variables 
      - --dbURLtype=postgresql (probably won't change)
      - --dbURLlink=localhost:5434 (default place postgresql runs on)
      - --dbURLname=ProjectManager (the name of the database)
      - --dbUsername=Backend (the username of the role)
      - --dbPassword=123 (the password of the role depending on what you set it to in step 1.2)
   4. Run the backend with maven build `mvn spring-boot:run` (this will install all the dependencies if they are missing)

4. Frontend setup
   1. [Install Node (16+) and npm](https://nodejs.org/en/download)
   2. Open a terminal and navigate to `*/ProjectManager/Client/my-app`
   3. Install the dependencies for the frontend with `npm install`
   4. Run the frontend with `npm run dev`

## Current features
* Create, edit, delete companies, projects and teams
* Roles and permissions throughout the website
* Interact with notes
* Invite employees

## Planned features and updates
* Real emails (currently you can use any email without verification)
* Upgrade to websockets in certain places (whiteboards)