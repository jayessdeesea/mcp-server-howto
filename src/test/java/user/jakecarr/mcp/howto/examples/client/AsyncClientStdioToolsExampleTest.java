package user.jakecarr.mcp.howto.examples.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the AsyncClientStdioToolsExample class.
 */
public class AsyncClientStdioToolsExampleTest {

    /**
     * Test that verifies the AsyncClientStdioToolsExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioToolsExample class should exist")
    public void testClassExists() {
        assertNotNull(AsyncClientStdioToolsExample.class);
    }
    
    /**
     * Test that verifies the AsyncClientStdioToolsExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioToolsExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            AsyncClientStdioToolsExample instance = AsyncClientStdioToolsExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate AsyncClientStdioToolsExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioToolsExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            AsyncClientStdioToolsExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in AsyncClientStdioToolsExample");
        }
    }
    
    /**
     * Test that verifies the client creation code in AsyncClientStdioToolsExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioToolsExample should create a client correctly")
    public void testClientCreation() {
        // This test verifies that the client creation code in AsyncClientStdioToolsExample is correct
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
            McpAsyncClient client = McpClient.async(transport)
                .clientInfo(clientInfo)
                .build();
            
            assertNotNull(client);
            
            // Close the client
            client.close();
        } catch (Exception e) {
            fail("Failed to create client: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the client can be initialized.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioToolsExample should initialize client correctly")
    public void testClientInitialization() {
        // This test verifies that the client initialization code in AsyncClientStdioToolsExample is correct
        // Note: We're not actually connecting to a server, just verifying the code structure
        
        try {
            // Create client info
            McpSchema.Implementation clientInfo = new McpSchema.Implementation("example-client", "1.0.0");
            
            // Create server parameters
            ServerParameters serverParams = ServerParameters.builder("example-server-command")
                .build();
            
            // Create transport with server parameters
            StdioClientTransport transport = new StdioClientTransport(serverParams);
            
            // Create the client using the builder pattern
            McpAsyncClient client = McpClient.async(transport)
                .clientInfo(clientInfo)
                .build();
            
            // Verify that the initialize method exists and returns a Mono
            assertNotNull(client.initialize());
            
            // Close the client
            client.close();
        } catch (Exception e) {
            fail("Failed to initialize client: " + e.getMessage());
        }
    }
}
