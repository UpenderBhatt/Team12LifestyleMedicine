# Lifestyle Medicine Platform

A web application built for a lifestyle medicine clinic. Patients sign up, complete a health questionnaire and receive an AI-generated, personalised roadmap of daily activities to help them reach their health goal. Clinicians use the same site to review patients and manage consultation appointments.

> **Team project:** this was built as a university group project at Cardiff University, so it is **not solely my own work**. To protect my teammates' privacy, their names and the original commit history are not included in this repository. The "My contributions" section below describes the parts I worked on.

## Features

- **Patients:** register, complete a health questionnaire, get a personalised goal roadmap of daily tasks (generated with Google Gemini), track progress with streaks and reflections, and search for health information
- **Clinicians:** view patients and their risk profiles, download a patient report as a PDF, and manage and book consultation appointments
- **Admins:** send reminder emails to every user who has a task still pending
- Role-based access (patient, clinician, admin) with Spring Security, CSRF protection and password hashing

## My contributions

- Goal roadmap and task features: creating, displaying and deleting tasks and saving goals to the database
- Integrated the Google Gemini API to generate each user's tasks automatically from personalised prompts built from their questionnaire answers
- Changed the registration flow so users must complete the questionnaire before their account is fully set up
- Email reminder system, including an admin page to send reminders to users with pending tasks
- Security hardening: CSRF tokens, moving credentials into environment variables, IDOR protection on clinician pages, and input validation
- Automated tests for task generation, the email service and the reminder controller
- Performance testing with JMeter, EXPLAIN plans and database indexes
- Site-wide styling and navigation bar

## Tech stack

Java 21 · Spring Boot 3.5 · Spring Security · Spring Data JPA · Thymeleaf · MariaDB · Spring AI (Google Gemini) · Spring Mail (Gmail SMTP) · OpenPDF · JUnit & Mockito · JMeter

## Getting started

### Prerequisites

- JDK 21
- MariaDB (MySQL also works)
- A Google Gemini API key, free from [Google AI Studio](https://aistudio.google.com/apikey)
- A Gmail account with an [app password](https://support.google.com/accounts/answer/185833), only needed for the reminder emails

### 1. Clone the repository

```bash
git clone <this-repo-url>
cd <repo-folder>/Team12Project
```

### 2. Create the database

Log in to MariaDB as root (`mysql -u root -p`) and run the following, choosing your own password:

```sql
CREATE DATABASE LifestyleMedicine;
CREATE USER 'team12user'@'localhost' IDENTIFIED BY 'choose-a-strong-password';
GRANT ALL PRIVILEGES ON LifestyleMedicine.* TO 'team12user'@'localhost';
FLUSH PRIVILEGES;
```

The tables are created automatically when the app first starts (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Set the environment variables

No secrets are stored in this repository. The app reads them from environment variables, which are listed in [`Team12Project/.env.example`](Team12Project/.env.example):

| Variable | What it is |
|---|---|
| `DB_USERNAME` / `DB_PASSWORD` | The database user you created in step 2 |
| `AI_API_KEY` | Your Google Gemini API key |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Gmail address and app password used to send reminders |
| `SEED_CLINICIAN_PASSWORD` | Password for the demo `clinician1` account (it is only created if this is set) |
| `SEED_ADMIN_PASSWORD` | Password for the demo `admin1` account (it is only created if this is set) |

Spring Boot does not read `.env` files on its own, so set these in one of these ways:

- **IntelliJ:** Run → Edit Configurations → *Environment variables*
- **macOS / Linux:** `export DB_USERNAME=team12user` (and the same for each variable) before running
- **Windows PowerShell:** `$env:DB_USERNAME="team12user"` (and the same for each variable) before running

### 4. Run the app

```bash
./gradlew bootRun        # on Windows: gradlew.bat bootRun
```

Then open <http://localhost:8080>. Patients register at `/register`. Clinicians and admins log in with the demo accounts above.

## Running the tests

```bash
./gradlew test
```

## Performance testing (optional)

The `loadtests/` folder contains the JMeter test plan, EXPLAIN plan queries and results used to measure the effect of the database indexes on `task`, `Answer` and `UserEntity`.

1. To load bulk test data, set `app.seed.testdata=true` in `src/main/resources/application.properties` and start the app once, then set it back to `false`. The seeded test patients share a simple password, so only do this on a local database.
2. Open `loadtests/cw2-loadtest.jmx` in JMeter, passing the clinician demo password as a property, for example `jmeter -JclinicianPassword=<your SEED_CLINICIAN_PASSWORD> -t loadtests/cw2-loadtest.jmx`
3. Each thread group (Roadmap, Risk Profile, Clinician Users) can be run at 5, 25 or 100 users. Results appear in the Aggregate Report.
4. The EXPLAIN queries are in `loadtests/explain_queries.sql`.
