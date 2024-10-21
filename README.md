Meeting Calendar Assistant API

Overview
The Meeting Calendar Assistant API allows users to manage meetings by booking them, checking for conflicts, and finding available time slots for employees.

API Endpoints

1. Book Meeting
    Method: POST
    URL: http://localhost:8080/api/meetings/book
    Request Body: JSON format.

    Example JSON Body
    {
        "startTime": "2024-10-25T10:00:00",
        "endTime": "2024-10-25T11:00:00",
        "participants": ["participant1@example.com", "participant2@example.com"]
    }

2. Check Conflicts
    Method: POST
    URL: http://localhost:8080/api/meetings/check-conflicts
    Request Body: JSON format.

    Example JSON Body
    {
        "startTime": "2024-10-25T10:00:00",
        "endTime": "2024-10-25T11:00:00",
        "participants": ["participant1@example.com", "participant2@example.com"]
    }

3. Free Slots
    Method: POST
    URL: http://localhost:8080/api/meetings/free-slots
    Request Body: JSON format.

    Example JSON Body
    {
    "employee1Meetings": [
        {
            "startTime": "2024-10-25T08:00:00",
            "endTime": "2024-10-25T09:00:00"
        },
        {
            "startTime": "2024-10-25T09:15:00",
            "endTime": "2024-10-25T10:00:00"
        }
    ],
    "employee2Meetings": [
        {
            "startTime": "2024-10-25T09:00:00",
            "endTime": "2024-10-25T09:45:00"
        }
    ],
    "duration": 30
    }


Testing the API
You can test the API using tools like Postman:

    1. Select the HTTP method (POST).
    2. Enter the URL for the desired endpoint.
    3. Select 'Body' and then choose 'raw'.
    4. Select 'JSON' from the dropdown menu.
    5. Paste the appropriate JSON body as shown in the examples.


## Author
Developed by Hitesh Dewangan. Passionate about creating efficient and user-friendly applications.
