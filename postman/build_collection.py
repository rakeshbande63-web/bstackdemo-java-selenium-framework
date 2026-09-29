import json


def test_script(lines):
    return {"listen": "test", "script": {"type": "text/javascript", "exec": lines}}


def pre_script(lines):
    return {"listen": "prerequest", "script": {"type": "text/javascript", "exec": lines}}


def body(obj):
    return {"mode": "raw", "raw": json.dumps(obj, indent=2),
            "options": {"raw": {"language": "json"}}}


def url(path):
    parts = [p for p in path.strip("/").split("/") if p]
    return {"raw": "{{baseUrl}}/" + path.strip("/"), "host": ["{{baseUrl}}"], "path": parts}


def auth_header():
    return [{"key": "Content-Type", "value": "application/json"},
            {"key": "Authorization", "value": "Bearer {{token}}"}]


items = []

# TC_001 - Add New User
items.append({
    "name": "TC_001 - Add New User",
    "event": [
        pre_script(["pm.collectionVariables.set('runEmail', 'capstone.' + Date.now() + '@fake.com');"]),
        test_script([
            "const jsonData = pm.response.json();",
            "pm.test('Status code is 201 Created', function () {",
            "    pm.expect(pm.response.code).to.eql(201);",
            "});",
            "pm.test('Response has an auth token', function () {",
            "    pm.expect(jsonData).to.have.property('token');",
            "    pm.expect(jsonData.token).to.be.a('string').and.to.not.be.empty;",
            "});",
            "pm.test('Response user email matches the request', function () {",
            "    pm.expect(jsonData.user.email).to.eql(pm.collectionVariables.get('runEmail'));",
            "});",
            "pm.collectionVariables.set('token', jsonData.token);",
        ]),
    ],
    "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": body({"firstName": "Test", "lastName": "User", "email": "{{runEmail}}", "password": "myPassword"}),
        "url": url("users"),
    },
})

# TC_002 - Get user Profile
items.append({
    "name": "TC_002 - Get user Profile",
    "event": [test_script([
        "const jsonData = pm.response.json();",
        "pm.test('Status code is 200 OK', function () {",
        "    pm.expect(pm.response.code).to.eql(200);",
        "});",
        "pm.test('Profile email matches the created user', function () {",
        "    pm.expect(jsonData.email).to.eql(pm.collectionVariables.get('runEmail'));",
        "});",
    ])],
    "request": {
        "method": "GET",
        "header": auth_header(),
        "url": url("users/me"),
    },
})

# TC_003 - Update User
items.append({
    "name": "TC_003 - Update User",
    "event": [
        pre_script([
            "pm.collectionVariables.set('updatedEmail', 'capstone.updated.' + Date.now() + '@fake.com');",
            "pm.collectionVariables.set('updatedPassword', 'myNewPassword');",
        ]),
        test_script([
            "const jsonData = pm.response.json();",
            "pm.test('Status code is 200 OK', function () {",
            "    pm.expect(pm.response.code).to.eql(200);",
            "});",
            "pm.test('Email and first name reflect the update', function () {",
            "    pm.expect(jsonData.email).to.eql(pm.collectionVariables.get('updatedEmail'));",
            "    pm.expect(jsonData.firstName).to.eql('Updated');",
            "});",
        ]),
    ],
    "request": {
        "method": "PATCH",
        "header": auth_header(),
        "body": body({"firstName": "Updated", "lastName": "Username",
                       "email": "{{updatedEmail}}", "password": "{{updatedPassword}}"}),
        "url": url("users/me"),
    },
})

# TC_004 - Log In User
items.append({
    "name": "TC_004 - Log In User",
    "event": [test_script([
        "const jsonData = pm.response.json();",
        "pm.test('Status code is 200 OK', function () {",
        "    pm.expect(pm.response.code).to.eql(200);",
        "});",
        "pm.test('Response has a fresh auth token', function () {",
        "    pm.expect(jsonData.token).to.be.a('string').and.to.not.be.empty;",
        "});",
        "pm.collectionVariables.set('token', jsonData.token);",
    ])],
    "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": body({"email": "{{updatedEmail}}", "password": "{{updatedPassword}}"}),
        "url": url("users/login"),
    },
})

# TC_005 - Add Contact
items.append({
    "name": "TC_005 - Add Contact",
    "event": [test_script([
        "const jsonData = pm.response.json();",
        "pm.test('Status code is 201 Created', function () {",
        "    pm.expect(pm.response.code).to.eql(201);",
        "});",
        "pm.test('Response has a contact _id', function () {",
        "    pm.expect(jsonData).to.have.property('_id');",
        "});",
        "pm.collectionVariables.set('contactId', jsonData._id);",
    ])],
    "request": {
        "method": "POST",
        "header": auth_header(),
        "body": body({
            "firstName": "John", "lastName": "Doe", "birthdate": "1970-01-01",
            "email": "jdoe@fake.com", "phone": "8005555555", "street1": "1 Main St.",
            "street2": "Apartment A", "city": "Anytown", "stateProvince": "KS",
            "postalCode": "12345", "country": "USA",
        }),
        "url": url("contacts"),
    },
})

# TC_006 - Get Contact List
items.append({
    "name": "TC_006 - Get Contact List",
    "event": [test_script([
        "pm.test('Status code is 200 OK', function () {",
        "    pm.expect(pm.response.code).to.eql(200);",
        "});",
        "pm.test('Contact list contains at least one contact', function () {",
        "    const jsonData = pm.response.json();",
        "    pm.expect(jsonData).to.be.an('array').that.is.not.empty;",
        "});",
    ])],
    "request": {"method": "GET", "header": auth_header(), "url": url("contacts")},
})

# TC_007 - Get Contact
items.append({
    "name": "TC_007 - Get Contact",
    "event": [test_script([
        "const jsonData = pm.response.json();",
        "pm.test('Status code is 200 OK', function () {",
        "    pm.expect(pm.response.code).to.eql(200);",
        "});",
        "pm.test('Contact email matches what was added', function () {",
        "    pm.expect(jsonData.email).to.eql('jdoe@fake.com');",
        "});",
    ])],
    "request": {"method": "GET", "header": auth_header(), "url": url("contacts/{{contactId}}")},
})

# TC_008 - Update full contact (PUT)
items.append({
    "name": "TC_008 - Update full contact",
    "event": [test_script([
        "const jsonData = pm.response.json();",
        "pm.test('Status code is 200 OK', function () {",
        "    pm.expect(pm.response.code).to.eql(200);",
        "});",
        "pm.test('Email reflects the full update', function () {",
        "    pm.expect(jsonData.email).to.eql('amiller@fake.com');",
        "});",
    ])],
    "request": {
        "method": "PUT",
        "header": auth_header(),
        "body": body({
            "firstName": "Amy", "lastName": "Miller", "birthdate": "1992-02-02",
            "email": "amiller@fake.com", "phone": "8005554242", "street1": "13 School St.",
            "street2": "Apt. 5", "city": "Washington", "stateProvince": "QC",
            "postalCode": "A1A1A1", "country": "Canada",
        }),
        "url": url("contacts/{{contactId}}"),
    },
})

# TC_009 - Update partial contact (PATCH)
items.append({
    "name": "TC_009 - Update partial contact",
    "event": [test_script([
        "const jsonData = pm.response.json();",
        "pm.test('Status code is 200 OK', function () {",
        "    pm.expect(pm.response.code).to.eql(200);",
        "});",
        "pm.test('First name reflects the partial update', function () {",
        "    pm.expect(jsonData.firstName).to.eql('Anna');",
        "});",
    ])],
    "request": {
        "method": "PATCH",
        "header": auth_header(),
        "body": body({"firstName": "Anna"}),
        "url": url("contacts/{{contactId}}"),
    },
})

# TC_010 - Logout User (+ best-effort cleanup)
items.append({
    "name": "TC_010 - Logout User",
    "event": [test_script([
        "pm.test('Status code is 200 OK', function () {",
        "    pm.expect(pm.response.code).to.eql(200);",
        "});",
    ])],
    "request": {"method": "POST", "header": auth_header(), "url": url("users/logout")},
})

items.append({
    "name": "Cleanup - Delete Contact (not in flow; keeps re-runs clean)",
    "event": [test_script([
        "pm.test('Status code is 200 or 204', function () {",
        "    pm.expect([200, 204]).to.include(pm.response.code);",
        "});",
    ])],
    "request": {"method": "DELETE", "header": auth_header(), "url": url("contacts/{{contactId}}")},
})

items.append({
    "name": "Cleanup - Delete User (not in flow; keeps re-runs clean)",
    "event": [test_script([
        "pm.test('Status code is 200 or 204', function () {",
        "    pm.expect([200, 204]).to.include(pm.response.code);",
        "});",
    ])],
    "request": {"method": "DELETE", "header": auth_header(), "url": url("users/me")},
})

collection = {
    "info": {
        "name": "Contact List Capstone - API Tests",
        "_postman_id": "b6f2f1d0-0000-4c00-9c00-contactlistcapstone",
        "description": "Telecom Domain capstone: Add User -> Get profile -> Update user -> Login -> "
                        "Add Contact -> Get contact list -> Get contact -> Update full contact -> "
                        "Update partial contact -> Logout. Assertions use Postman's built-in chai "
                        "'pm.expect' / 'pm.test' API. Run with the collection runner or newman; "
                        "requests execute top-to-bottom so variables (runEmail, token, contactId) "
                        "carry across requests automatically.",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
    },
    "item": items,
    "variable": [
        {"key": "baseUrl", "value": "https://thinking-tester-contact-list.herokuapp.com"},
        {"key": "runEmail", "value": ""},
        {"key": "updatedEmail", "value": ""},
        {"key": "updatedPassword", "value": ""},
        {"key": "token", "value": ""},
        {"key": "contactId", "value": ""},
    ],
}

with open("ContactList-Capstone.postman_collection.json", "w") as f:
    json.dump(collection, f, indent=2)

print("wrote", len(items), "requests")
