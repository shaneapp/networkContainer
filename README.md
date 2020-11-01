# Network IoC Container

A pseudo networking library implementing inversion of control principles. The library allows configuration of different HttpClient that can allow varying levels of security and methods of performing a network fetch, as well as the decoding mechanism on the raw response produced by that fetch.

**API Usage**
- Responses must inherit from IDecoderResult
- Decoders are responsible for converting raw byte responses into Headers, Body etc

**Creating a Decoder**
```kotlin
data class BodyResponse(val body: String) : IDecoderResult

class BodyDecoder : IResponseDecoder<HeaderResponse> {  
    override fun deserialize(rawResult: ByteArray): HeaderResponse {  
        return BodyResponse(rawResult.toString())  
    }  
}
```

**Creating a HttpClient**
```kotlin
class StandardHttpClient : IHttpClient {  
    override fun performFetch() : ByteArray {
	    // perform network call to server
        return data
    }  
    override fun validateDomain(endpoint: String): Boolean = endpoint.startsWith("http")  
}
```

**Example Usage**
```kotlin 
val secureNetwork = NetworkContainer(StandardHttpClient())  
val headerResult = secureNetwork.requestSync("http://google.co.uk", HttpMethod.GET, BodyDecoder())
```