# 🚆 Train Routes Finder

Train Routes Finder is a Java-based application that helps users find train routes between cities, offering a user-friendly graphical interface for exploring direct and multi-stop connections. The system efficiently processes train route searches while displaying pricing and real-time information.

## 🏗️ Project Structure
-  📂 User Interface – A JavaFX-based GUI for searching and displaying train routes.
-  📂 Service Layer – Implements business logic, including route search and fare calculation.
-  📂 Repository Layer – Manages database interaction for retrieving train schedules and routes.

## 🌟 Key Features
-  🔍 Train Route Search – Users can input departure and destination cities to find possible connections.
-  ✅ Direct Route Filter – Option to display only direct train routes.
-  📍 Multi-Stop Route Visualization – Shows routes requiring train changes using an efficient backtracking algorithm.
-  👥 Live User Count – Displays how many users are searching for the same route.
-  💰 Dynamic Pricing Calculation – Calculates trip costs based on the number of stations.

## 🛠️ Technologies & Tools Used
-  💻 Programming Language: Java
-  🖥️ UI Framework: JavaFX
-  🗃️ Database: PostgreSQL (for storing train routes and schedules)
-  🔌 Development Environment: IntelliJ IDEA
-  🔄 Algorithmic Approach: Backtracking for route discovery

## 📌 Design Patterns & Modern Java Features
-  🧩 Backtracking Algorithm – Efficiently finds all possible train routes, ensuring optimal path discovery.
-  🔄 Observer Pattern – Tracks user searches in real-time, providing live updates on route availability.
-  ⚡ Streams API & Lambda Expressions – Enhances performance by streamlining data processing.

