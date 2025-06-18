# Test Case Document: tracKKar API

## Overview
This document contains comprehensive test cases for the tracKKar API, covering all major features and endpoints. Use this for manual testing, automated testing, or as a reference for QA teams.

---

## 1. User Management

### 1.1 User Registration
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-1.1.1 | Register with valid data | Valid name, email, phone, password, role | 201 Created, user object in response |
| TC-1.1.2 | Register with invalid email | Invalid email format | 400 Bad Request, error message |
| TC-1.1.3 | Register with duplicate phone/email | Existing phone/email | 400 Bad Request, error message |
| TC-1.1.4 | Register with missing fields | Missing required fields | 400 Bad Request, error message |
| TC-1.1.5 | Register with invalid phone format | Phone with < 10 or > 15 digits | 400 Bad Request, error message |
| TC-1.1.6 | Register with invalid role | Invalid role value | 400 Bad Request, error message |

### 1.2 User Login
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-1.2.1 | Login with email & password | Valid email, password | 200 OK, token and user info |
| TC-1.2.2 | Login with phone & password | Valid phone, password | 200 OK, token and user info |
| TC-1.2.3 | Login with invalid credentials | Wrong email/phone or password | 401 Unauthorized, error message |
| TC-1.2.4 | Login with missing credentials | Missing email/phone or password | 400 Bad Request, error message |

### 1.3 Get Users (with Pagination & Filtering)
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-1.3.1 | Get all users | No params | 200 OK, paginated user list |
| TC-1.3.2 | Get users by role | role=ADMIN or GATEKEEPER | 200 OK, filtered user list |
| TC-1.3.3 | Search users | search param | 200 OK, filtered user list |
| TC-1.3.4 | Pagination | page, size params | 200 OK, correct page of users |
| TC-1.3.5 | Sorting | sortBy, sortDir params | 200 OK, sorted user list |
| TC-1.3.6 | Invalid role filter | Invalid role value | 400 Bad Request, error message |

### 1.4 Get User by ID
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-1.4.1 | Get existing user | Valid user ID | 200 OK, user object |
| TC-1.4.2 | Get non-existent user | Invalid user ID | 404 Not Found, error message |
| TC-1.4.3 | Get user with invalid UUID format | Invalid UUID format | 400 Bad Request, error message |

### 1.5 Delete User
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-1.5.1 | Delete existing user | Valid user ID | 200 OK, success message |
| TC-1.5.2 | Delete non-existent user | Invalid user ID | 404 Not Found, error message |
| TC-1.5.3 | Delete user with invalid UUID format | Invalid UUID format | 400 Bad Request, error message |

---

## 2. Gate Management

### 2.1 Create Gate
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.1.1 | Create gate with valid data | Valid name, lat, long, status (optional), assignedGatekeeper (optional) | 201 Created, gate object |
| TC-2.1.2 | Create gate with missing required fields | Missing name, lat, or long | 400 Bad Request, error message |
| TC-2.1.3 | Create gate with invalid coordinates | Invalid lat/long values | 400 Bad Request, error message |
| TC-2.1.4 | Create gate with invalid status | Invalid status value | 400 Bad Request, error message |
| TC-2.1.5 | Create gate with non-existent gatekeeper | Invalid gatekeeper ID | 400 Bad Request, error message |

### 2.2 Get Gates (with Pagination & Filtering)
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.2.1 | Get all gates | No params | 200 OK, paginated gate list |
| TC-2.2.2 | Get gates by status | status=OPEN or CLOSED | 200 OK, filtered gate list |
| TC-2.2.3 | Search gates | search param | 200 OK, filtered gate list |
| TC-2.2.4 | Pagination | page, size params | 200 OK, correct page of gates |
| TC-2.2.5 | Sorting | sortBy, sortDir params | 200 OK, sorted gate list |
| TC-2.2.6 | Invalid status filter | Invalid status value | 400 Bad Request, error message |

### 2.3 Get Gate by ID
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.3.1 | Get existing gate | Valid gate ID | 200 OK, gate object |
| TC-2.3.2 | Get non-existent gate | Invalid gate ID | 404 Not Found, error message |
| TC-2.3.3 | Get gate with invalid UUID format | Invalid UUID format | 400 Bad Request, error message |

### 2.4 Update Gate Status
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.4.1 | Update status with valid data | Valid gate ID, status, updatedBy | 200 OK, updated gate object |
| TC-2.4.2 | Update status with invalid gate ID | Invalid gate ID | 400 Bad Request, error message |
| TC-2.4.3 | Update status with invalid status | Invalid status value | 400 Bad Request, error message |
| TC-2.4.4 | Update status with invalid user ID | Invalid updatedBy ID | 400 Bad Request, error message |
| TC-2.4.5 | Update non-existent gate | Non-existent gate ID | 400 Bad Request, error message |

### 2.5 Delete Gate
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.5.1 | Delete existing gate | Valid gate ID | 200 OK, success message |
| TC-2.5.2 | Delete non-existent gate | Invalid gate ID | 404 Not Found, error message |
| TC-2.5.3 | Delete gate with invalid UUID format | Invalid UUID format | 400 Bad Request, error message |

### 2.6 Assign/Unassign Gatekeeper
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.6.1 | Assign gatekeeper | Valid gate ID, user ID | 200 OK, success message |
| TC-2.6.2 | Unassign gatekeeper | Valid user ID | 200 OK, success message |
| TC-2.6.3 | Assign with invalid gate ID | Invalid gate ID | 400 Bad Request, error message |
| TC-2.6.4 | Assign with invalid user ID | Invalid user ID | 400 Bad Request, error message |
| TC-2.6.5 | Assign to non-existent gate | Non-existent gate ID | 400 Bad Request, error message |
| TC-2.6.6 | Assign non-existent user | Non-existent user ID | 400 Bad Request, error message |
| TC-2.6.7 | Unassign non-existent user | Non-existent user ID | 400 Bad Request, error message |

### 2.7 Get Gates with Assigned Gatekeepers
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.7.1 | Get all gates with assigned gatekeepers | None | 200 OK, list of gates with gatekeepers |
| TC-2.7.2 | Get gates when no assignments exist | None | 200 OK, empty list |

### 2.8 Get Gate by Gatekeeper
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-2.8.1 | Get gate by gatekeeper | Valid gatekeeper ID | 200 OK, gate object |
| TC-2.8.2 | Get gate by invalid gatekeeper | Invalid gatekeeper ID | 404 Not Found, error message |
| TC-2.8.3 | Get gate by non-existent gatekeeper | Non-existent gatekeeper ID | 404 Not Found, error message |

---

## 3. Feedback Management

### 3.1 Submit Feedback
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-3.1.1 | Submit valid feedback | Valid gate ID, message, timestamp, userAgent | 200 OK, feedback object |
| TC-3.1.2 | Submit feedback with missing gate ID | Missing gate ID | 400 Bad Request, error message |
| TC-3.1.3 | Submit feedback with invalid gate ID | Invalid gate ID | 400 Bad Request, error message |
| TC-3.1.4 | Submit feedback with empty message | Empty message | 400 Bad Request, error message |
| TC-3.1.5 | Submit feedback to non-existent gate | Non-existent gate ID | 400 Bad Request, error message |

### 3.2 Get All Feedback
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-3.2.1 | Get all feedback | None | 200 OK, list of feedback |
| TC-3.2.2 | Get feedback when none exists | None | 200 OK, empty list |

### 3.3 Get Feedback for Gate
| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-3.3.1 | Get feedback for gate | Valid gate ID | 200 OK, list of feedback for gate |
| TC-3.3.2 | Get feedback for invalid gate | Invalid gate ID | 400 Bad Request, error message |
| TC-3.3.3 | Get feedback for non-existent gate | Non-existent gate ID | 200 OK, empty list |
| TC-3.3.4 | Get feedback for gate with no feedback | Valid gate ID with no feedback | 200 OK, empty list |

---

## 4. Error Handling & Validation

| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-4.1 | Invalid endpoint | Wrong URL | 404 Not Found, error message |
| TC-4.2 | Invalid method | Wrong HTTP method | 405 Method Not Allowed, error message |
| TC-4.3 | Validation errors | Invalid data in any endpoint | 400 Bad Request, error message with details |
| TC-4.4 | Malformed JSON | Invalid JSON in request body | 400 Bad Request, error message |
| TC-4.5 | Missing request body | Missing body for POST/PUT requests | 400 Bad Request, error message |
| TC-4.6 | Invalid UUID format | Invalid UUID in path variables | 400 Bad Request, error message |

---

## 5. Security (if applicable)

| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-5.1 | Access protected endpoint without token | No token | 401 Unauthorized, error message |
| TC-5.2 | Access with invalid/expired token | Invalid/expired token | 401 Unauthorized, error message |
| TC-5.3 | Access with valid token | Valid token | 200 OK, data |
| TC-5.4 | Access with malformed token | Malformed token | 401 Unauthorized, error message |

---

## 6. Performance & Load Testing

| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-6.1 | Large dataset pagination | Large number of records | 200 OK, reasonable response time |
| TC-6.2 | Concurrent requests | Multiple simultaneous requests | 200 OK, no data corruption |
| TC-6.3 | Search performance | Search with large dataset | 200 OK, reasonable response time |
| TC-6.4 | Memory usage | Long running operations | No memory leaks |

---

## 7. Data Integrity

| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|----------------|
| TC-7.1 | Delete user with assigned gate | Delete user who is assigned to gate | 400 Bad Request or cascade delete |
| TC-7.2 | Delete gate with feedback | Delete gate that has feedback | 400 Bad Request or cascade delete |
| TC-7.3 | Update user affects gate assignment | Update user assigned to gate | Gate assignment remains intact |
| TC-7.4 | Unique constraints | Duplicate email/phone | 400 Bad Request, error message |

---

## Sample Test Data

### Valid User Data
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "password": "password123",
  "role": "GATEKEEPER"
}
```

### Valid Gate Data
```json
{
  "name": "Main Gate",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "status": "OPEN",
  "assignedGatekeeper": "uuid-here"
}
```

### Valid Feedback Data
```json
{
  "gate": "uuid-here",
  "message": "Gate is working properly",
  "timestamp": "2024-01-01T10:00:00Z",
  "userAgent": "Mozilla/5.0..."
}
```

---

## Testing Tools

### Manual Testing
- **Postman**: Import the API endpoints and create collections
- **cURL**: Use command line for quick testing
- **Browser**: For GET requests

### Automated Testing
- **JUnit**: For unit and integration tests
- **TestContainers**: For database testing
- **RestAssured**: For API testing

### Performance Testing
- **JMeter**: For load testing
- **Gatling**: For performance testing

---

## Notes

1. **Environment Setup**: Ensure the application is running and database is properly configured
2. **Test Data**: Use the sample data provided or create your own test data
3. **Cleanup**: Clean up test data after testing to avoid conflicts
4. **Documentation**: Update this document as new features are added
5. **Regression Testing**: Run these tests after any code changes

---

## Test Execution Checklist

- [ ] Environment is ready
- [ ] Database is clean/configured
- [ ] Application is running
- [ ] Test data is prepared
- [ ] All test cases are executed
- [ ] Results are documented
- [ ] Issues are reported
- [ ] Test data is cleaned up

---

**Last Updated**: January 2024  
**Version**: 1.0  
**Author**: tracKKar Development Team 