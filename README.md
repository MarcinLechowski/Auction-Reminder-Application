Auction Reminder Application

This is a Spring Boot web application that sends email notifications about Allegro auctions that are nearing their end. The application integrates with the Allegro API to fetch auction data and uses email notifications to alert users about upcoming auction endings.

About Me

I am a dedicated developer with a passion for building efficient and scalable web applications. My focus is on leveraging Spring Boot to create robust solutions that meet real-world needs.

Features

Auction Reminder: Retrieves auction data from the Allegro API and sends email notifications for auctions that are ending soon.
Email Notification: Notifies users via email with key auction details such as title, end time, and a direct link to the auction.
Time Filtering: Filters auctions based on a specified time window (in minutes) to determine which auctions are ending soon.
Planned Enhancements
The application is currently undergoing improvements. Future updates will include:

Assortment-Specific Notifications: Ability to filter and send notifications for auctions belonging to specific product assortments.
Additional features to further customize and enhance the notification system.

How It Works

ReminderController: Provides a REST API endpoint (/api/remind) that accepts an email address and a time window (in minutes). It fetches the auctions ending soon and triggers the notification service.
AllegroService: Communicates with the Allegro API to fetch auction data. It processes the API response and maps the data into a list of Auction objects.
NotificationService: Uses Spring’s JavaMailSender to send email notifications containing auction details.

Getting Started

Prerequisites
Java 11 or higher
Maven or Gradle for building the project
An Allegro API access token (configured in the application properties)
Email server settings for Spring's JavaMailSender
Running the Application
Clone the repository.

Configure the application properties:
Set up the Allegro access token and email server details in application.properties or application.yml.
Build the project:
For Maven:
./mvnw clean install
Run the application:
./mvnw spring-boot:run

API Endpoint
GET /api/remind
Parameters:
email: The recipient's email address.
minutesBeforeEnd: The time window (in minutes) to filter auctions that are ending soon.
Response: Returns a message indicating the result of the notification process.

Contributing
Contributions are welcome! Please feel free to submit pull requests or raise issues for any improvements or bug fixes.
