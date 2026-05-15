# Vidyarthi-Bus 🚌
> Crowdsourced real-time bus occupancy alert for rural college students

**Developer:** Sriram A S  
**USN:** 1HK22IS109  
**College:** HKBK College of Engineering  
**Department:** Information Science & Engineering  
**Subject:** BIS803 — Android App Development using GenAI  
**Semester:** VIII | 2025-26  

## Problem
Students in rural areas board college buses with no idea if a seat is available. A full bus means a missed exam. There is no existing real-time solution.

## Solution
Vidyarthi-Bus lets students already on the bus report seat status with one tap. Waiting students see a live color-coded Crowd Meter before the bus arrives.

## Tech Stack
- Android (Kotlin) — min SDK 26
- Firebase Realtime Database
- Firebase Anonymous Authentication  
- Android Fused Location Provider API

## Features
- Live Crowd Meter (Green / Amber / Red)
- One-tap crowd reporting with GPS verification
- 15-minute auto-expiry on stale reports
- Shared Auto fallback contacts when bus is full

## How to Run
1. Clone this repo
2. Add your own google-services.json from Firebase Console
3. Enable Realtime Database + Anonymous Auth
4. Build and run in Android Studio (JDK 17, AGP 8.x)
