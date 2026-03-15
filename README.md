# Kiran General Store Manager 🏪

A production-ready Android app for small Indian shopkeepers to manage Udhaar, Rentals, Buy List, Tasks and Customers.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Repository Pattern |
| DI | Hilt |
| Database | Room (offline-first) |
| Auth | Supabase Email/Password |
| Voice | Android SpeechRecognizer (Hindi hi-IN) |
| AI | Gemini 1.5 Flash REST API |

---

## Setup Instructions

### 1. Clone / Open in Android Studio
```
File → Open → Select KiranStoreManager folder
```

### 2. Add API Keys
Copy `local.properties.example` to `local.properties` and fill in:
```properties
sdk.dir=/path/to/your/Android/sdk
GEMINI_API_KEY=your_gemini_key
SUPABASE_URL=https://yourproject.supabase.co
SUPABASE_ANON_KEY=your_anon_key
```

### 3. Set Up Supabase
1. Create project at https://supabase.com
2. Enable Email Auth in Authentication → Providers
3. Copy URL and anon key into local.properties

### 4. Get Gemini API Key
1. Visit https://aistudio.google.com/app/apikey
2. Create a key and paste into local.properties

### 5. Load Seed Data (optional, for testing)
In `KiranApp.kt`, uncomment:
```kotlin
CoroutineScope(Dispatchers.IO).launch { SeedData.populateSeedData(database) }
```

### 6. Build & Run
```
Build → Make Project (Ctrl+F9)
Run → Run 'app' (Shift+F10)
```

---

## App Flow

```
Launch
  ↓
Splash Screen
  ↓
Not logged in → Login Screen
  ↓                    ↓
New user          Existing user
  ↓                    ↓
Shop Setup      Home Dashboard
  ↓                    ↓
Home Dashboard  [Udhaar | Rent | Customers]
```

---

## Voice Commands (Hindi / Hinglish)

| Command | Action |
|---|---|
| "Ramesh ko 200 ka udhaar likh do" | Add ₹200 debt for Ramesh |
| "Raju ne 100 rupaye diye" | Record ₹100 payment from Raju |
| "Buy list me 20 packet milk add karo" | Add 20 milk packets to buy list |
| "Aaj ka kaam: supplier ko call karo" | Add task: call supplier |

---

## Project Structure

```
app/src/main/java/com/kiranstore/manager/
├── KiranApp.kt                    ← Hilt Application class
├── MainActivity.kt                ← Entry + nav host
├── di/AppModule.kt                ← Dependency injection
├── utils/Utils.kt                 ← Helpers (currency, date, avatar)
├── data/
│   ├── database/
│   │   ├── entities/Entities.kt   ← 7 Room entities
│   │   ├── dao/                   ← 6 DAO interfaces
│   │   └── KiranDatabase.kt       ← Room DB + seed data
│   └── repository/Repositories.kt ← 5 repositories
├── services/
│   ├── auth/SupabaseAuthService.kt
│   ├── ai/GeminiService.kt        ← Voice → JSON action parser
│   ├── speech/SpeechRecognitionService.kt
│   └── contacts/ContactsService.kt
└── ui/
    ├── theme/                     ← Material 3 orange theme
    ├── components/SharedComponents.kt ← Reusable UI
    ├── navigation/                ← Routes + NavHost
    ├── viewmodel/                 ← 8 ViewModels
    └── screens/
        ├── auth/                  ← Login + Signup
        ├── setup/                 ← Shop Setup
        ├── home/                  ← Dashboard
        ├── udhaar/                ← Credit management
        ├── rental/                ← Machine rentals
        ├── customers/             ← Directory + contacts
        ├── buylist/               ← Purchase tracker
        ├── tasks/                 ← Daily tasks
        └── settings/              ← Profile + logout
```

---

## License
Built for Kiran Store. Free to modify and extend.
