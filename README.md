# AergoForms
toy project

http://localhost:9000/index   

User Name : admin  
Password : admin  

## Database

http://localhost:9000/h2-console   

saved Settings : Generic H2 (Server)  
setting Name: Generic H2 (Server)  
Driver class : org.h2.Driver  
JDBC URL: jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL  
User Name : blocko  
Password : blocko  

## Swagger

http://localhost:9000/swagger-ui.html  
http://localhost:9000//v2/api-docs  

## Run
1.  Run Configurations에 Spring Boot App 추가.  
2.  VM arguments 항목에 아래 내용 추가.  
    -Dspring.profiles.active="development"  
