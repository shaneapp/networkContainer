
private const val BODY_RESPONSE = "Hello World"
private val RESPONSE_EXAMPLE = byteArrayOf(0xC8.toByte(), 0x0B.toByte()) +
        "text/html".toByteArray() +
        BODY_RESPONSE.toByteArray()

fun main(args: Array<String>) {
    println("Creating new network container")

    println("Calling unsecure endpoint")
    val unsecureEndpoint = "http://coolapi.com"
    val unsecureNetwork = NetworkContainer(UnsecureHttpClient())
    val debugResult = unsecureNetwork.requestSync(unsecureEndpoint, HttpMethod.GET, DebugDecoder())
    println("Server response: ${debugResult.debugOutput}\n")

    println("Calling secure endpoint")
    val secureEndpoint = "https://coolapi.com"
    val secureNetwork = NetworkContainer(SecureHttpClient())
    val headerResult = secureNetwork.requestSync(secureEndpoint, HttpMethod.POST, HeaderDecoder())
    println("Header Result: $headerResult")
}

class NetworkContainer(private val httpClient: IHttpClient) : Network {
    override fun <T: IDecoderResult>requestSync(endpoint: String, httpMethod: HttpMethod,
                                                responseDecoder: IResponseDecoder<T>) : T {
        println("performing ${httpMethod.name} on endpoint: $endpoint")
        return responseDecoder.deserialize(httpClient.performFetch())
    }

    override fun protocolAllowed(endpoint: String): Boolean {
        return httpClient.validateDomain(endpoint)
    }
}

class HeaderDecoder : IResponseDecoder<HeaderResponse> {
    override fun deserialize(rawResult: ByteArray): HeaderResponse {
        println("deserializing header")
        val statusCode = rawResult[0].toUByte().toInt()
        val length = rawResult[1].toUByte().toInt()
        val mimeType = rawResult.copyOfRange(2, 11).toString(Charsets.UTF_8)
        return HeaderResponse(statusCode, length, mimeType)
    }
}

class DebugDecoder : IResponseDecoder<DebugResponse> {
    override fun deserialize(rawResult: ByteArray): DebugResponse {
        println("deserializing entire response as debug string")
        return DebugResponse(rawResult.toString(Charsets.UTF_8))
    }
}

class UnsecureHttpClient : IHttpClient {
    override fun performFetch() : ByteArray {
        println("fetching response in plain text")
        return RESPONSE_EXAMPLE
    }
    override fun validateDomain(endpoint: String): Boolean = endpoint.startsWith("http")
}

class SecureHttpClient : IHttpClient {
    override fun performFetch() : ByteArray {
        println("fetching response using encryption")
        return RESPONSE_EXAMPLE
    }
    override fun validateDomain(endpoint: String) = endpoint.startsWith("https")
}

interface IHttpClient {
    fun performFetch(): ByteArray
    fun validateDomain(endpoint: String): Boolean
}

interface Network {
    fun protocolAllowed(endpoint: String): Boolean
    fun <T: IDecoderResult>requestSync(endpoint: String, httpMethod: HttpMethod, responseDecoder: IResponseDecoder<T>): T
}

enum class HttpMethod { GET, POST, PUT, DELETE }

interface IResponseDecoder<T: IDecoderResult> {
    fun deserialize(rawResult: ByteArray): T
}

interface IDecoderResult
data class HeaderResponse(val statusCode: Int, val contentLength: Int, val contentType: String) : IDecoderResult
data class DebugResponse(val debugOutput: String) : IDecoderResult