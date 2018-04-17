# Smart Recruiting Assistant

## Steps to launch the project
### Preliminary
- MySQL 5.6+
- Java 1.8+
- Maven 3.0+

### Package
```
cd recruiting-assistant/
mvn package -DskipTests
```

### Database
Execute the following SQLs to create the database and user with proper authorities.
```sql
create database recruiting_assistant
create user 'recruiting_assistant'@'localhost' identified by 'recruiting_assistant';
grant all on recruiting_assistant.* to 'recruiting_assistant'@'localhost';
```

### Execution
The package has embedded Tomcat. You can launch the server by the following command.
```
cd target/
java -jar recruiting-assistant-1.0-SNAPSHOT.jar
```
You should not close this shell.

### Initialization (just for Dev phase)
Access the following URL to generate default accounts.
```
http://localhost:8888/init
```
You can change the server port in `application.properties`.
```properties
server.port=8888
```

## Dashboard and Default Accounts
After finished the above steps, you can access `http://localhost:8888/admin` to sign in the Dashboard.

You should use the following default accounts for the first time.
- Super Administrator: 
  - username: `admin`
  - password: `admin`
- Company Manager:
  - username: `sap`
  - password: `sap`

## Users and Authorities
Super Administrators can create, list, update and delete companies, users and tags.
But Company Managers can only modify the information of their own company.

One company allows to have multiple managers, but one manager can only belong to one company.

## API
### Company List
- Endpoint
  ```
  /api/companies
  ```
- Request
  ```json
  {}
  ```
- Response
  ```json
  {"companies": ["SAP", "..."]}
  ```
### Select Company
- Endpoint
  ```
  /api/select
  ```
- Request
  ```json
  {"wxId": "xxx", "company": "SAP"}
  ```
- Response
  ```json
  {"success": "true/false"}
  ```
### Ask Question
- Endpoint
  ```
  /api/ask
  ```
- Request
  ```json
  {"wxId": "xxx", "question": "SAP的地址是什么？"}
  ```
- Response
  ```json
  {"success": "true", "answer": "上海市浦东新区晨晖路1001号"}
  ```
  ```json
  {"success": "false", "answer": "400/401"}
  ```
### Apply Interview
- Endpoint
  ```
  /api/apply
  ```
- Request
  ```json
  {"wxId": "xxx", "name": "张三", "phone": "1xxxxxxxxxx"}
  ```
- Response
  ```json
  {"success": "true/false"}
  ```
### Online Interview
- Endpoint
  ```
  /api/interview
  ```
- Request
  - For the first problem:
    ```json
    {"wxId": "xxx", "problemId": "-1", "answer": ""}
    ```
  - Afterwards:
    ```json
    {"wxId": "xxx", "problemId": "1", "answer": "A"}
    ```
- Response
  - Not the last problem:
    ```json
    {"success": "true", "problemId": "1", "problem": "xxx"}
    ```
  - The last problem:
    ```json
    {"success": "true", "problemId": "-1", "problem": "您已完成在线测试，请耐心等待后续通知，谢谢！"}
    ```