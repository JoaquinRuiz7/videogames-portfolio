# GameScope 🎮

**GameScope** 
is a web application that allows users to search for videogames and discover the best available purchase deals for  PC across different platforms.
---

## Learning Objectives

This project was created primarily as a **hands-on learning exercise for Kubernetes**.

The main objective was to understand how modern containerized applications are deployed and orchestrated in a distributed environment.

Through this project I explored:

- Docker containerization
- Kubernetes deployments and scaling
- service-to-service communication
- ingress configuration and routing
- secrets management
- container networking

The application serves as a practical environment to experiment with **real-world Kubernetes concepts**.

## Architecture

The backend is built with Spring Boot following **Clean Architecture and SOLID principles**, 
ensuring that business logic remains independent from framework-specific concerns.

GameScope integrates with two external APIs:

- **RAWG Video Games Database API** — provides videogame metadata.
- **CheapShark API** — provides videogame price deals.

A centralized backend service gathers and processes the data from both APIs, 
returning simplified responses optimized for frontend consumption.

---

## Tech Stack

#### Backend
- Java
- Spring Boot
#### Frontend
- React
- TypeScript
#### Infrastructure
- Docker
- Docker Compose
- Kubernetes
- NGINX
---

## Containerized Setup

Both **frontend** and **backend** are containerized using Docker.

A **Docker Compose** configuration is included to run the application locally.

- Containers communicate internally through the created appnet network.
- The backend container is not exposed to the host.
- Only the frontend container publishes a port to the host.

---

## Kubernetes Deployment

The project also includes a full **Kubernetes configuration**.

It includes:

- Deployments
- Services
- Ingress

### Deployment

- **2 replicas for the frontend**
- **2 replicas for the backend**

### Services

Services expose a stable network endpoint for a group of pods and distribute incoming traffic among them.

### Ingress

The frontend is exposed through an **NGINX Ingress controller**.

### Secrets

API keys are managed using **Kubernetes Secrets**.

Secrets are **not included in the repository** for security reasons.

---

## RAWG API Key Setup

To run the backend you need a **RAWG API key**.

Get your API key here:

https://rawg.io/apidocs

Then create a `.env` file inside videogames-backend project and add the following:

**RAWG_API_KEY=your_api_key_here**


---

## Running the Application Locally

Make sure you have **Docker** and **Docker Compose** installed.

Then run:

```bash
docker compose build
docker compose up -d
```

Then open http://localhost:3000 and you are all done!

Feel free to explore the application and the infrastructure setup behind it.