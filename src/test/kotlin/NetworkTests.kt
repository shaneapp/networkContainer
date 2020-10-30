import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test

class NetworkTests {

    private val secureEndpoint = "https://randomapi.com"
    private val unsecureEndpoint = "http://randomapi.com"

    @Test
    fun testSecureClient() {
        val unsecureNetwork: Network = mockk()
        assertEquals(unsecureNetwork.protocolAllowed(secureEndpoint), true)
        assertEquals(unsecureNetwork.protocolAllowed(unsecureEndpoint), true)

        val secureNetwork: Network = mockk()
        assertEquals(secureNetwork.protocolAllowed(secureEndpoint), true)
        assertEquals(secureNetwork.protocolAllowed(unsecureEndpoint), false)
    }

}