# Email Service

## Table Of Contents
1. Overview
2. API Documentation
3. Deployment
4. Demo
5. Limitations
6. Test Coverage
7. Todo
   

### 1. Overview
The objective of this service is to expose an API to send out emails. The API supports multiple recipients, cc and bcc email IDs. In the backend, the email service utilizes SendGrid and MailGun email services to fullfil the request. The service is designed such a way that if any of the aformentioned services failed, automatic failover to the available one will be performed.

### 2. API Documentation

##### Send Email API
Sends email to given list of `to`, `cc` and `bcc` email IDs from the `from` email ID. Email `subject` and `body` are optional parameters. Since email specification allows messages to be sent without the said fields.

- URL <br>
  `/emails`

- Method <br>
  `POST`

- URL Parameters <br>
  `None`

- Request Body
    ```
    {
        "from": STRING [REQUIRED],
        "to": ARRAY OF STRINGS [REQUIRED],
        "subject": STRING [OPTIONAL],
        "body": STRING [OPTIONAL],
        "ccs": ARRAY OF STRINGS [OPTIONAL],
        "bcs": ARRAY OF STRINGS [OPTIONAL]
    }
    ```

- Response
    - Success Response <br>
      This resposne means the request is valid, accepted and currently queued. The request will be process asynchronously. Currently there is no API available to check the status of an accepted request.
      `Status code: 202 ACCEPTED`
      Response
      ```
      {
          "status": STRING,
          "requestId": STRING
      }
      ```

    - Error Response  
      `Status code: 400 BAD REQUEST`
      Response
      ```
      {
          "statusCode": INTEGER,
          "errorMessage": STRING
      }
      ```
      Reasons:
      - If the request does not match the request body schema
      - If the request contains invalid email IDs

      `Status code: 500 INTERNAL SERVER ERROR`
      `Response`
      ```
      {
          "statusCode": INTEGER,
          "errorMessage": STRING
      }
      ```
      Reasons:
      - Email service is unavailable to due to internal server error (Both SendGrid and MailGun are down)

### 3. Deployment

##### 3.1 Prerequisites
- Git
- Java 8
- Ensure `JAVA_HOME` is set

##### 3.2 Instructions
1. Checkout repository
   `git checkout <REPOSITORY_URL> .`

2. Navigate to the project location and build the project
   `cd PROJECT_ROOT_DIR`
   `./gradlew bootJar`

3. Set the following environment variables required for the application to run.
    - `MAIL_GUN_API_KEY`
    - `MAIL_GUN_DOMAIN`
    - `SENDGRID_API_KEY`

4. Execute the jar
   Assuming you are in the project root directory
   `java -jar build/libs/emailservice-0.0.1-SNAPSHOT.jar`

### 4. Demo
The application is currently deployed in an AWS BeanStalk environment. The application can be accessed via `emailservice-env.xb3bpqkjp8.ap-southeast-2.elasticbeanstalk.com` Please refer to the API Documentation section for more information. 

- The repository also contains a Postman Collection when can be readily used via Postman to test the API. The collection and environment file can be found in `PROJECT_ROOT_DIR/postman` Use `domain-aws` as value in the Postman URL placeholder in order to initiate requests to the AWS deployed application.

- Alternatively, perform a POST request on http://emailservice-env.xb3bpqkjp8.ap-southeast-2.elasticbeanstalk.com/emails with a valid request body.

### 5. Limitations
- The number of max number of recipients per request are constrained by the email provider services SendGrid and MailGun used in the application.
- Default email provider service used is SendGrid. If the said service fails, MailGun will be used to fulfil the request. **MailGun requires a custom verified domain to send out emails to any unauthorized email IDs. This application currently does not have a custom verified domain setup for MailGun. Thus, when only using MailGun for fulfilment, currently, emails to unauthorized email will not be fulfilled.**
- The application does not have UI in the frontend. Any REST API clients like Postman can used directly consume the exposed API. Please refer to the API documentation section for more information. 

### 6. Test Coverage
The application does not have full test coverage currently. The following lists the features with test converage.
- Sanitation and validation of input requests. 
- Failover to secondary email provider upon failure of the primary service

### 7. Todo
- Front end UI
- Tests for REST controllers
    - Validation of dat types and mapping
    - Request responses
