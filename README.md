## Contents
* [General Information](#general-information)
* [Launch](#launch)
* [Features](#features)

# General Information
The aim of the project is to organize and manage teams. You can create companies, projects and teams. Invite employees. Interact with notes and whiteboards. Created for a school project.

## Launch
1. [Install Java](https://www.oracle.com/cis/java/technologies/downloads/#java17)
2. [Install Node (16+) and npm](https://nodejs.org/en/download) 
3. [Install SQL Server Express](https://www.microsoft.com/en-us/sql-server/sql-server-downloads)
4. [Install SSMS](https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-ver16) (optional)
5. Clone the repository
6. Execute the .sql file
7. [Create a login to your database](https://docs.microsoft.com/en-us/sql/relational-databases/security/authentication-access/create-a-login?view=sql-server-ver16)
   - Username: sa
   - Password: 123
8. Open your preferred IDE and run the backend with maven build `mvn spring-boot:run` (this will install all the dependencies if they are missing)
9. Open a terminal and navigate to `*/2223-dzi-java-pbborisov18/Client/my-app/`
10. Install the dependencies for the frontend with `npm install`
11. Run the frontend with `npm run dev`

## Features
* Create, edit, delete companies, projects and teams
* Roles
* Interact with notes
* Invite employees
