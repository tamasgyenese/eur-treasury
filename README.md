## Treasury REST API

The REST API to the example app is described below.

### Fetch balance with the given currency for Account

#### Request

`GET /api/v1/balance/{id}/{currency}`

    curl -i -H 'Accept: application/json' http://localhost:9999/api/v1/balance/{id}/{currency}

#### Response
    
               Status = 200
        Error message = null
              Headers = [Content-Type:"application/json"]
         Content type = application/json
                 Body = {"id":1,"accountId":1,"amount":32.45,"currency":"EUR"}

### Fetch transaction mutations for Account

#### Request

`GET /api/v1/treasury/mutation/{id}`

    curl -i -H 'Accept: application/json' http://localhost:9999/api/v1/treasury/mutation/{id}

#### Response
    
               Status = 200
        Error message = null
              Headers = [Content-Type:"application/json"]
         Content type = application/json
                 Body = [
                          {
                            "transactionId": 1,
                            "amount": -33.0,
                            "partnerName": "Jessica Jones",
                            "currency": "EUR",
                            "date": "2023-09-01T12:12:12"
                          },
                          {
                            "transactionId": 2,
                            "amount": -133.0,
                            "partnerName": "Wade Wilson",
                            "currency": "EUR",
                            "date": "2023-09-02T12:12:12"
                          }
                        ]





## Database schema

![Alt text](DB.png?raw=true)