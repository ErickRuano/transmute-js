FORMAT: 1A
HOST: https://sapp.miclarocorporativo.com

# SSEC

Security system API.  It contains an API to create, manage and authenticate users.

# Group Clients

## Clients Collection [/clients]

### Create client [POST]

+ Response 200 (application/json)

    + Body

            {
                "idClient" : 1,
                "name" : "Pentcloud",
                "email" : "pentcloud@pentcloud.com",
                "phone" : "+502 5444 9194",
                "claroId" : "023402043204320",
                "preExecutiveId" : 8,
                "postExecutiveId" : 12,
                "country" : 502,
                "createdAt" : "July 21, 1983 01:15:00"
            }

## Client [/clients/{idClient}]

+ Parameters

    + idClient: 1 (number) - Unique id of each client.

### Read client [GET]

+ Response 200 (application/json)

    + Body

            {
                "idClient" : 1,
                "name" : "Pentcloud",
                "email" : "pentcloud@pentcloud.com",
                "phone" : "+502 5444 9194",
                "claroId" : "023402043204320",
                "preExecutiveId" : 8,
                "postExecutiveId" : 12,
                "country" : 502,
                "createdAt" : "July 21, 1983 01:15:00",
                "updatedAt" : "July 21, 1983 01:15:00"
            }

### Update client [PUT]

+ Response 204

### Delete client [DELETE]

+ Response 204 (application/json)


# Group Users

## Claro admins collection [/admins]

### Create Claro Admin [POST]

+ Response 200 (application/json)

    + Body

            {
                "idUser" : 1,
                "username" : "erick.ruano",
                "password" : "p@ssw0rd",
                "email" : "erick.ruano@pentcloud.com",
                "firstname" : "Erick",
                "lastname" : "Ruano",
                "phone" : "+502 54449194",
                "country" : "Guatemala",
                "type" : 1,
                "profile" : 23,
                "createdAt" : "July 21, 2016 01:15:00"
            }

+ Request (application/json)

## Admin [/users/{idUser}]

+ Parameters

    + idUser: 1 (number) - Unique id of each user.

### Read user [GET]

+ Response 200 (application/json)

    + Body

            {
                "idUser" : 1,
                "username" : "erick.ruano",
                "password" : "p@ssw0rd",
                "email" : "erick.ruano@pentcloud.com",
                "firstname" : "Erick",
                "lastname" : "Ruano",
                "phone" : "+502 54449194",
                "country" : "Guatemala",
                "type" : 1,
                "profile" : 23,
                "createdAt" : "July 21, 2016 01:15:00"
            }

### Update user [PUT]

+ Response 204

### Delete user [DELETE]

+ Response 204 (application/json)

## Customer admins collection [/customer/{idCustomer}/admin]

### Create Customer Admin [POST]

+ Response 200 (application/json)

    + Body

            {
                "idUser" : 1,
                "username" : "erick.ruano",
                "password" : "p@ssw0rd",
                "email" : "erick.ruano@pentcloud.com",
                "firstname" : "Erick",
                "lastname" : "Ruano",
                "phone" : "+502 54449194",
                "country" : "Guatemala",
                "type" : 2,
                "profile" : 23,
                "createdAt" : "July 21, 2016 01:15:00"
            }

+ Request (application/json)

## Customer admin [/users/{idUser}]

+ Parameters

    + idUser: 1 (number) - Unique id of each user.

### Read user [GET]

+ Response 200 (application/json)

    + Body

            {
                "idUser" : 1,
                "username" : "erick.ruano",
                "password" : "p@ssw0rd",
                "email" : "erick.ruano@pentcloud.com",
                "firstname" : "Erick",
                "lastname" : "Ruano",
                "phone" : "+502 54449194",
                "country" : "Guatemala",
                "type" : 2,
                "profile" : 23,
                "createdAt" : "July 21, 2016 01:15:00"
            }

### Update user [PUT]

+ Response 204

### Delete user [DELETE]

+ Response 204 (application/json)

## Customer user [/users]

### Create user [POST]

+ Response 200 (application/json)

    + Body

            {
                "idUser" : 1,
                "username" : "erick.ruano",
                "password" : "p@ssw0rd",
                "email" : "erick.ruano@pentcloud.com",
                "firstname" : "Erick",
                "lastname" : "Ruano",
                "phone" : "+502 54449194",
                "country" : "Guatemala",
                "type" : 3,
                "profile" : 23,
                "createdAt" : "July 21, 2016 01:15:00"
            }

+ Request (application/json)

## User [/users/{idUser}]

+ Parameters

    + idUser: 1 (number) - Unique id of each user.

### Read user [GET]

+ Response 200 (application/json)

    + Body

            {
                "idUser" : 1,
                "username" : "erick.ruano",
                "password" : "p@ssw0rd",
                "email" : "erick.ruano@pentcloud.com",
                "firstname" : "Erick",
                "lastname" : "Ruano",
                "phone" : "+502 54449194",
                "country" : "Guatemala",
                "type" : 3,
                "profile" : 23,
                "createdAt" : "July 21, 2016 01:15:00"
            }

### Update user [PUT]

+ Response 204

### Delete user [DELETE]

+ Response 204 (application/json)

# Group Profiles

## User profile [/profiles]

### Create profile [POST]

+ Response 200 (application/json)

    + Body

            {
                "idProfile" : 1,
                "name" : "Marketing",
                "profile" : "{\"name\":\"Superadmin\",\"modules\":{\"facturacion\":true,\"consultas\":true,\"soporte\":true,\"biblioteca\":true,\"faqs\":true,\"reporteria\":true,\"administracion\":true,\"notificaciones\":true}}",
                "createdAt" : "July 21, 2016 01:15:00"
            }

+ Request (application/json)

## User [/profiles/{idProfile}]

+ Parameters

    + idProfile: 1 (number) - Unique id of each profile.

### Read profile [GET]

+ Response 200 (application/json)

    + Body

            {
                "idProfile" : 1,
                "name" : "Marketing",
                "profile" : "{\"name\":\"Superadmin\",\"modules\":{\"facturacion\":true,\"consultas\":true,\"soporte\":true,\"biblioteca\":true,\"faqs\":true,\"reporteria\":true,\"administracion\":true,\"notificaciones\":true}}",
                "createdAt" : "July 21, 2016 01:15:00"
            }

### Update profile [PUT]

+ Response 204

### Delete profile [DELETE]

+ Response 204 (application/json)

# Group Auth

## User [/login]

### User autentication [POST]

+ Response 200 (application/json)

    + Body

             {
                "username": "admin",
                "email": "admin@claro.com",
                "firstname": "Super",
                "lastname": "Admin",
                "phone": "+502 0000000",
                "country": "Guatemala",
                "type": {
                    "id": 1,
                    "name": "Superadmin"
                },
                "profile": {
                  "name": "Superadmin",
                  "modules": {
                    "facturacion": true,
                    "consultas": true,
                    "soporte": true,
                    "biblioteca": true,
                    "faqs": true,
                    "reporteria": true,
                    "administracion": true,
                    "notificaciones": true
                  }
                }

+ Request (application/json)
    
    + Attributes

        + username (string)
        + password (string)