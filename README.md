# Code Challenge

## Task description

---
A **CSMS** (charging station management system) is used to manage charging stations, charging processes and customers (
so-called eDrivers) amongst other things. One of the most important functionalities of such a CSMS is to calculate a
price to a particular charging process so that the **eDriver** can be invoiced for the consumed services. Establishing a
price for a charging process is usually done by applying a rateDto to the **CDR** (charge detail record) of the
corresponding charging process

### Charging process

During a charging process two important events are sent by the charging station to the CSMS:

- StartTransaction - constitutes the beginning of a charging process
- StopTransaction - instructs the CSMS that the charging process has ended

Every charging station has an electricity meter that is counting the overall delivered energy (in Wh) and an internal
clock. On both events a meter value retrieved from the electricity meter along with a timestamp are sent by the charging
station to the CSMS

| Event            |  Parameter  |  Description  |
|------------------|:------------|:--------------|
| StartTransaction | meterStart  | meter value of the electricity meter when the charging process was started |
|                  | timestamp   |   timestamp (according to ISO 8601) when the charging process was started |
| StopTransaction  | meterStop   |    meter value of the electricity meter when the charging process was stopped |
|                  | timestamp   |    timestamp (according to ISO 8601) when the charging process was stopped | 

### A minimal CDR is built by combining these values

| Event            |  Parameter  |  
|------------------|:------------|
| meterStart | 1204307  |
| timestampStart | 2021-04-05T10:04:00Z  |
| meterStop | 1215230  |1215230
| timestampStop | 2021-04-05T11:27:00Z  |

### Let's assume we apply the following rateDto to our example CDR

| Component            |  Conditions  |  Price  |
|------------------|:------------|:--------------|
| energy           | 0.30€ per kWh  | €3.277 |
| time             | 2€ per hour   | €2.767|
| transaction  | 1€ service fee   |    €1 |
| total amount                 |    |€7.04|

### Rate API

---

* **Description**

  Will apply the given rateDto to the corresponding CDR


* **Endpoint**

  /rate


* **Method:**

  `POST`


* **URL Params**


* **Request Body:**

  Request body structure depicted in json-schema format

   ```
    {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "type": "object",
      "properties": {
        "rateDto": {
          "type": "object",
          "properties": {
            "energy": {
              "type": "number"
            },
            "time": {
              "type": "integer"
            },
            "transaction": {
              "type": "integer"
            }
          },
          "required": [
            "energy",
            "time",
            "transaction"
          ]
        },
        "CDRdto": {
          "type": "object",
          "properties": {
            "meterStart": {
              "type": "integer"
            },
            "timestampStart": {
              "type": "string"
            },
            "meterStop": {
              "type": "integer"
            },
            "timestampStop": {
              "type": "string"
            }
          },
          "required": [
            "meterStart",
            "timestampStart",
            "meterStop",
            "timestampStop"
          ]
        }
      },
      "required": [
        "rateDto",
        "CDRdto"
      ]
    }
  ```

* **Success Response:**

    * **Code:** 200

      response body structure depicted in json-schema format

   ```
    {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "type": "object",
      "properties": {
        "rateDto": {
          "type": "object",
          "properties": {
            "energy": {
              "type": "number"
            },
            "time": {
              "type": "integer"
            },
            "transaction": {
              "type": "integer"
            }
          },
          "required": [
            "energy",
            "time",
            "transaction"
          ]
        },
        "CDRdto": {
          "type": "object",
          "properties": {
            "meterStart": {
              "type": "integer"
            },
            "timestampStart": {
              "type": "string"
            },
            "meterStop": {
              "type": "integer"
            },
            "timestampStop": {
              "type": "string"
            }
          },
          "required": [
            "meterStart",
            "timestampStart",
            "meterStop",
            "timestampStop"
          ]
        }
      },
      "required": [
        "rateDto",
        "CDRdto"
      ]
    }
  ```    

* **Error Response:**

    * **Code:** 404 NOT FOUND

  OR

    * **Code:** 400 BAD REQUEST

### A sample post request body

```
{
    "rate": {
        "energy": 0.3,
        "time": 2,
        "transaction": 1
    },
    "cdr": {
        "meterStart": 1204307,
        "timestampStart": "2021-04-05T10:04:00Z",
        "meterStop": 1215230,
        "timestampStop": "2021-04-05T11:27:00Z"
    }
}
```

### A sample response

```
{
    "overall": 7.044,
    "components": {
        "energy": 3.277,
        "time": 2.767,
        "transaction": 1
    }
}
```

## How to run project

---

### System Requirements:

* **Java 8**

* **Maven**

* **Docker:**

### running unit tests:

```
$ mvn test
```

### building project:

```
$ mvn clean package
```

#

**Note** : Application runs on default 8080 port, make sure 8080 port is not busy even in ``` mvn clean package``` phase
(used in test execution)

### creating docker image:

```
docker build -t rate-api .
```

### running docker image:

Windows (powershell)

```
docker run --rm --name rate-api -p 8080:8080 -v ${PWD}/log:/log -d rate-api
```

Linux

```
docker run --rm --name rate-api -p 8080:8080 -v $(PWD)/log:/log -d rate-api
```

###

### How to access API

#### you can access [Swagger](http://localhost:8080/swagger-ui/index.htm) of the project at following URL:

http://localhost:8080/swagger-ui/index.html

###

#### if you prefer CLI over any GUI sample curl request template is for you:

```
$ curl --location --request POST 'localhost:8080/rate' \
--header 'Content-Type: application/json' \
--header 'Accept-Language: de' \
--data-raw '{
    "rate": {
        "energy": 0.3,
        "time": 2,
        "transaction": 1
    },
    "cdr": {
        "meterStart": 1204307,
        "timestampStart": "2021-04-05T10:04:00Z",
        "meterStop": 1215230,
        "timestampStop": "2021-04-05T11:27:00Z"
    }
}'
```












  

