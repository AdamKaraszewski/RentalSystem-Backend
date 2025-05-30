BEFORE YOU RUN compose.yml make sure that:
1. docker service is running (type "docker ps" in terminal),
2. the .env file is located in the same directory like compose.yml,
3. the database-role-init.sql is located in the same directory like compose.yml
4. the database-init.sql is located in the same directory like compose.yml
5. the .env is located in the same directory like compose.yml
6. your current working directory is the one where .env, compose.yml, *.sql are located (type pwd to check).

|_ working_directory (e.g. database)
   |_.env
   |_compose.yml
   |_database-roles-init.sql
   |_database-init.sql

database-roles-init.sql example:

*--------------------------------------------------------------------*
| CREATE ROLE admin WITH LOGIN PASSWORD "password";                  |
| CREATE ROLE authenticationmodule WITH LOGIN PASSWORD "password";   |
| CREATE ROLE accountmodule WITH LOGIN PASSWORD "password";          |
| CREATE ROLE busunesslogicmodule WITH LOGIN PASSWORD "password";            |
*--------------------------------------------------------------------*

database-init.sql example:

*--------------------------------------------------------------------*
| CREATE DATABASE my_db OWNER admin;                                 |
*--------------------------------------------------------------------*

.env example:

*-------------------------------------------------------------------*
| DB_PORT=5432                                                      |
| POSTGRES_USER=postgres                                            |
| POSTGRES_PASSWORD=postgres                                        |
| POSTGRES_DB=postgres                                              |
| ADMINER_PORT=8081                                                 |                                      |
*-------------------------------------------------------------------*

Type "docker compose up" to initialize container with RDBMS PostgreSQL.

After rental_system_db container init, a new directory will be created in your current working directory:

|_ working_directory (e.g. docker)
   |_.env
   |_compose.yml
   |_database-roles-init.sql
   |_database-init.sql
   |_database_data
     |_pg_hba.conf

To connect to PostgreSQL, first check if rental_system_db container is running (docker ps) - if
container is not running start it by typing "docker start rental_system_db".

If rental_system_db is running, type "docker exec -it rental_system_db bash" - this will connect you to the container.