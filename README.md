Rest-Book: A Comprehensive Quarkus-Powered Library Management API 🚀

Overview

Rest-Book is a cutting-edge Library Management System built on Quarkus, the Supersonic Subatomic Java Framework. Designed for performance, scalability, and efficiency, this API delivers seamless book management, borrowing & return tracking, analytics, and AI-powered book inquiries using OpenAI integration.

Key Features ✨

📚 Book Management

Fetch all book details

Count available books

Retrieve overdue books

📝 Review System

Users can submit reviews for books

Retrieve reviews for specific books

🔄 Borrow & Return Books

Track borrowed books

Record book returns

Prevent duplicate borrow requests

📊 Analytics Dashboard

Gain insights into the most borrowed books

Identify most active users

Track book return statistics

🚚 Home Delivery System

Request home delivery of books (availability-based)

🤖 AI-Powered Book Summaries

Integrated with OpenAI API

Users can ask questions and receive AI-generated book summaries

API Endpoints

Books

GET /api/books → Fetch all books

GET /api/books/count → Get total count of books

GET /api/books/{id} → Retrieve book by ID

POST /api/books/add → Add a new book

Borrow & Return

POST /api/books/{id}/borrow/{username} → Borrow a book

POST /api/books/{id}/return → Return a borrowed book

Reviews

POST /api/books/{id}/review → Add a book review

GET /api/books/{id}/reviews → Get book reviews

Analytics

GET /api/books/most-borrowed → Top N most borrowed books

GET /api/books/most-active-users → Top N most active users

GET /api/books/return-count → Return count per book

GET /api/books/user-returns → Return count per user

GET /api/books/last-return-time → Last return timestamps

AI & OpenAI Integration

POST /api/books/{id}/ask → Ask AI about a book

POST /api/books/search → AI-powered book search

Home Delivery

POST /api/books/{id}/deliver → Request home delivery

Library Management

GET /api/books/library → Fetch all books from the library

Built With 🛠️

🚀 Quarkus – Ultra-fast Java framework for cloud-native applications.

🌐 RESTEasy JAX-RS – Simplifies RESTful API development.

✅ Jakarta Validation & Dependency Injection – Ensures robust validation and modular architecture.

🧠 OpenAI API – Enhancing user experience with AI-powered book insights.

Installation & Running

Clone the repository:

git clone https://github.com/YashAhlawat/Rest-Book.git
cd Rest-Book

Build the project:

mvn clean package

Run the application:

mvn quarkus:dev

👨‍💻 Developed By

Yash Ahlawat – Bringing innovation to digital library management! 🚀📖









