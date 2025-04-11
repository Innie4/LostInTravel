# LostInTravel

A travel companion app that helps users discover, plan, and share their travel experiences.

## Setup Instructions

### Prerequisites
- Android Studio Flamingo (2022.2.1) or newer
- JDK 17
- Android SDK 34
- Gradle 8.0.2

### Getting Started
1. Clone the repository:


2. Open the project in Android Studio:
- Launch Android Studio
- Select "Open an existing Android Studio project"
- Navigate to the cloned repository and click "Open"

3. Firebase Setup:
- Make sure the `google-services.json` file is placed in the app directory
- The Firebase configuration is already set up in the Gradle files

4. Build the project:
- Click on "Build" > "Rebuild Project"

5. Run the app:
- Connect an Android device or use an emulator
- Click on the "Run" button in Android Studio

## Architecture and Design Choices

### Architecture
The app follows the **Clean Architecture** principles with MVVM (Model-View-ViewModel) pattern:

- **Presentation Layer**: Compose UI components and ViewModels
- **Domain Layer**: Use cases, repositories interfaces, and domain models
- **Data Layer**: Repository implementations, data sources, and DTOs

### Tech Stack
- **UI**: Jetpack Compose with Material 3 design
- **Navigation**: Compose Navigation
- **Dependency Injection**: Hilt
- **Local Storage**: Room Database
- **Network**: Retrofit with OkHttp
- **Asynchronous Operations**: Kotlin Coroutines and Flow
- **Image Loading**: Coil
- **Authentication**: Firebase Authentication
- **Logging**: Timber

### Design Patterns
- **Repository Pattern**: Abstracts data sources from the rest of the app
- **Factory Pattern**: For creating complex objects
- **Observer Pattern**: Using Kotlin Flow for reactive programming
- **Dependency Injection**: Using Hilt to manage dependencies

## Trade-offs and Limitations

### Performance Considerations
- Image caching is limited to Coil's default settings
- No offline-first approach implemented yet

### UI/UX Limitations
- Limited animations and transitions
- No dark mode support yet
- No tablet-specific layouts

### Technical Limitations
- Firebase is only used for authentication, not for data storage
- No real-time data synchronization
- Limited error handling for network failures

## Future Improvements

### Short-term Improvements
- Implement comprehensive error handling
- Add offline support with data synchronization
- Enhance UI with animations and transitions
- Add dark mode support

### Medium-term Improvements
- Implement social features (sharing trips, following friends)
- Add push notifications for trip reminders
- Integrate with maps for location-based features
- Implement trip planning with itinerary builder

### Long-term Vision
- Add AI-powered trip recommendations
- Implement augmented reality features for landmark recognition
- Create a web version for cross-platform access
- Develop a marketplace for local experiences and guides
