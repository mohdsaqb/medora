# Medora

[![CI](https://github.com/mohdsaqb/medora/actions/workflows/ci.yml/badge.svg)](https://github.com/mohdsaqb/medora/actions/workflows/ci.yml)

A hospital records system. Staff register patients, record diagnoses against
them, and issue prescriptions against each diagnosis.

Spring Boot REST API, React frontend, PostgreSQL.

## Contents

- [What it does](#what-it-does)
- [Stack](#stack)
- [Roles](#roles)
- [Running locally](#running-locally)
- [Configuration](#configuration)
- [API](#api)
- [Tests](#tests)
- [Deploying](#deploying)
- [Known limitations](#known-limitations)
- [Licence](#licence)

## What it does

The data model is a chain. A **patient** has many **problems** (diagnoses), and
each problem has many **prescriptions**. Separately, **staff** records hold
clinicians and their departments, and **admissions** link a patient to a staff
member.

Reading is open to anyone. Creating, editing and deleting require a sign in,
and what you may do depends on your role.

## Stack

| Layer | Technology |
|---|---|
| Language | Java 23 |
| Framework | Spring Boot 4.1.0 |
| Persistence | Spring Data JPA, Hibernate |
| Security | Spring Security 7, HTTP Basic, BCrypt |
| Database | PostgreSQL 17 |
| Mapping | ModelMapper |
| Boilerplate | Lombok |
| Build | Maven (wrapper included) |
| Frontend | React 16, Create React App |
| Routing | React Router 5 |
| HTTP | Axios |
| Forms | Formik |
| Styling | Bootstrap 4 (CDN) |
| Notifications | AlertifyJS |
| Tests | JUnit 5, MockMvc, Spring Security Test, H2 |

## Roles

Three staff roles, checked at the endpoint. One account per role is created on
first boot, with usernames `admin`, `doctor` and `receptionist` and the
passwords you supply through environment variables.

| | Patients | Diagnoses and prescriptions | Staff | Delete |
|---|---|---|---|---|
| Anonymous | read | read | read | no |
| `RECEPTIONIST` | read, create, edit | read | read | no |
| `DOCTOR` | read, create, edit | read, create, edit | read | no |
| `ADMIN` | everything | everything | everything | yes |

## Running locally

Requires JDK 23, Node 20 and either Docker or a local PostgreSQL.

### 1. Configure

```bash
cp .env.example .env
cp frontend/.env.example frontend/.env
```

Set `ADMIN_PASSWORD`, `DOCTOR_PASSWORD` and `RECEPTIONIST_PASSWORD` in `.env`.
Accounts are only created when these are set.

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

Starts on <http://localhost:8185>. With Docker running, Spring Boot starts the
PostgreSQL container from `compose.yml` automatically. To use an existing
PostgreSQL instead, set `DOCKER_COMPOSE_ENABLED=false` and point `DB_URL` at it.

Hibernate creates the schema on first run. For demo data, 12 patients with
Indian names and cities, and deliberately fake contact numbers:

```bash
psql "postgresql://medora:medora@localhost:5432/medora" -f backend/seed.sql
```

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

Starts on <http://localhost:3000>. Sign in from the navbar.

## Configuration

All settings are environment variables with local defaults, so the app runs
without configuration and takes real values in deployment. See `.env.example`.

| Variable | Default | Purpose |
|---|---|---|
| `PORT` | `8185` | Backend HTTP port |
| `DB_URL` | local Postgres | JDBC connection string |
| `DB_USERNAME` | `medora` | Database user |
| `DB_PASSWORD` | `medora` | Database password |
| `ALLOWED_ORIGIN` | `http://localhost:3000` | Origins allowed to call the API |
| `ADMIN_PASSWORD` | empty | Seeds the admin account |
| `DOCTOR_PASSWORD` | empty | Seeds the doctor account |
| `RECEPTIONIST_PASSWORD` | empty | Seeds the receptionist account |
| `DDL_AUTO` | `update` | Hibernate schema mode |
| `SHOW_SQL` | `false` | Log every SQL statement |
| `DOCKER_COMPOSE_ENABLED` | `true` | Start the database container on boot |
| `REACT_APP_API_URL` | `http://localhost:8185/api` | Backend URL, read at build time |

Seeding runs only when the users table is empty, so restarts never overwrite a
changed password.

## API

Base path `/api`. Every `GET` is public. The role column shows the minimum role
required.

### Patients

| Method | Path | Role |
|---|---|---|
| `GET` | `/patient` | anonymous |
| `GET` | `/patient/find-by-id/{patientid}` | anonymous |
| `GET` | `/patient/find-by-email/{email}` | anonymous |
| `GET` | `/patient/find-by-name/{name}` | anonymous |
| `GET` | `/patient/deleted-patient` | anonymous |
| `GET` | `/patient/cities` | anonymous |
| `POST` | `/patient` | `RECEPTIONIST` |
| `PUT` | `/patient/{patientid}` | `RECEPTIONIST` |
| `DELETE` | `/patient/{patientid}` | `ADMIN` |

### Problems

| Method | Path | Role |
|---|---|---|
| `GET` | `/problem/find-by-problemid/{problemid}` | anonymous |
| `GET` | `/problem/find-all-by-patientid/{patientid}` | anonymous |
| `GET` | `/problem/status` | anonymous |
| `POST` | `/problem` | `DOCTOR` |
| `PUT` | `/problem/{problemid}` | `DOCTOR` |
| `DELETE` | `/problem/{problemid}` | `ADMIN` |

### Prescriptions

| Method | Path | Role |
|---|---|---|
| `GET` | `/prescription/find-all-by-problemid/{problemid}` | anonymous |
| `POST` | `/prescription` | `DOCTOR` |
| `PUT` | `/prescription/{prescriptionid}` | `DOCTOR` |
| `DELETE` | `/prescription/{prescriptionid}` | `ADMIN` |

### Staff and account

| Method | Path | Role |
|---|---|---|
| `GET` | `/staff` | anonymous |
| `GET` | `/staff/deleted-staff` | anonymous |
| `GET` | `/staff/cities` | anonymous |
| `GET` | `/staff/department` | anonymous |
| `DELETE` | `/staff/{staffid}` | `ADMIN` |
| `GET` | `/user/me` | any signed in account |

`GET /user/me` returns the signed in username and role. It is how the frontend
checks credentials, since HTTP Basic has no login endpoint of its own.

### Request rules

Creating a patient requires **name**, **last name** and **phone number**.
Updating does not: `PUT` merges whatever it receives onto the stored record, so
a partial payload changes only the fields it contains and leaves the rest alone.

Blank optional fields are stored as null rather than an empty string. `email`
carries a unique constraint, so without that any second patient saved with an
empty email would collide with the first.

### Error responses

Errors share one shape, `{"date": ..., "message": ...}`, so a client can always
show something useful.

| Status | Meaning |
|---|---|
| `400` | Required fields missing. The message names them. |
| `401` | Not signed in, on an endpoint that needs an account |
| `403` | Signed in, but the role is not allowed |
| `404` | No matching record. Collection endpoints return `[]` instead. |
| `409` | Conflicts with an existing record, such as a duplicate email |

Stack traces are never returned.

## Tests

```bash
cd backend && ./mvnw verify      # 17 tests, JUnit 5 against in-memory H2
cd frontend && CI=true npm test  # Jest
```

The backend suite needs no database and no Docker. It covers:

- The patient endpoints, including that anonymous callers can read but not write
- Every role rule, including that a doctor gets `403` rather than `401` on a
  delete, which is what distinguishes a wrong role from a missing login
- That seeded passwords are stored BCrypt hashed, never in plain text
- That a create without a phone number is rejected, and that an update with a
  partial payload keeps the fields it left out

CI runs both suites on every push and pull request.

## Deploying

The backend ships a multi-stage `backend/Dockerfile` that builds with a JDK and
runs on a JRE as a non root user. It reads `PORT` at runtime, so it works on any
platform that assigns one.

1. Create a PostgreSQL database and copy its connection details.
2. Deploy `backend/` as a Docker service. Set `DB_URL`, `DB_USERNAME`,
   `DB_PASSWORD`, the three seed passwords, `ALLOWED_ORIGIN`, and
   `DOCKER_COMPOSE_ENABLED=false`.
3. Deploy `frontend/` as a static site. Build `npm run build`, publish `build/`,
   and set `REACT_APP_API_URL` to the backend URL including `/api`.
4. Set `ALLOWED_ORIGIN` to the frontend domain and redeploy the backend.

`REACT_APP_API_URL` is baked in at build time, so changing it needs a rebuild.

## Known limitations

Honest notes rather than a roadmap.

- **Frontend is dated.** React 16 and Create React App are both end of life.
  Migrating to Vite and React 19 is the largest outstanding piece of work, and
  it has to happen in one coordinated change: React, both routers, the date
  picker and the testing library all need major bumps together.
- **Reads are public.** Any visitor can list patients and their diagnoses. That
  keeps a deployed demo shareable without credentials, but it is not a setting
  to carry into anything holding real records.
- **Basic auth, not tokens.** Credentials are sent on every request and held in
  `sessionStorage`. Workable over HTTPS at this scope, but JWT is the better fit
  for a real deployment. The role rules would carry over unchanged.
- **No patient logins.** All three roles are staff. Patient accounts need
  per-row ownership checks rather than endpoint rules, which is a larger change.
- **List endpoints are unbounded.** No pagination yet.
- **`ProblemService.update` ignores the soft delete flag and creation date** by
  design, but neither is settable through any other endpoint either.
- **Six overlapping DTOs.** Named after call sites rather than data, and worth
  consolidating.
- **Frontend test coverage is one smoke test.** The backend is well covered; the
  React components are not.

## Licence

MIT. See [LICENSE](LICENSE).
