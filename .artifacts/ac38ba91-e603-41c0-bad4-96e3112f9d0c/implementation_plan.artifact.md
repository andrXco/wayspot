# Refactor Home Screen Components

Decouple the top section components to follow a more modular approach, separating the header (Title + Notifications) from the search bar.

## Proposed Changes

### Home Screen

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/felip/StudioProjects/wayspot/app/src/main/java/com/example/wayspot/ui/screens/home/HomeScreen.kt)
- Replace `HomeTopSection` with individual calls to `HomeHeader` and `WaySpotSearchBar`.
- Update imports accordingly.

#### [DELETE] [HomeTopSection.kt](file:///C:/Users/felip/StudioProjects/wayspot/app/src/main/java/com/example/wayspot/ui/screens/home/components/HomeTopSection.kt)
- Remove this file as it is no longer needed and creates redundancy.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to ensure the project still builds correctly.

### Manual Verification
- Verify the `HomeScreenPreview` in Android Studio to confirm the UI layout:
    1. Header at the top.
    2. Search bar below the header.
    3. Post list below the search bar.
