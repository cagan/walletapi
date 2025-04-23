docker-compose kill

mvn clean package -DskipTests

mvn compile jib:dockerBuild

docker-compose up -d