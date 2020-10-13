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
    - Bean - is an object that is instantiated, assembled, and managed by a Spring
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
        - HttpRequestMethodNotSupportedException
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
    - go to CustomGlobalExceptionHandler
    - hover and go to definition of ResponseEntityExceptionHandler find HttpRequestMethodNotSupportedException copy
    - paste HttpRequestMethodNotSupportedException function, and refactor with custom message
        - add @Override and add custom message and error code
    - create unit test check if patch throws exception
        - create user_tc0011_updateUserById_patch_CustomException
        - make a post call and set patch in the url path due to bug with restTemplate and patch function
        
4) implement exception handler for custom exception UserNameNotFoundException
    - create new UserNameNotFoundException class in exceptions package folder
        - extend Exception
        - add Constructor for string
    - Controller
        - edit getUserByUsername function to throw UserNameNotFoundException if user does not exist in repository
        - check if username exists, if not throw UserNameNotFoundException
    - CustomGlobalExceptionHandler
        - create handleUserNameNotFoundException method
        - annotate with @ExceptionHandler(UserNameNotFoundException.class) - pass in custom Exception.class
        - this function will pass thru the custom error message and return a ResponseEntity with error code
    - create unit test for getUserByUsername_Exception
        - create user_tc0012_getByUsername_CustomException
        - make get call with a bad username and assert error message
        
5) implement path variable validation & implement exception handler for ConstraintViolationException
    - go to Controller
        - getUserById - add @Min(1) for path variable
        - add @Validated annotation to UserController
    - create handleConstraintViolationExceptionHandler and add with custom message and error code Bad Request
    - create unit test user_tc0013_getByUserId_constraint_CustomException
        - make get call with user id < 1 and assert error message
    
6) implement Global Exception handler using @RestControllerAdvice
    - go to GlobalExceptionHandler 
        - comment out @ControllerAdvice (only if you want to use @RestControllerAdvice)
    - create new class GlobalRestControllerAdviceExceptionHandler
        - annotate with @RestControllerAdvice
        - create usernameNotFound Method
            - add annotation @ExceptionHandler(UsernameNotFoundException.class)
            - add annotation @ResponseStatus(HttpStatus.NOT_FOUND)
            - return CustomErrorDetails message

7) switching between @ControllerAdvice and @RestControllerAdvice
    - to switch between the 2 difference advices
        - to use @RestControllerAdvice - comment out @ControllerAdvice from GlobalExceptionHandler
        - to use @ControllerAdvice - comment out @RestControllerAdvice from GlobalRestControllerAdvice

#### 06-jpa-oneToMany-association 

- JPA - one to many database association can be represented either through @ManyToOne or @OneToMany
- @ManyToOne annotation allows us to map the Foreign Key column in the child entity mapping so that the child
    has an entity object reference to its parent entity

1) create order entity and @ManyToOne association
    - create Order model / entity
        - annotate with @Table(name = "orders")
    - add order fields
        - add order_id & order_description
        - add annotations to order_id
        - add User variable
            - add @ManyToOne(fetch = FetchType.LAZY) 
            - add Fetch type as lazy - unless a call to order.getUser this user will not be fetched when a request comes
        - add @JsonIgnore - when an order object is created it will not expect user data to be sent
    - add getters and setters
    - add NoArgument Constructor

2) update user model / entity with @OneToMany association
    - add orders variable field
    - add @OneToMany Mapping - one user to many orders
        - add MappedBy to user variable in Order Entity (mappedBy="user")
        - order is the owner of the relationship. We don't want to create a foreign key in both tables, we can make user
          as the foreign key, which will create a column for user in the order table.  User side is the referencing side
    - add getters and setters for "orders"
    - update data.sql
        - first start the app, then nav to http://localhost:8080/h2-console/
        - check orders table for correct columns: ORDER_ID, ORDER_DESCRIPTION, USER_ID
        - add insert statement to data.sql file
    - start the app and check getAllUsers and you should see order data within the response body
    
3) implement getAllOrders method
    - go to Users Controller - add @RequestMapping("/users") at class level
        - remove "/users" at method level for all User related methods
        - this will help when creating self links in HATEOAS section
    - create OrderRepository (Interface) (data layer that does all the database connections and operations with Order)
        - extend JpaRepository<Order, Long> 
    - create OrderService class
        - add @Service at class level
        - @Autowire userRepository - used for finding a userById
        - write method: add try catch / error handling, return order is if user found
    - create OrderController 
        - add @RestController at a class level
        - add @RequestMapping("/orders")
        - add @Autowired for OrderService
        - write method for getAllOrdersByUserId
            - add @GetMapping("/{userId}")
            - add try catch and exception handling
    - create unit test for getAllOrdersByUserId
        - create OrderControllerTest class
        - add the following annotations
        ```
            - @RunWith(SpringJUnit4ClassRunner.class)
            - @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
            - @ActiveProfiles("test")
            - @FixMethodOrder(MethodSorters.NAME_ASCENDING)
        ```
        - add @Autowired for restTemplate
        - add final String path for "orders/"
        - write unit test order_tc0001_getAllOrderByUserId
            - testing happy path 
        - write unit test order_tc0002_getAllOrderByUserId_Exception
            - testing exception message if user not found 
 
4)  - go to OrderService and add method for pulling records from repository
    - go to Controller add method for getting all orders from the service
    - write unit test for getAllOrders endpoint
    
5) implement createOrder method
    - go to OrderService and create createOrder method
    - go to OrderController and create createOrder method
    - write unit test for createOrder 
        - test happy path
        - test exception message if user not found

6) implement getOrderByOrderId
    - create OrderNotFoundException class 
    - go to OrderService and create getOrderByOrderId method
    - go to OrderController create method getOrderByOrderId
    - write unit test for getOrderOrderId
        - test happy path
        - test exception message if user not found
    
#### 07-complete-order-api 

1) implement updateOrderById
    - go to OrderService and create updateOrderById
    - go to OrderController create updateOrderById
    - write unit test for updateOrderById
        - test happy path
        - test exception message if user not found
    
2) implement deleteOrderById
    - go to OrderService and create deleteOrderById
    - go to OrderController create deleteOrderById
    - write unit test for deleteOrderById
        - test happy path
        - test exception message if user not found

#### 08-integration-testing

1) write user repository integration tests
    - create UserRepositoryIntegrationTest class
    - test CRUD operations

2) write user service integration tests (mockito) 
    - add a UserServiceImpl class
    - refactor UserService to be an interface, and migrate all method body's to UserServiceImpl class
    - create UserServiceImplIntegrationTest class
        - see notes in test file for annotations and class setup

3) write UserControllerTest (@WebMvcTest)
    - rename UserControllerTest from before to UserIntegrationTest
    - create new UserControllerTest class
    - add annotations, please see test file, write tests
    
4) write order repository integration tests
    - create OrderRepositoryIntegrationTest class
    - test CRUD operations
    
5) write order service integration tests (mockito) 
   - add a OrderServiceImpl class
   - refactor OrderService to be an interface, and migrate all method body's to UserServiceImpl class
   - create OrderServiceImplIntegrationTest class
       - see notes in test file for annotations and class setup
           
6) write OrderControllerTest (@WebMvcTest)
    - rename OrderControllerTest from before to UserIntegrationTest
    - create new OrderControllerTest class
    - add annotations, please see test file, write tests
    
#### 09-hateoas

- hateoas is used to present information about a REST api to a client without the need to bring up the api documentation
- includes links in a return response which can be used by the client to further communicate with the server
- Spring hateoas provides 3 abstractions for creating uri
    - resource Support
    - link
    - ControllerLinkBuilder

0) add hateoas to dependency pom.xml
    - see hateoas in pom.xml file

1) extend both models to ResourceSupport
    - go to Order model / entity to extend ResourceSupport 
    - go to User model / entity to extend ResourceSupport 
        - User model will have a syntax error
        - since "id" is a field within hateos, must change our id field to userId
        - regenerate getters and setters, constructor, and toString 
    - refactor unit test & integration test
        - test will break
        - update order insert statement for data.sql file
        - run all test and slowly update and fix tests that need to reference the new userId and constructor
    
2) create new User and Order Controllers for hateoas implementation
    - create UserHateoasController
        - annotate with @RestController, @RequestMapping, @Validated
        - Autowire UserRepository
        - copy methods getUserById, getAllUsers from UserController
    - create OrderHateoasController
        - annotate with @RestController, @RequestMapping, @Validated
        - Autowire UserRepository & OrderRepository
        - copy method getAllOrders from OrderController

3) implement self link in getUserById method
    - update dependency in pom.xml (previous dependency was incorrect)
    
    ```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-hateoas</artifactId>
		</dependency>
    ```

    - refactor getUserById
        - update return to Resource<User>
        - write code for selflink and add to user return object
    - write unit test for getUserById hateoas
        - create UserIntegrationHateoasTest 
        - write test to verify selflink is returned
        
4) implement self link in getAllUsers, and relationship link with getAllOrdersByUserId
    - refactor getAllUsers hateoas
        - implement selflink for each user on list / add loop and selflink logic
        - create relationship link with getAllOrderByUserId
            - add and refactor getAllOrdersByUserId hateoas
                - copy getAllOrdersByUserId method from OrderController
                - change return type to Resources<Order>
                - refactor to have all order return as Resources
            - go back to getAllUsers and add relationship link with getAllOrderByUserId
                - under the user.add(selfLink) line, add code to get Resource<Order> and add to user
            - create a selflink for getAllUsers
                - below the loop block add selflink code, and add to resources 
    - refactor getAllOrders to return Resource<Order>
    - write unit test for getAllOrders, getAllUsers
        - create OrderIntegrationHateoasTest 
            - write test for getAllOrders & getAllOrdersByUserId
        - add getAllUsers test to verify selflink and links returned for each users on list

#### 10-internationalization

- Internationalization is the process of making our application adaptable to different languages.  

- spring boot provides LocaleResolver and ResourceBundleMessageSource which will handle the internationalization

1) create required beans and message property files per language
    - define a bean named LocaleResolver
        - go to SpringBoot2ApiApplication class file
        - create method for LocaleResolver
        - set default as Locale.US
        - add @Bean annotation
    - add property files for messages
        - add messages.properties
        - add messages_cn.properties
        - add messages_de.properties
   
2) create rest service to convert it to support internationalization
    - ResourceBundleMessageSource
        - go to SpringBoot2ApiApplication class file
        - create method for ResourceBundleMessageSource
        - add @Bean annotation
        - Create a getMessagesInI18NFormat method
        - go to HelloController
        - add a new method getMessagesInI18NFormat 
            - add proper annotations for method and ResourceBundleMessageSource field
            - please see example in HelloController for proper setup
        - write unit test to check configurations / methods / controller are setup correctly
            go to HelloTest and add tests for all languages
    - simplify getMessagesInI18NFormat method
        - please see example in HelloController for proper setup
        - remove params from method
        - leverage LocaleContextHolder.getLocale()
        - write unit test to check configurations / methods / controller are setup correctly
            - create helper function for creating a header with Accept-Language

#### 11-filtering

- apply @JsonIgnore at the field level in model class
- apply @JsonIgnoreProperties at the class level in model, and define list of fields that can be ignored
- these annotations simply hides the fields from the Jackson parser

1) static filtering
    - Model / entity level
    - @JsonIgnore apply to SSN field
    - @JsonIgnoreProperties apply to firstname and lastname
    - go to User model
        - add @JsonIgnore to ssn field
        - add @JsonIgnoreProperties({"firstname", "lastname"}) at the class level
        - due to "Ignore" statements above, firstname, lastname, and ssn will no longer be returned in the response.
        it will break existing tests.  The fix is to make firstname, lastname, and ssn nullable
        - set firstname, lastname, and ssn to nullable=true
    - fix broken test - comment out asserts that check firstname, lastname, and ssn

2) dynamic filtering 
    - @JsonFilter
    - Rest logic related to filtering will be defined in service or controller
    - implement with a basic has set
    - send fields using REST service query parameters to retrieve the data for those respective fields
    
    - Create new UserMappingJacksonController class
        - copy getUserById method from UserController
        - add proper annotations
    - go to User Model and comment out @JsonIgnore and @JsonIgnoreProperties and revert nullable to false
    - update / comment in code from UserControllerTest to check for firstname, lastname, and ssn
    
    - go to UserMappingJacksonController
        - edit getUserById code
        - change return type to MappingJacksonValue
        - add code to define fields that should be seen, use FilterProvider and MappingJacksonValue to return mapped value
        - add name given in FilterProvider ("userFilter") to User Model as @JsonFilter("userFilter") annotation at the class level

    - convert dynamic filtering by using @RequestParams
        - go to UserMappingJacksonController
        - copy paste getUserById and rename method to getUserById2
            - add params to @GetMapping
            - add @RequestParam for fields
            - remove hardcoded field values
    
    - user @JsonView to customize views
    - useful if we have a single model which needs to provide different views to different category of clients
        - example: a view for external view vs internal view (example: employee management: normal vs manager vs hr view)
    - in User Model comment out @JsonFilter("userFilter") -- this is for MappingJacksonValue filtering
    - in the model folder create a Views class
        - create 2 static classes (external, internal)
    - in the User Model
        - annotate fields in User Entity with @JsonView
        - decide which fields should be external and internal and annotate accordingly
    - in the Controller Layer
        - create a new controller UserJsonViewController
        - copy getUserById method from UserController and create 2 methods
            - external getUserById
                - @JsonView(Views.External.class)
            - internal getUserById2
                - @JsonView(Views.Internal.class)
    - in the Order model
        - annotate fields in Order Entity with @JsonView
    - write unit test to check internal vs external endpoints
        - create a test file UserIntegrationJsonViewTest
        - write test for external and internal endpoints

#### 12-dto-data-transfer-object

- exposing the model / entity object through a REST endpoint can be a security issue if we do not carefully protect 
which entity fields should be made available for publicly exposed REST api

- ModelMapper supports converting model / entity objects to DTO and vice versa (info: modelmapper.org)

- refactoring safe, simple fluent API for handling special use cases, and API is type safe and refactoring safe

1) add model mapper dependency in pom.xml
    - add following to pom.xml

```
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>2.3.8</version>
</dependency>
```

2) define model mapper bean in AppConfig
    - create a config package
    - create AppConfig class
        - annotate @Configuration
    - define ModelMapper bean in configuration class
        - create model mapper method and annotate @Bean
    
3) create new DTO package as UserMmDTO (Mm = model mapper)
    - create new package dto
    - create new class UserMmDto
        - add fields for example
        - generate getter and setter
    - go to UserService and add getUserByIdMm
    - go to UserServiceImpl add getUserByIdMm method 
        - will need to add @Autowire ModelMapper at the top of the class
        - add conversion of user to UserMmDto code
        - change return to UserMmDto
    - create new UserModelMapperController and copy getUserById and rename to getUserDtoById
        - write to use userService.getUserByIdMm(id)
    - write unit tests
        - create UserIntegrationDtoTest class
        - write unit test for new endpoint

4) utilize MapStruct for DTO

- is a code generator, and is generated at compile time, not runtime, and reflection is not used

- solves the issue of generating beans mapper classes.  It will generate bean mapper classes automatically

- requires a plugin to be added to pom.xml (mapstruct-processor) is used to generate the mapper implementation during build phage

    - update pom.xml (see pom.xml for details)
        - add mapstruct and maven plugin version in properties
        - add mapstruct dependency
        - add plugins for mapstruct
    
    - create UserMsDTO (Ms = map struct)
        - add fields userId, userName, and emailAddress (userName and emailAddress are different to show mapping of DTO)
        - create 2 constructors, one all args, one no args
        
    - create Mapper Interface
        - create mapper package
        - create UserMapper class
        - add @Mapper(componentModel = "Spring") at the class level
        - define INSTANCE
        - define User to UserMsDto method name (Input User Obj > Output UserMsDto)
        - define List<User> to List<UserMsDto> method name (Input List<User> Obj > Output List<UserMsDto>)
        - add @Mappings & @Mapping for source and target (since model / entity names are different from UserMsDto)
    
    - create UserMapStructController
        - add annotations 
        - add method for getAllUsersDto using userMapper.userToUserDto method
        - add unit test for testing new controller endpoint 
    
    - create getUserByIdMsDto using userMapper.userToUserDto method
        - go to UserService and add getUserByIdMs
        - go to UserServiceImpl and add getUserByIdMs method
        - go to UserMapStructController and define getUserByIdDto method
    
    - refactor UserMapStructController getAllUsersDto method to call userService
        - go to UserService and add getAllUsersMs
        - go to UserServiceImpl implement getAllUsersMs method
        - go to UserMapStructController refactor to userService.getAllUsersMs()
        
#### 13-versioning

- uri versioning (example: /users/v1/101)

1) Create 2 dto's and add a field on Model named "address"
    - go to UserModel add new field named "address"
        - generate getter & setter, new all args constructor, and tostring 
    - update data.sql
    - go to dto layer
        - create new UserDtoV1 class - contains all fields except "address"
        - create new UserDtoV2 class - contains all fields including "address"

2) implement url versioning
    - go to controller package create UserUriVersioningController
    - use ModelMapper to transfer Entity to dto
    - implement getUserByIdv1 and getUserByIdv2 methods with uri's
    - uri versions
        - @GetMapping({"/v1/{id}", "/v1.1/{id}")
        - @GetMapping("/v2/{id}")
        
#### 14-swagger

- swagger is used to document an api, and helps users understand how to implement their client apps

1) add springfox dependencies to pom.sml
    ```
   		<dependency>
   			<groupId>io.springfox</groupId>
   			<artifactId>springfox-swagger2</artifactId>
   			<version>2.9.2</version>
   		</dependency>
   		
   		<dependency>
   			<groupId>io.springfox</groupId>
   			<artifactId>springfox-swagger-ui</artifactId>
   			<version>2.9.2</version>
   		</dependency>
   ```
    
2) create SwaggerConfig file
    - annotate with @Configuration
    - annotate with @EnableSwagger2
    - create a Docket bean and annotate with @Bean
    - swagger metadata URL
        - http://localhost:8080/v2/api-docs
        - verify swagger yml online at https://editor.swagger.io/
    - swagger ui url
        - http://localhost:8080/swagger-ui.html

3) add API info to modify header of our documentation
    - create a new class "AppInfo" within our SwaggerConfig class
    - update the Docket bean with AppInfo getUserApiInfo

4) restrict scope of swagger documentation generation using API base packages and paths
    - update base package in RequestHandlerSelectors.basepackage
        - go to SpringBoot2ApiApplication class and copy the package name from the top of the class
        - go to SwaggerConfig change Docket .apis(RequestHandlerSelectors.basePackage("com.sdet.auto.springboot2api"))
        
    - update PathSelectors.ant("/users/**) to limit to specific paths
        - .paths(PathSelectors.ant("/users/**")
        
    - test doc from http://localhost:8080/v2/api-docs @ https://editor.swagger.io/
        - error found due to Options<User> in User Controller
            - go to UserController > fix error by refactoring getUserById to return User
    
5)  create swagger info for orders api
    - go to SwaggerConfig 
        - create ApiInfo for orders
        - create docket for orders
        - add .groupName("") to both user and orders docket
    - test doc from http://localhost:8080/v2/api-docs?group=Orders @ https://editor.swagger.io/
        - error found due to Options<Order> in Order Controller
            - ngo to OrderController > fix error by refactoring getOrderById to return Order
    - update path for deleteOrderById added "id" to path @DeleteMapping("id/{orderId}")  
        - fix broken integration tests   

6) auto populate documentation for JSR-303 Validations
    - JSR-303 Spec: https://beanvalidation.org/1.0/spec
    - add dependency in pom.xml and restart embedded tomcat
   
   ```
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-bean-validators</artifactId>
        <version>2.9.2</version>
    </dependency>
   ```
    - go to User model
        - add @Size(min=2, max=50) to username, firstname, & lastname
   
    - @Import(BeanValidatorPluginsConfiguration.class) on top of the swagger configuration
        - go to SwaggerConfig add @Import(BeanValidatorPluginsConfiguration.class) at the class level
        
    - verify Models in Swagger UI
        - Users Model should have min max defined for username, firstname, & lastname
   
6) add swagger Core annotation to Model class
    - class level
    - @ApiModel(description = "Used create a new user")
    - field level: notes, required, position, unorder without position
        - @ApiModelProperty(notes = "userId - Unique identifier of user", required = true)

7) add swagger Core annotation to Controller Class
    - controller
        -@Api(tag = "Use Management RESTful Services", value = "UserController", description)
    - method level
        - @ApiOperation(value = "create a new user")
    - parameter level
        - @ApiParam("User information for a new user created)
    - produces