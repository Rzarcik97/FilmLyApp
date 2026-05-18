# 🎬 FilmLy – Movie & Series Frontend Application

FilmLy is a modern, responsive web application for browsing movies and series, managing personal watchlists, and viewing tailored recommendations. Built to seamlessly consume the FilmLy Spring Boot REST API, it delivers a smooth user experience with rich animations, real-time caching, and a secure JWT-driven authentication flow.

## 📚 Table of Contents

* [Technologies](#-technologies)
* [Architecture & State Management](#-architecture--state-management)
* [Key Features & Pages](#-key-features--pages)
* [How to Run](#-how-to-run)
* [Testing](#-testing)
* [CI/CD & Deployment](#-cicd--deployment)

---

## 🛠 Technologies

| Technology | Description |
| :--- | :--- |
| **React 19** | Core UI library utilizing the latest concurrent features |
| **TypeScript 6.x** | Advanced static type safety across components and API models |
| **Tailwind CSS** | Styling framework and responsive layouts |
| **Axios** | HTTP client for consuming the backend REST API |
| **Redux Toolkit & React Redux** | Global state management for authentication and cached application states |
| **React Hook Form** | Performant, flexible form handling with minimal re-renders |
| **Zod** | Schema-based validation tool coupled with React Hook Form resolvers |
| **Framer Motion** | Production-ready animation library for fluid UI transitions |
| **Lucide React** | Sleek, consistent, and customizable icon library |
| **npm Workspaces** | Monorepo structure isolating the frontend workspace directory |

---

## 🏗️ Architecture & State Management

The frontend follows a feature-based folder structure, keeping components, hooks, and API logic isolated by domain.

```text
                    ┌──────────────────────────────┐
                    │          User Device         │
                    │      (Browser Viewport)      │
                    └──────────────┬───────────────┘
                                   │ User Interacts
                                   ▼
                    ┌──────────────────────────────┐
                    │       UI / Page Layer        │
                    │   Components & Routing       │
                    └──────────────┬───────────────┘
                                   │ Hooks / State Triggers
                                   ▼
             ┌─────────────────────────────────────────────┐
             │            State Management Layer           │
             │  Local State (useState / Custom Hooks)      │
             │  Global State (Redux Toolkit: Auth context) │
             └───────────────────────┬─────────────────────┘
                                     │
                                     ▼
                   ┌─────────────────────────────────┐
                   │            API Client           │
                   │    Axios Interceptors (JWT)     │
                   └──────────────┬──────────────────┘
                                  │ HTTP Requests (with Bearer Token)
                                  ▼
                   ┌─────────────────────────────────┐
                   │        FilmLy Spring Boot       │
                   │            Backend API          │
                   └─────────────────────────────────┘

```markdown
## 📘 Key Features & Pages

### 🌐 Public Access (Anonymous Users)
* **Landing / Home Dashboard:** Highlights trending, popular, and upcoming movies/series via interactive sliders.
* **Advanced Search & Discover:** Multi-attribute filtering system (by title, genre, or rating).
* **Detail Pages:** Rich presentation of movie/series meta-data, cast profiles, and "Similar Content" lists.

### 🔐 Protected Access (Requires Login)
* **Personalized Dashboard:** Displays tailored recommendations computed by the backend algorithm.
* **Interactive Watchlist:** Add/remove items, and toggle a visual "Watched" status.
* **Likes System:** Immediate feedback loops for liking and disliking content cards.
* **Secure Profile Settings:** Dedicated workspace for email and password updates featuring a verification status tracker.

---

## 🚀 How to Run

### ✅ Prerequisites
* Node.js (v18.x or higher)
* npm / yarn

### 🔐 Environment Variables
Create a `.env` file in the project root:

```bash
cp .env.example .env

Fill in your configuration variables:
# API URL of your FilmLy Backend
VITE_API_BASE_URL=http://localhost:8081/api/v1

▶️ Run Locally
# Install dependencies
npm install

# Run the development server
npm run dev

The application will start locally at http://localhost:5173.

```markdown
## 🧪 Testing

We ensure robustness by testing the UI at multiple levels:

* **Unit & Component Testing (Vitest + React Testing Library):** Verifies standalone reusable UI elements (buttons, input fields, cards) render correctly based on varying props.
* **Integration Testing:** Mocks the backend REST API responses to test how pages handle loading, error, and data states.

Run the test runner:
```bash
npm run test


