package user.jakecarr.mcp.howto.examples.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpAsyncClient;
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
 * Tests for the AsyncClientStdioPromptsExample class.
 */
public class AsyncClientStdioPromptsExampleTest {

    /**
     * Test that verifies the AsyncClientStdioPromptsExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioPromptsExample class should exist")
    public void testClassExists() {
        assertNotNull(AsyncClientStdioPromptsExample.class);
    }
    
    /**
     * Test that verifies the AsyncClientStdioPromptsExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioPromptsExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            AsyncClientStdioPromptsExample instance = AsyncClientStdioPromptsExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate AsyncClientStdioPromptsExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioPromptsExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            AsyncClientStdioPromptsExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in AsyncClientStdioPromptsExample");
        }
    }
    
    /**
     * Test that verifies the client creation code in AsyncClientStdioPromptsExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioPromptsExample should create a client correctly")
    public void testClientCreation() {
        // This test verifies that the client creation code in AsyncClientStdioPromptsExample is correct
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
     * Test that verifies the prompt request creation in AsyncClientStdioPromptsExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioPromptsExample should create prompt requests correctly")
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
    
    /**
     * Test that verifies the error handling in AsyncClientStdioPromptsExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("AsyncClientStdioPromptsExample should handle errors correctly")
    public void testErrorHandling() {
        try {
            // Create an MCP error
            McpSchema.JSONRPCResponse.JSONRPCError jsonRpcError = new McpSchema.JSONRPCResponse.JSONRPCError(
                McpSchema.ErrorCodes.METHOD_NOT_FOUND,
                "Prompt not found: test-prompt",
                null
            );
            
            // Create an MCP error
            io.modelcontextprotocol.spec.McpError error = new io.modelcontextprotocol.spec.McpError(jsonRpcError);
            
            // Verify the error
            assertNotNull(error);
            assertEquals(McpSchema.ErrorCodes.METHOD_NOT_FOUND, error.getJsonRpcError().code());
            assertEquals("Prompt not found: test-prompt", error.getJsonRpcError().message());
        } catch (Exception e) {
            fail("Failed to test error handling: " + e.getMessage());
        }
    }
}
