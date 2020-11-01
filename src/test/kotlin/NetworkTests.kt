import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Test

class NetworkTests {

    private val secureEndpoint = "https://randomapi.com"
    private val unsecureEndpoint = "http://randomapi.com"

    @Test
    fun testSecureClient() {
        val unsecureNetwork = NetworkContainer(UnsecureHttpClient())
        assertEquals(unsecureNetwork.protocolAllowed(secureEndpoint), true)
        assertEquals(unsecureNetwork.protocolAllowed(unsecureEndpoint), true)

        val secureNetwork = NetworkContainer(SecureHttpClient())
        assertEquals(secureNetwork.protocolAllowed(secureEndpoint), true)
        assertEquals(secureNetwork.protocolAllowed(unsecureEndpoint), false)
    }

    @Test
    fun testDecoderDeserializesResponse() {
        val network = NetworkContainer(object : IHttpClient {
            override fun performFetch() = "Test response".toByteArray()
            override fun validateDomain(endpoint: String) = true
        })

        val debugDecoder = object : IResponseDecoder<DebugResponse> {
            override fun deserialize(rawResult: ByteArray): DebugResponse {
                return DebugResponse(rawResult.toString(Charsets.UTF_8))
            }
        }

        mockkObject(network)
        every { network.protocolAllowed(any()) } returns true

        val postResult = network.requestSync(secureEndpoint, HttpMethod.POST, debugDecoder)

        verify(exactly = 1) { network.requestSync(any(), any(), any()) }
        assertEquals(postResult.debugOutput, "Test response")
    }

}