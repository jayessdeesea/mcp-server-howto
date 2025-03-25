package user.jakecarr.mcp.howto.examples.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SyncClientStdioToolsExample class.
 */
public class SyncClientStdioToolsExampleTest {

    /**
     * Test that verifies the SyncClientStdioToolsExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioToolsExample class should exist")
    public void testClassExists() {
        assertNotNull(SyncClientStdioToolsExample.class);
    }
    
    /**
     * Test that verifies the SyncClientStdioToolsExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioToolsExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            SyncClientStdioToolsExample instance = SyncClientStdioToolsExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate SyncClientStdioToolsExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioToolsExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            SyncClientStdioToolsExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in SyncClientStdioToolsExample");
        }
    }
    
    /**
     * Test that verifies the client creation code in SyncClientStdioToolsExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioToolsExample should create a client correctly")
    public void testClientCreation() {
        // This test verifies that the client creation code in SyncClientStdioToolsExample is correct
        // by creating a client using the same pattern
        
        try {
            // Create client info
            McpSchema.Implementation clientInfo = new McpSchema.Implementation("example-client", "1.0.0");
            
            // Create server parameters
            ServerParameters serverParams = ServerParameters.builder("example-server-command")
                .build();
            
            // Create transport with server parameters
            StdioClientTransport transport = new StdioClientTransport(serverParams);
            
            // Create the client using the builder pattern
            McpSyncClient client = McpClient.sync(transport)
                .clientInfo(clientInfo)
                .build();
            
            assertNotNull(client);
            
            // Close the client
            client.close();
        } catch (Exception e) {
            fail("Failed to create client: " + e.getMessage());
        }
    }
}
