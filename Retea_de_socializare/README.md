# 📄 Social Network Application Project
This project is a Social Network Application developed in Java, providing core social features like friendship management, private messaging, private group creation, and real-time notifications. It simulates a messaging platform where users can interact through friend requests, private groups, and notifications, enhancing the social experience.

##🏗️ Project Structure
The repository is organized into modules covering the key features of a social network:

-  📂 User Management – Handles user registration, login, and managing profiles.

-  📂 Friendship Management – Manages sending, accepting, and rejecting friend requests, as well as viewing mutual friends.

-  📂 Private Messaging & Group Chat – Allows users to send private messages, as well as create and manage private groups for communication.

-  📂 Database Handling – Manages database interactions for storing user data, friend requests, messages, and group information.

-  📂 Notifications – Handles the notification system, alerting users about events like new friend requests or new messages.

## 🎨 Core Features of the Social Network
### 1️⃣ User Profiles

-  Registration & Login: Users can create an account, log in, and manage their personal profiles (e.g., username, password).

-  Profile Information: Users have details like their username, user ID, and profile preferences.

### 2️⃣ Friend Requests & Management

-  Send/Receive Friend Requests: Users can send and receive friend requests to establish connections.

-  Accept/Reject Requests: Friend requests can be accepted or rejected.

-  View Friends: Users can see their friends and view mutual friends between them.

### 3️⃣ Private Messaging

-  Direct Messages: Users can send private messages to their friends.

-  Message History: Keeps a history of messages exchanged between users.

-  Inbox: Displays unread and old messages in an organized manner for easy navigation.

### 4️⃣ Private Group Chat

-  Create Groups: Users can create private groups for specific social interactions.

-  Group Messaging: Members of a group can send and receive messages within the group.

-  Manage Group Members: Users can add or remove members from the group, ensuring only invited individuals can participate.

-  Group Privacy: Groups allow for private communication, ensuring confidentiality within a defined circle of friends.

### 5️⃣ Notifications

-  Real-Time Notifications: The application sends real-time notifications to users for various events, such as new friend requests or new messages.

-  Notification System: Ensures users are kept updated on the latest interactions and social events within the app, making the experience more engaging and interactive.

-  Read/Unread Status: Notifications are marked as read or unread based on user interactions.

### 6️⃣ Database Interaction

-  Database Connectivity: The app interacts with a database to manage user data, friend requests, messages, and group chats.

-  SQL Queries: Data is retrieved and stored using SQL queries, including user details, friends list, and group information.

-  Data Integrity: Ensures relational constraints are maintained for secure data storage.

## 🛠️ Technical Highlights
-  💻 Language: Java

-  🖥️ Technologies Used: JavaFX for the UI, SQL for database management, JDBC for database connectivity.

-  📄 Database: Uses SQL to store and manage user profiles, friend requests, messages, groups, and notifications.

-  🔄 Event Handling: Implements ActionListeners and MouseListeners to manage user interactions and dynamically update the interface.

-  👥 Friend Management: Provides features to send, accept, and reject friend requests and view mutual friends.

-  💬 Private Messaging: Allows users to send messages privately to their friends, with a stored history of conversations.

-  💬 Private Group Chat: Supports creating and managing private groups, enabling users to communicate within smaller, more secure circles of friends.

-  📲 Real-Time Notifications: Sends instant notifications to users about important activities such as friend requests or messages.

-  🛠️ Modular Design: Designed with separate classes for each feature, enabling easier maintenance and future improvements.

## 🎨 User Interface
-  🖥️ GUI Design: Built with JavaFX, offering an interactive interface that allows users to manage friends, send messages, and create groups.

-  💬 Messaging Interface: Simple interface for private messaging and managing conversations.

-  👥 Friend Management Interface: Displays the list of friends and allows sending/accepting friend requests.

-  👥 Group Management Interface: Provides a view of all private groups, with options to create and manage them.

-  🔔 Notifications: Displays notifications for incoming messages, friend requests, and other relevant social interactions.

-  🔒 Secure Login: Users can log in securely to access their private network of friends and groups.

## 🌟 Key Features
-  User Registration & Login: Secure user registration and login system for profile management.

-  Friend Requests & Management: Send and receive friend requests, accept/reject, and view mutual friends.

-  Private Messaging: Direct messaging between users, with a saved message history.

-  Private Group Chat: Allows users to create private groups for specific conversations, where only invited friends can participate.

-  Group Management: Users can add/remove members from groups, ensuring the group's privacy.

-  Notifications: Real-time notifications alert users about new events like friend requests, new messages, or group invitations.

-  Database-Backed Data: Stores user profiles, messages, friend requests, groups, and notifications in a relational database.

-  User-Friendly Interface: Clean and intuitive interface for easy navigation and interaction.

-  Event-Driven Interactions: Uses ActionListeners for handling user actions such as sending messages, adding friends, or creating groups.

## 🛠️ Technical Challenges
-  Database Interaction: Implementing efficient SQL queries for managing large datasets such as user profiles, messages, friend requests, and group memberships.

-  Private Group Creation: Designing a system where groups can be formed, members can be added or removed, and communication is restricted to invited members.

-  Real-Time Communication: Handling real-time messaging and group chat, ensuring smooth user interactions.

-  Notifications: Implementing a real-time notification system for new friend requests, messages, and group interactions.

-  User Interface: Creating an intuitive interface for managing users, friends, messages, groups, and notifications.


## 🛠️ Technologies & Tools Used
-  💻 Programming Language: Java

-  🖥️ Framework: JavaFX (for UI components)

-  🗃️ Database: SQL (MySQL or SQLite for storing user data, friend requests, messages, groups, and notifications)

-  🔌 Database Connectivity: JDBC (Java Database Connectivity)

-  🔄 Event-Driven Programming: ActionListeners and MouseListeners for managing actions.

-  🔒 Security: User authentication and secure storage of user credentials.

-  🔔 Notifications: Real-time notifications system to keep users engaged.

