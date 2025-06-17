CampusPlus

Description
CampusPlus is an Android application developed using Java in Android Studio, designed to serve as a comprehensive platform for campus-related activities. It provides user authentication (login/signup with email and social media providers like Facebook, Google, Twitter, and Apple), news updates (faculty, exam, and sports), profile management, settings, and developer information, delivering trusted content to students and faculty with a user-friendly dark-themed interface.


Table of Contents
Installation
Usage
Features
File Structure
Screens
Contributing
License
Contact

Installation
To set up the project locally:
# Clone the repository
git clone https://github.com/your-username/CampusPlus.git
# Navigate to the project directory
cd CampusPlus
# Open the project in Android Studio
# Ensure Android Studio is installed with the necessary SDKs (API level 33 recommended)
# Build the project
./gradlew build

Usage
To run the application:
Connect an Android device or start an emulator in Android Studio.
Select "Run" from the Android Studio menu to build and deploy the app.
Explore the app's features:
Authenticate with email/password or social media accounts.
Access news categories (Faculty, Exam, Sports).
Manage profile details and settings.
View developer information.

Features
User authentication with email/password and social media integration (Facebook, Google, Twitter, Apple).
News feed with categories: Faculty News, Exam News, and Sports News.
Profile management with editable fields (name, email, personal statement).
Settings for navigation and logout functionality.
Developer information section with student index and version details.
Dark-themed UI with intuitive navigation.
Splash screen for app initialization.

File Structure
CampusPlus/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/campusplus/
│   │   │   │       ├── AppleActivity.java       # Apple login/signup activity
│   │   │   │       ├── AppleSignUp.java         # Apple signup logic
│   │   │   │       ├── DeveloperInfoActivity.java # Developer details
│   │   │   │       ├── EditProfileActivity.java  # Profile editing logic
│   │   │   │       ├── ExamNewsActivity.java    # Exam news display
│   │   │   │       ├── FacultyNewsActivity.java  # Faculty news display
│   │   │   │       ├── FbActivity.java          # Facebook activity
│   │   │   │       ├── FbSignUp.java            # Facebook signup logic
│   │   │   │       ├── GoogleActivity.java      # Google login/signup activity
│   │   │   │       ├── GoogleSignUp.java        # Google signup logic
│   │   │   │       ├── LoginActivity.java       # Login screen logic
│   │   │   │       ├── MainActivity.java        # Main entry point
│   │   │   │       ├── NewsMainActivity.java    # News feed management
│   │   │   │       ├── ProfileActivity.java     # Profile management
│   │   │   │       ├── SettingsActivity.java    # Settings navigation
│   │   │   │       ├── SignUpActivity.java      # General signup logic
│   │   │   │       ├── SplashScreenActivity.java # Splash screen logic
│   │   │   │       ├── SportsNewsActivity.java  # Sports news display
│   │   │   │       └── TwitterActivity.java     # Twitter login/signup activity
│   │   │   │       └── TwitterSignUp.java       # Twitter signup logic
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_exam_news.xml    # Exam news layout
│   │   │   │   │   ├── activity_faculty_news.xml # Faculty news layout
│   │   │   │   │   ├── activity_login.xml        # Login layout
│   │   │   │   │   ├── activity_main.xml         # Main layout
│   │   │   │   │   ├── activity_news_item.xml    # News item layout
│   │   │   │   │   ├── activity_news_main.xml    # News feed layout
│   │   │   │   │   ├── activity_profile.xml      # Profile layout
│   │   │   │   │   ├── activity_register.xml     # Register layout
│   │   │   │   │   ├── activity_settings.xml     # Settings layout
│   │   │   │   │   ├── activity_sports_news.xml  # Sports news layout
│   │   │   │   │   ├── apple_main.xml            # Apple login layout
│   │   │   │   │   ├── apple_signup_main.xml     # Apple signup layout
│   │   │   │   │   ├── dialog_report_news.xml    # Dialog for news reporting
│   │   │   │   │   ├── fb_main.xml               # Facebook login layout
│   │   │   │   │   ├── fb_signup_main.xml        # Facebook signup layout
│   │   │   │   │   ├── google_main.xml           # Google login layout
│   │   │   │   │   ├── google_signup_main.xml    # Google signup layout
│   │   │   │   │   ├── login_main.xml            # Login main layout
│   │   │   │   │   ├── menu_item.xml             # Menu item layout
│   │   │   │   │   ├── news_main.xml             # News main layout
│   │   │   │   │   ├── profile_edit_main.xml     # Profile edit layout
│   │   │   │   │   ├── profile_main.xml          # Profile main layout
│   │   │   │   │   ├── settings_main.xml         # Settings layout
│   │   │   │   │   ├── signup_main.xml           # Signup main layout
│   │   │   │   │   ├── twitter_main.xml          # Twitter login layout
│   │   │   │   │   └── twitter_signup_main.xml   # Twitter signup layout
│   │   │   │   ├── menu/
│   │   │   │   │   └── bottom_nav_menu.xml       # Bottom navigation menu
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml              # String resources
│   │   │   │   │   ├── colors.xml               # Color definitions
│   │   │   │   │   └── styles.xml               # Theme and style definitions
│   │   │   │   ├── drawable/                    # Icons and images (e.g., user icons, logos)
│   │   │   │   └── mipmap/                     # App icons
│   │   │   ├── AndroidManifest.xml             # App configuration
│   │   │   └── ic_launcher.png                 # App launcher icon
│   │   └── test/
│   │       ├── java/
│   │       │   └── com/example/campusplus/androidTest/
│   │       │       └── ExampleInstrumentedTest.java # Instrumentation tests
│   │       └── com/example/campusplus/test/
│   │           └── ExampleUnitTest.java        # Unit tests
│   └── build.gradle                            # App-level build configuration
├── gradle/
│   └── wrapper/                                # Gradle wrapper files
├── .gitignore                                  # Git ignore file
├── build.gradle                                # Project-level build configuration
├── gradlew                                     # Gradle wrapper script
├── gradlew.bat                                 # Gradle wrapper batch script
└── settings.gradle                             # Project settings

Screens
Login Screen: Displays login form with email, password, social login options, and "Save Data" checkbox.
Register with Facebook: Shows registration with username, password, and signup button.
Register Screen: Includes username/email, password, confirm password, email, statement, and social login options.
News Feed: Features categories like sports, exam, and faculty updates.
Faculty News: Highlights a new dean appointment announcement.
Exam News: Details the semester 2 final exam timetable release.
Sports News: Covers the inter-faculty football tournament finals.
Profile Screen: Shows user profile with edit, settings, logout, and developer info options.
Edit Profile: Allows editing of name, email, and personal statement.
Developer Info: Displays developer name, email, student index, and version.
Settings: Offers navigation to Login, Profile, News, and Developer Information.

Contributing
Contributions are welcome! Follow these steps:
Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make your changes and commit (git commit -m 'Add feature').
Push to the branch (git push origin feature-branch).
Open a Pull Request.
Please adhere to the project's coding standards and test your changes.

License
This project is licensed under the MIT License. See the LICENSE file for details.

Contact
For questions or feedback, reach out to:
Your Name:chathumi.rathnayaka25@gmail.com
GitHub:chathumi25

