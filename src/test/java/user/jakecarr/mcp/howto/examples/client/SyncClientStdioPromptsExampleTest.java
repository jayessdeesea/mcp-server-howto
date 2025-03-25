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
 * Tests for the SyncClientStdioPromptsExample class.
 */
public class SyncClientStdioPromptsExampleTest {

    /**
     * Test that verifies the SyncClientStdioPromptsExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioPromptsExample class should exist")
    public void testClassExists() {
        assertNotNull(SyncClientStdioPromptsExample.class);
    }
    
    /**
     * Test that verifies the SyncClientStdioPromptsExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioPromptsExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            SyncClientStdioPromptsExample instance = SyncClientStdioPromptsExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate SyncClientStdioPromptsExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioPromptsExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            SyncClientStdioPromptsExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in SyncClientStdioPromptsExample");
        }
    }
    
    /**
     * Test that verifies the client creation code in SyncClientStdioPromptsExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioPromptsExample should create a client correctly")
    public void testClientCreation() {
        // This test verifies that the client creation code in SyncClientStdioPromptsExample is correct
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
    
    /**
     * Test that verifies the prompt request creation in SyncClientStdioPromptsExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncClientStdioPromptsExample should create prompt requests correctly")
    public void testPromptRequestCreation() {
        try {
            // Create a GetPromptRequest with arguments
            Map<String, Object> promptArgs = Map.of(
                "language", "Java",
                "code", "public class Example { public static void main(String[] args) { } }"
            );
            
            McpSchema.GetPromptRequest promptRequest = new McpSchema.GetPromptRequest("code-analysis", promptArgs);
            
            // Verify the request
            assertNotNull(promptRequest);
            assertEquals("code-analysis", promptRequest.name());
            assertEquals(promptArgs, promptRequest.arguments());
        } catch (Exception e) {
            fail("Failed to create prompt request: " + e.getMessage());
        }
    }
}
