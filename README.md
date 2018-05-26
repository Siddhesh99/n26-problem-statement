# N26 Assignment

## Build and Run:
#### Dependencies
- Application uses junit, mockito for testing
- ```build.gradle``` takes care of all the dependencies

#### Build
- Run ```gradle build``` to build the project

#### Run
- Run ```gradle bootrun```
- Above command starts the application on localhost:8080

#### APIs
- `POST /transactions` : saves transactions which are not older than 60 seconds for statistics  calculation
- `GET /statistics` : returns statistics for last 60 seconds

## Project details:
#### Flow
```
POST request on /transactions -> If timestamp on the transaction is not older than 60 seconds, put it into the
    per second bucket for the calculation.
Keep on clearing transactions who's timestamp is older than 60 seconds by an scheduler which runs in background

GET request on /statistics -> return already calculated statistics result in O(1) time

```

#### Details
Store per second aggregated statistics in a `ConcurrentHashMap`.
ConcurrentHashMap is used to allow concurrent request handling and manipulation via scheduled job for deleting old transactions.
This runs at every second and checks whether there is any statistics present for older time,
If there is any such statistics, remove it from the bucket and calculate the final statistics result again.
`synchronised` for result this variable is being updated through save and clear jobs at the same time.

