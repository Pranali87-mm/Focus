**Focus – Body-Double Pomodoro Study App**

Focus is a calm, body-double based Pomodoro study app for college students and ADHD-friendly users. It helps users maintain focus without burnout by providing a quiet visual companion, gentle reflections, and subtle progression cues. Unlike typical productivity apps, Focus avoids gamification, streaks, and pressure, emphasizing sustainable focus and presence.

**Features
**
Preset Pomodoro Timers: 25/5, 45/10, 50/10 minute cycles.

Planned Session Count: Users select how many sessions they intend to complete (2, 4, 6).

Body-Double Character: Always present during focus; minimal animations (breathing, blink, micro-posture shifts).

Break Screen: Character posture relaxes; lighting softens to encourage rest.

Character States: Balanced / Strained / Exhausted, based on session completion and breaks.

Reflection & Insights: Shows completed vs planned sessions, focus/break ratio, and character states timeline.

Story-Based Moments: Unlock subtle, static images of the character in different life moments. Images hint at a timeline but are never explicitly explained.

Local Storage Only: All data is stored on-device via Room; no cloud or account required.

**Design
**
Aesthetic: Warm, autumn-cozy, calm and minimal interface.

Character: Maximalist illustrations; subtle presence to avoid distraction.

Material 3 Principles: Spacing, elevation, and components consistent throughout.

**Screen Flow (MVP)**

Onboarding / Intro – Philosophy and timer explanation with minimal text or short GIF.

Character Selection / Setup – Optional initial choice for body-double appearance.

Timer Setup – Select Pomodoro preset and planned session count.

Focus Screen – Timer + character; micro-animations only.

Break Screen – Relaxed character, warmer environment.

Reflection Screen – Shows session completion, balance, and character state messages.

Insights Screen – Graphs for focus vs break, session completion trends, state timeline.

Moments Collection – Gallery of unlocked story-based images of the character; chronological hints, no explicit narrative.

**Tech Stack**

Language: Kotlin

UI: Jetpack Compose

Architecture: MVVM

Local Storage: Room Database

Design System: Material 3 with warm, autumn-cozy aesthetic

**How to Run**

Clone the repository:

git clone https://github.com/Pranali87-mm/Focus.git


Open in Android Studio.

Build and run on an emulator or device (minimum SDK 21).

No account setup required; data is stored locally.

**License**

MIT License – free to explore, learn, and build upon.
