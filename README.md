# THE LIGHT (“THE LIGHT OF AKWA IBOM”)
> ### *“BRIGHTER WE SHINE!”*

An offline mobile-friendly Android application built with **Kotlin** and **Jetpack Compose**, celebrating Akwa Ibom culture, community innovation, and global entertainment.

---

## 🌟 Key Features

1. **Brand Identity & Akwa Ibom Pride**:
   - Official adaptive launcher icon and top header with the custom **THE LIGHT OF AKWA IBOM** brand insignia.
   - Distinctive Akwa Ibom palette: Emerald Green (`#006C35`), Sunshine Gold (`#FFB300`), and Sunrise Orange (`#E65100`).
   - Slogan: *“BRIGHTER WE SHINE!”*

2. **TikTok-Style Vertical Video Feed**:
   - Swipe-up vertical pager for short videos and movies.
   - Multi-regional content: African (Nollywood/Afrobeats/Akwa Ibom), Chinese (Wushu martial arts & Sichuan opera), and Philippine (acoustic Harana & Cebu street food) entertainment.
   - Video actions: Heart like with counter, comment drawer, follow toggles, rotating vinyl disc, mute toggle, and video upload.

3. **Community & Real-Time Messaging**:
   - Community social feed with LGA tags (Uyo, Ikot Ekpene, Eket, Oron) and category filters (*Culture*, *Tech*, *News*, *Lifestyle*).
   - WhatsApp-inspired 1-on-1 private chats and community group discussions.
   - Audio voice note playback simulation with waveforms and duration indicators.
   - In-app notification center for scores, matches, and state bulletins.

4. **Light Dating (Strictly 18+)**:
   - Age-restricted dating discovery cards with verified profiles.
   - Interactive Pass, Super Like, and Match buttons.
   - Mutual match celebration modal with instant private dating chat transition.
   - Safety, blocking, and reporting controls.

5. **Ibibio Language Zone**:
   - Dedicated cultural lounge (*“Kèm usem Ibibio kpọt mi!”*).
   - Real-time Ibibio chat room with quick-response phrases (*Amesiere*, *Sosongo*, *Idem mfo*, *Abasi ọdiọñ fi*).
   - Searchable phrasebook & dictionary with phonetic guides and audio pronunciation simulation.
   - Traditional proverbs (*Nke*) with cultural meanings.
   - Audio calling practice room.

6. **Sports & Entertainment**:
   - Live European football fixtures, live scores ticker, and upcoming schedules for the **English Premier League (EPL)** and **UEFA Champions League (UCL)**.
   - Real-time league standings tables (Arsenal, Man City, Liverpool, Real Madrid, Bayern, etc.).
   - Entertainment news on Nollywood, Akwa Ibom creatives, and concerts at Godswill Akpabio International Stadium.

7. **Akwa Ibom Opportunities**:
   - Directory for **Jobs**, **Scholarships**, **SME Business Grants**, **Skills Training**, and **Tourism & Heritage** (Ibeno Beach, Ikot Ekpene Raffia crafts, Ibom Icon Resort).
   - In-app opportunity submission form for recruiters and community leaders.

8. **Admin Dashboard & Security**:
   - Overview metrics: registered members, active videos, pending moderation reports.
   - Content moderation queue: review flagged dating profiles, posts, and messages with dismiss or ban actions.
   - State bulletin broadcast tool.

9. **Offline-First Persistence**:
   - Powered by **Android Room SQLite Database** (`AppDatabase`, `AppDao`).
   - All posts, comments, likes, chat messages, dating matches, and custom videos persist reliably on-device without internet access.

---

## 📱 APK Download & Installation

The signed debug APK is generated and ready to run on any Android device running Android 7.0 (API level 24) or higher:

- **Pre-built APK**: `the_light_app.apk`
- **Gradle Build Output**: `app/build/outputs/apk/debug/app-debug.apk`

### How to Install:
1. Download `the_light_app.apk` to your Android device.
2. Open the file and allow "Install from Unknown Sources" if prompted.
3. Tap **Install** and launch **THE LIGHT**.

---

## 🛠️ Build from Source

Requirements:
- Android SDK 36 (Minimum SDK: 24)
- Java 11 / 17
- Gradle 8+

To assemble the APK:
```bash
gradle :app:assembleDebug
```

To run unit and Robolectric tests:
```bash
gradle :app:testDebugUnitTest
```
