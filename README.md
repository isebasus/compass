# 📜️ Compass [![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/isebasus/Archive/blob/master/LICENSE)
A silly lightweight Minecraft server API used for server statuses.

# API Calls
The following describes Compass's API calls

### Initialization 
This API call is used to initialize a server to the API, so that way calls to ping can be cached.

``/v1/init``

```
response = {
    hostname: String,
    port: Int,
    protocolVersion: Int,
    serverVersion: String,
    maxPlayerCount: Int,
    playerCount: List,
    description: String,
    favicon: String,
    ping: Long,
    averagePing: List,
    numberOfPings: Long,
    status: ServerError
    lastClientPing: Long
}
```

### Ping
This API call is used after initialization, and the call will now use the API cache. The response will also contain a smaller payload. 

``/v1/ping``

```
response = {
    playerCount: List,
    ping: Long,
    averagePing: List,
    numberOfPings: Long,
    status: ServerError
}
```
