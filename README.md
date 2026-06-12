# myeasybudget

Spring Boot backend prepared for Docker deployment with PostgreSQL.

Production domain: https://my-easy-budget.com

## Run with Docker

The Compose stack reads test database settings from `.env`.

```powershell
docker compose up --build
```

Backend:

- Application: http://localhost:8080
- Health: http://localhost:8080/actuator/health

PostgreSQL defaults:

- Database: `myeasybudget`
- User: `myeasybudget_user`
- Password: `myeasybudget_password`
- Host port: `5432`

The frontend is not included in the Compose stack yet because its technology has not been selected.
