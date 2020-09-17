# spring-boot-2-api

Sample project using Spring Boot 2 and Java

#### 01-springboot-init-commit
1) Create project at https://start.spring.io
2) Add dependencies (please check pom.xml) 
3) Build and Run app to verify app is working

#### 02-hello-world
1) Create Java class and add hello world code
2) Create a test for hello world endpoint
3) Create UserDetails Bean
    - Create UserDetails Model
    - Add method and mapping in HelloController
    - Write a test for new endpoint
    
#### 03-jpa-user-mgmt
1) Update application.properties
2) Create User Entity, @Entity, @Table, Define getter setters
3) Implement H2 database
    - In Memory Database, data will be lost when we restart JVM or when JVM reloads)
    - Prepopulate DB during runtime
        - test our entity and h2 database
        - start the application
        - navigate to H2 console: localhost:8080/h2-console
        - all fields should default.  Check "JDBC URL" to see if it matches this: `jdbc:h2:mem:testdb`
        - click connect, and now can see the table schema and fields on the next screen
    - Create a data.sql in src/main/resources (add insert statement)
        - test insert statement
        - follow steps above to start app and nave to h2 console.
    - Note: Columns will be in alphabetical order in DB except primary Key Id
    - Note: Thus, insert statement values should be in alphabetical order as displayed in H2 db.
4) Create User Repository:  UserRepository (Interface) (data layer that does all the database connections and operations)
5) Implement getAllUsers RESTful Service, @Service, @RestController
    - create UserService (business logic layer)
    - annotate with @Service
    - annotate with @Autowired (autowire UserRepository)
    - create getAllUsers methods
    - create UserController
        - annotate @RestController
        - annotate @Autowired (autowire userService)
        - create getAllUsers method
        - annotate getAllUsers method with @GetMapping + path
6) Test getAllUsers
    - start the app and nav to localhost:8080/users on your web browser
    - install postman and nav to to localhost:8080/users
    - write a unit test to test UserController method for getAllUsers
        - add junit dependency to pom
        - add test class for UserControllerTest and write test
        - add new application-test.properties file for test
        - execute unit test
7) Implement createUser @PostMapping
    - go to UserService / service layer create method for createUser
    - go to UserController create method for createUser 
        - add @PostMapping 
        - add @RequestBody to param of method.  This is where json body will be passed in.
    - unit test createUser 
        - create createUser helper function
        - write createUser unit test
8) Implement getUserById @GetMapping
    - go to UserService create method for getUserById
        - method signature is Optional<User> because the function may or may not find the user.
    - go to UserController create method for getUserById
        - method returns Optional<User>
        - add @PathVariable parameter which gets the id from the url
        - add @GetMapping("/users/{id}) the id is a param and comes from the url input
    - unit test getUserById
        - write getUserById unit test
9) Implement updateUserById @PutMapping
    - go to UserService create method for updateUserById
    - go to UserController create method for updateByUserId
        - add @PathVariable for id input, add @RequestBody for json body input
    - unit test updateByUserId
        - write updateByUserId unit test
            - retrieve an existing record
            - edit existing record and submit a put request using the edited entity
            - verify the response from the put call
            - make a get request to retrieve the record and assert all the fields updated
10) Implement deleteUserById @DeleteMapping
11) implement getUserById @GetMapping




