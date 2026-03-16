# Kirana Store Manager

Kirana Store Manager is an Android application designed to help small shopkeepers manage their daily business operations easily.  
The app focuses on **simplicity, offline support, and quick data entry** for managing customer credit (Udhar), rentals, and shop tasks.

This project is being developed step-by-step using an **AI-assisted development workflow** and modern Android architecture.

---

## ✨ Features

### Customer Management
- Add and manage customers
- Store phone numbers and notes
- Search customers quickly
- View complete customer history

### Udhar (Credit) Tracking
- Add Udhar entries for customers
- Record payments
- Support **partial payments**
- Automatically calculate remaining balance

### Rental System
- Manage rental machines (e.g., drill machines, ladders)
- Track active and returned rentals
- Record deposits and rent charges
- Maintain rental history per customer

### Tasks
- Maintain a simple shop task list
- Mark tasks as completed
- Track pending tasks

### Buy List
- Track items the shop needs to purchase
- Mark items as purchased
- Manage stock reminders

### Voice Assistant (Planned)
- Mic button to add entries using voice
- Natural language commands like:
  - “Ramesh ko 200 ka udhar likh do”
  - “Milk buy list me add karo”

### Cloud Sync (Planned)
- Supabase authentication
- Multi-shop login support
- Cloud database sync
- Image upload using Supabase Storage

---

## 🏗 Architecture

The project follows a **clean Android architecture**:

- **MVVM (Model-View-ViewModel)**
- **Repository Pattern**
- **Jetpack Compose UI**
- **Room Database**
- **Hilt Dependency Injection**


---

## 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Material Design 3
- Room Database
- Hilt Dependency Injection
- Android SpeechRecognizer (planned)
- Gemini API (planned)
- Supabase Auth & Database (planned)
- Supabase Storage (planned)

---

## 📱 Screens

The app includes the following screens:

- Home Dashboard
- Customers
- Customer Details
- Add Customer
- Udhar Entry
- Payment Entry
- Rentals
- Add Rental
- Tasks
- Buy List
- Settings

---

## 🚀 Development Phases

### Phase 1
- Customer management
- Udhar tracking
- Payments
- Rental system
- Basic UI

### Phase 2
- Settings
- Task management
- Buy list

### Phase 3
- Voice commands
- AI assistant (Gemini)

### Phase 4
- Supabase authentication
- Multi-shop accounts
- Cloud sync

### Phase 5
- Image uploads
- WebP compression
- Storage optimization

---

## ⚙️ Setup

### Requirements
- Android Studio (latest version)
- JDK 17+
- Gradle (bundled with Android Studio)

### Run the Project

Clone the repository:

```bash
git clone https://github.com/harsh-gupta-10/Kirana-Store-Manager.git


