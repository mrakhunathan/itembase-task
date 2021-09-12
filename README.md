# hatchways-assessment

## Reference Documentation
* Clone the project from Git (master branch), Make sure to have Java 11 and Maven on your path/classpath
* You can run the application with mvn clean install && mvn spring-boot:run
* Swagger should be Loaded on http://localhost:8080/currency-convertor/swagger-ui/index.html
* Here is a sample CURL 

###### 

```bash
curl -X 'POST' \
  'http://localhost:8080/currency-convertor/convert' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "from": "EUR",
  "to": "USD",
  "amount": 5.67
}'
```

```bash
curl -X 'POST' \
  'http://localhost:8080/currency-convertor/convert' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "from": "USD",
  "to": "INR",
  "amount": 10
}'
```


Please find response screenshot added to root of this project as well.  
