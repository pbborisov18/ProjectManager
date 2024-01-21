## Contents
* [General Information](#general-information)
* [Demo](#demo)
* [Launch Yourself](#launch-yourself)
* [Current Features](#current-features)
* [Planned Features and Updates](#planned-features-and-updates)

# General Information
The project is about organizing the development process with a typical kanban board. 
On top of that you have the ability to be part of companies, projects and teams.
Inviting employees, managing them, their roles and permissions. Interacting with whiteboards and notes.
<br><br>
The aim of the project is for learning purposes. Plan is to continuously apply what I learn
and having a platform on which I can do so. 
Focus will be mainly on the backend unless changes on the frontend are needed.
## Demo
**DISCLAIMER: The frontend has been mainly made for 1080p resolution. I understand that's not ideal and how important this is, but as I said earlier, the focus of this project is the backend.**
<br><br>
[Live demo](https://agile-ace.tech)

https://github.com/pbborisov18/ProjectManager/assets/56866287/0c12b121-c5a6-4a46-8811-e80ca18b6d55

## Launch yourself

#### If a step isn't mentioned, that means it wasn't necessary.

1. DB setup
   1. [Install PostgreSQL 16+](https://www.postgresql.org/download/)
   2. [Create two roles](https://www.postgresql.org/docs/current/sql-createrole.html) - "Admin" and "Backend". 
   Give them at least "Can log in" permission.
   3. [Create a database](https://www.postgresql.org/docs/current/sql-createdatabase.html) called "ProjectManager" (case is important)
   4. Do one of the two:
      - Execute the StartingPoint.sql file
      - Use the StartingPointCustom.sql file to [restore the database](https://www.postgresql.org/docs/8.1/backup.html)

2. Clone the repository

3. Backend setup
   1. [Install Java](https://www.oracle.com/cis/java/technologies/downloads/#java17)
   2. Open the project using your preferred IDE
   3. Provide the necessary environment variables 
      - `--dbURLtype=postgresql` (probably won't change)
      - `--dbURLlink=localhost:5434` (default place postgresql runs on)
      - `--dbURLname=ProjectManager` (the name of the database)
      - `--dbUsername=Backend` (the username of the role)
      - `--dbPassword=123` (the password of the role depending on what you set it to in step 1.2)
      - `--frontendUrl=http://localhost:5173` (the default url the frontend runs on)
   4. Run the backend with maven build `mvn spring-boot:run` (this will install all the dependencies if they are missing)

4. Frontend setup
   1. [Install Node (20+) and npm](https://nodejs.org/en/download)
   2. Create a file named `.env` in `Client/my-app`
      - `VITE_BACKEND_URL='http://localhost:8080'` (or whatever port you set the backend to run on)
   3. Open a terminal and navigate to `*/ProjectManager/Client/my-app`
   4. Install the dependencies for the frontend with `npm install`
   5. Run the frontend with `npm run dev`

Annnnnnnnd you're done! You can check out the site at http://localhost:5173 (default port svelte runs on)

## Current features
* Create, edit, delete companies, projects and teams
* Change roles and permissions for users
* Interact with notes
* Invite employees

## Planned features and updates
* Real emails (currently you can use any email without verification)
* Upgrade to web sockets in certain places (whiteboards)
* Optimize query performance in certain places
* Give the ability to play with the columns in the whiteboard (backend is ready to handle that)

<br>
<a href="https://www.flaticon.com/authors/freepik" title="settings icons">Settings, leave, plus, rectangle and settings icons created by Freepik</a><br>
<a href="https://www.flaticon.com/authors/pixel-perfect" title="right icons">Check, delete and edit icons created by Pixel perfect</a><br>
<a href="https://www.flaticon.com/authors/tempo-doloe" title="invite icons">Invite icon created by Tempo_doloe</a><br>

