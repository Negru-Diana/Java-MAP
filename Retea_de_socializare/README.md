# 📄 Social Network Application Project
This project is a *Social Network Application* developed in Java, designed to provide core social features such as friendship management, private messaging, private group creation, and real-time notifications. The application simulates a messaging platform where users can interact through friend requests, private groups, and notifications, enriching the social experience.

## 🏗️ Project Structure
The repository is organized into modules, each focusing on a different aspect of the social network:

-  📂 User Management: Handles user registration and login.

-  📂 Friendship Management: Manages the sending, accepting, and rejecting of friend requests, along with viewing mutual friends.

-  📂 Private Messaging & Group Chat: Enables users to send private messages, reply to messages, and create private groups.

-  📂 Database Handling: Manages database interactions for storing user data, friend requests, messages, and group information.

-  📂 Notifications: Provides real-time alerts for events such as new friend requests.

## 🎨 Core Features of the Social Network
### 1️⃣ User Profiles
- Registration & Login: Users can create accounts, login.

- Profile Information: Users can view profile details.

### 2️⃣ Friend Requests & Management
-  Send/Receive Friend Requests: Users can send and receive friend requests to connect with others.

-  Accept/Reject Requests: Friend requests can be accepted or rejected by the user.

-  View Friends: Users can view their list of friends, including mutual friends with others.

### 3️⃣ Private Messaging
-  Direct Messaging: Users can send private messages to their friends.

-  Message History: Keeps a history of all messages exchanged.

-  Reply to Messages: Users can reply to specific messages, facilitating ongoing conversations.

-  Inbox: Displays unread and old messages for easy navigation.

### 4️⃣ Private Group Chat
-  Create Groups: Users can create private groups for specific social interactions.

-  Group Messaging: Users can send and receive messages within the group.

-  Group Privacy: Ensures private communication.

### 5️⃣ Notifications
-  Real-Time Notifications: The app sends notifications to users for new friend requests.

###  6️⃣ Database Interaction
-  Database Connectivity: The application interacts with a PostgreSQL database to manage user data, friend requests, messages, and groups.

-  SQL Queries: Efficient queries handle data retrieval and storage for users, friends, messages, etc.

-  Pagination: Ensures smooth data navigation.

## 🛠️ Technical Highlights
-  💻 Language: Java ( Java 8 Features)

-  🖥️ Technologies Used: JavaFX (for UI), PostgreSQL (for database), JDBC (for database connectivity)

-  📄 Database: PostgreSQL used for storing user profiles, friend requests, messages, groups, and notifications.

-  🔄 Event Handling: ActionListeners and MouseListeners manage user interactions and update the interface dynamically.

-  📱 Real-Time Updates: Utilizes the Observer/Observable Pattern to update the UI in real-time, reflecting changes like new messages or friend requests without needing to refresh.

-  💬 Private Messaging: Messages can be replied to, ensuring users can continue conversations seamlessly.

-  📲 Real-Time Notifications: Users receive instant notifications for important activities, keeping the app interactive and engaging.

-  🛠️ Modular Design: The system is divided into separate classes, each responsible for specific features, making it easy to maintain and scale.



## 🎨 Design Patterns & Modern Java Features
-  🔄 Observer/Observable Pattern: Keeps the UI automatically updated with changes such as new friend requests, messages, and notifications, without requiring a manual refresh.

-  🔢 Streams API & Lambda Expressions: Simplifies collection processing, making the code cleaner and more efficient by using the Streams API for data manipulation and lambda expressions for functional-style operations.

-  ⚡ Optional: Manages potential null values gracefully, avoiding NullPointerExceptions and improving overall app stability.



