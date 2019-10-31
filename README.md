# Prerequisites
1) npm: '6.9.0'
2) @angular/cli 8.3.15
3) JDK 8

# Run instructions
Backend:

1) open project as gradle project and update all dependencies
2) run YoutubeappApplication.java class  (make sure port 8080 is available)
3) You might want to install Lombok plugin in your IDE if not building

Frontend:

1) cd to youtubeapp-frontend directory
2) run `npm install`
3) run `npm start` or `ng serve`

1) **You can access the website by opening the browser at http://localhost:4200/**
2) **You can access the database by opening the browser at http://localhost:8080/h2-console/**


# Designdoc

Application uses the Angular as frontend and and Spring Rest API as backend. Authentication is implemented with a JWT using Spring Security Module. Each API request is first authenticated/validated except the public endpoints such as /login & /registration.

Backend application provides the JWT token after successful validation of user credentials. Invalidates the token after specified expiry time.

Frontend application intercepts each http request to the backend to add the Bearer JWT to the request (if available).

Data is stored in the embedded h2 database with Spring's JPA module.

Each user contains the country, jobInterval and the information about top video/comment per requirements. Also next update date is maintained for each user.

Spring scheduler triggers every minute to check all users that need to be updated (by next update date) and updates the corresponding video/comment if needed.

While frontend client application is querying the API in live mode to fetch the user information containing the video/comment and updates the user interface if changes have happened.

User registration fields are validated both on the client and the backend side.

*Country list is not implemented and user has to manually enter the country name with ISO standards*
