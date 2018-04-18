## API
Here lists the APIs provided for frontend.

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