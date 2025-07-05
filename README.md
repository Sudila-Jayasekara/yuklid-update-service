To run
1. Create application-ci.yml in same place with all details
2. make sure you add application-ci.yml to .gitignore

mvn spring-boot:run -Dspring-boot.run.profiles=ci

To build locally 
mvn clean install -Drevision=v1.2.3 -Dspring.profiles.active=ci


**Since use -DskipTests in github build, buid won't fail even it has error so make sure test correctly in local environment**