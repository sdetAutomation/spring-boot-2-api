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
    - go to UserService create method deleteUserById
        - first perform a findById, if present continue with delete function
    - go to Controller create method for deleteUserById
        - add @PathVariable for id input
    - unit test deleteUserById
        - write deleteUserById
            - create new record
            - make delete request to delete the record that was just created
            - verify response code
            - add auto wire UserRepository to use for verification
            - assert record not in database using UserRepository
11) implement getUserByUsername @GetMapping
    - go to UserRepository add method for findByUsername (custom method)
        - see notes added to this method
    - go to UserService create method getByUsername
    - go to UserController create method for getUserByUsername
        - add @GetMapping("users/byusername/{username}") - note path has addition, since searching by string vs Long
    - unit test getUserByUsername
        - write getUserByUsername
            - write getUserByUsername unit test
            
#### 04-exception-handling 
1) implement getUSerById
    - create UserNotFoundException class in exceptions folder
        - extend Exception class
        - create UserNotFoundException constructor
    - go to UserService
        - go to getUserById add throws UserNotFoundException
        - add if statement to check if user found, if not found throw exception and write custom message
    - go to Controller
        - add a try catch block and move getUserById into the try block
        - catch the exception UserNotFoundException
        - throw new ResponseStatusException - a default /error mapping, returning a JSON response with HTTP status 
            and the exception message.
        - add status code and pass the exception message from the service
    - unit test UserNotFoundException
        - write getUSerById UserNotFoundException unit test
        - since object response will not be a User, cast response entity as String.class
        - map response body to ObjectMapper
        - assert expected messages vs actual
    - disable stacktrace from response body
        - go to application.properties set following: server.error.include-stacktrace=never
2) implement updateUserById
    - go to UserService
        - go to updateUserById add logic to check if user is present, and throw exception if not found
    - go to Controller
        - add try catch block and move updateUserById into the try block
        - catch the exception UserNotFoundException
        - throw new ResponseStatusException 
        - add status code Bad Request and pass the exception from the service
    - unit test UpdateUserById
        - write UpdateUserById UserNotFoundException unit test
        - since object response will not be a User, cast response entity as String.class
        - map response body to ObjectMapper
        - assert expected messages vs actual
3) implement deleteUserById
    - go to UserService
        - go to deleteUserById add logic to check if user is present, and throw ResponseStatusException if not found
    - no changes needed to Controller
    - unit test DeleteUserById
        - write DeleteUserById ResponseStatusException unit test
        - since object response will not be a User, cast response entity as String.class
        - map response body to ObjectMapper
        - assert expected messages vs actual
4) implement createUser
    - create a UserExistsException class within exceptions class
        - extend Exception
        - create string constructor
    - go to UserService
        - go to createUser add logic to check if user is present, and throw UserExistsException if found
        - verify if user exist based on username (username is a unique constraint)
        - if not null throw exception
    - go to Controller
        - add try catch block and move createUser into try block
        - catch implement ResponseStatusException and pass thru the UserExistException message
    unit test CreateUser
        - write CreateUser ResponseStatusException unit test
        - since object response will not be a User, cast response entity as String.class
        - map response body to ObjectMapper
        - assert expected messages vs actual
5) add location header for createUserService
    - go to Controller
        - add UriComponentBuilder to createUser signature / as param
        - use HttpHeaders class
        - set location header using builder
        - build ResponseEntity<> return: pass in user object, header, set http status created
        - change method to return ResponseEntity<User>
        - remove previous annotation of @ResponseStatus
    - edit user_tc0002_createUser to also assert header location
        - add field to get HttpHeaders from response
        - assert expected location vs actual
        
#### 05-validations-global-exception-handling 

- commonly used validation annotations: @NotNull, @Size, @Min, @Max, @Email, @NotBlank, @NotEmpty

- global exception handling
    - @ControllerAdvice - allows to write global code that can be applied to wide range of controllers
        - by default @ControllerAdvice annotation will be applicable to all classes that use @Controller
        which also applies to @RestController
    - @ExceptionHandler - annotation for handling exceptions in specific handler classes or handler methods
        - if used with controller directly need to define per controller basis.  If used with @ControllerAdvice it will 
        only be used in Global Exception Handler class, but applicable to all controllers due to @ControllerAdvice
    - @RestControllerAdvice - combination of both @ControllerAdvice and @ResponseBody
        - we can use @ControllerAdvice annotation for handling exceptions in the RESTful Service but need to add a 
        @ResponseBody separately
- use case combination
    - @ControllerAdvice & ResponseEntityExceptionHandler class
        - MethodArgumentNotValidException
        - HttpRequestMethodNotSupporedException
    - @ControllerAdvice & @ExceptionHandler
        - for pre-defined exceptions like ConstraintViolations
        - for custom exceptions like UserNameNotFoundException
    - @RestControllerAdvice & @ExceptionHandler
        - for custom exceptions like UserNameNotFoundException
        - for pre-defined exceptions like "Exception.class" (applicable to all exceptions)

1) implement Bean Validation
    - Model / Entity Layer
        - implement validation for User Model
            - @NotEmpty(message="Username is a required field.  Please provide a username")
            - @Size(min=2, message="FirstName should contain at least 2 characters")
    - Controller
        - go to createUser method and add @Valid to params - this will validate the request body in the controller
        before it gets to the serviceLayer
        - if the validations do not pass, it will return a 400 and json error message 
    - fix unit test user_tc0010_createUser_Exception
        - since bean validation of firstName requires at lest 2 characters, must add td_FirstName in test data
2) implement Custom Global Exception Handler using @ControllerAdvice & ResponseEntityExceptionHandler
    - create new CustomErrorDetails class in exceptions package folder
        - define variables: date, message, errorDetails
        - add all fields constructor
        - add getters for fields
    - create CustomGlobalExceptionHandler
        - extend ResponseEntityExceptionHandler
        - add @ControllerAdvice annotation
            - global code that can be applied to wide range of controllers
        - hover and go to definition of ResponseEntityExceptionHandler find handleMethodArgumentNotValid method and copy
        - create method to implement and override handleMethodArgumentNotValid from RepositoryEntityExceptionHandler
            - add customErrorMessage in code - set date, custom error, and ex.getMessage
            - return a response entity with custom error details and HttpStatus.BAD_REQUEST
    
3) implement exception handler for HttpRequestMethodNotSupportedException
4) implement exception handler for custom exception UserNameNotFoundException
5) implement path variable validation & implement exception handler for ConstraintViolationException
6) implement Global Exception handler using @RestControllerAdvice
7) switching between @ControllerAdvice and @RestControllerAdvice


