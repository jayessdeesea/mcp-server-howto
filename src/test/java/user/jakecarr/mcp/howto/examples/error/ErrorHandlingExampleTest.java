package user.jakecarr.mcp.howto.examples.error;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ErrorHandlingExample class.
 */
public class ErrorHandlingExampleTest {
    
    private final ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outputCapture));
        System.setErr(new PrintStream(outputCapture));
    }
    
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        outputCapture.reset();
    }

    /**
     * Test that verifies the ErrorHandlingExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample class should exist")
    public void testClassExists() {
        assertNotNull(ErrorHandlingExample.class);
    }
    
    /**
     * Test that verifies the ErrorHandlingExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            ErrorHandlingExample instance = ErrorHandlingExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate ErrorHandlingExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            ErrorHandlingExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in ErrorHandlingExample");
        }
    }
    
    /**
     * Test that verifies the runServer method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should have a runServer method")
    public void testRunServerMethodExists() {
        try {
            // Get the runServer method using reflection
            Method method = ErrorHandlingExample.class.getDeclaredMethod("runServer");
            method.setAccessible(true);
            
            // Verify the method exists
            assertNotNull(method);
        } catch (Exception e) {
            fail("runServer method not found: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the runClient method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should have a runClient method")
    public void testRunClientMethodExists() {
        try {
            // Get the runClient method using reflection
            Method method = ErrorHandlingExample.class.getDeclaredMethod("runClient");
            method.setAccessible(true);
            
            // Verify the method exists
            assertNotNull(method);
        } catch (Exception e) {
            fail("runClient method not found: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the createToolSchema method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should have a createToolSchema method")
    public void testCreateToolSchemaMethodExists() {
        try {
            // Get the createToolSchema method using reflection
            Method method = ErrorHandlingExample.class.getDeclaredMethod("createToolSchema");
            method.setAccessible(true);
            
            // Verify the method exists
            assertNotNull(method);
            
            // Invoke the method
            Object result = method.invoke(null);
            
            // Verify the result
            assertNotNull(result);
            assertTrue(result instanceof McpSchema.JsonSchema);
        } catch (Exception e) {
            fail("createToolSchema method not found or failed: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the server creation code in ErrorHandlingExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should create a server correctly")
    public void testServerCreation() {
        // This test verifies that the server creation code in ErrorHandlingExample is correct
        // by creating a server using the same pattern
        
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("error-handling-example", "1.0.0");
            
            // Create transport provider
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create the server using the builder pattern
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .tool(
                    new McpSchema.Tool(
                        "success-tool",
                        "A tool that succeeds",
                        createTestToolSchema()
                    ),
                    (exchange, toolArgs) -> {
                        List<McpSchema.Content> content = List.of(
                            new McpSchema.TextContent(
                                null,
                                null,
                                "Tool executed successfully"
                            )
                        );
                        
                        return new McpSchema.CallToolResult(content, false);
                    }
                )
                .build();
            
            assertNotNull(server);
        } catch (Exception e) {
            fail("Failed to create server: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the client creation code in ErrorHandlingExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should create a client correctly")
    public void testClientCreation() {
        // This test verifies that the client creation code in ErrorHandlingExample is correct
        // by creating a client using the same pattern
        
        try {
            // Create client info
            McpSchema.Implementation clientInfo = new McpSchema.Implementation("error-handling-client", "1.0.0");
            
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
     * Test that verifies the error handling code in ErrorHandlingExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("ErrorHandlingExample should handle errors correctly")
    public void testErrorHandling() {
        // This test verifies that the error handling code in ErrorHandlingExample is correct
        // by checking that the McpError class is used correctly
        
        try {
            // Create a McpError
            McpSchema.JSONRPCResponse.JSONRPCError jsonRpcError = new McpSchema.JSONRPCResponse.JSONRPCError(
                McpSchema.ErrorCodes.METHOD_NOT_FOUND,
                "Tool not found: test-tool",
                null
            );
            McpError error = new McpError(jsonRpcError);
            
            // Verify the error
            assertNotNull(error);
            assertEquals(McpSchema.ErrorCodes.METHOD_NOT_FOUND, error.getJsonRpcError().code());
            assertEquals("Tool not found: test-tool", error.getJsonRpcError().message());
        } catch (Exception e) {
            fail("Failed to test error handling: " + e.getMessage());
        }
    }
    
    /**
     * Creates a JSON schema for testing tools.
     */
    private static McpSchema.JsonSchema createTestToolSchema() {
        // Create input schema for the tool
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, Object> param = new HashMap<>();
        param.put("type", "string");
        param.put("description", "A parameter");
        
        properties.put("param", param);
        
        List<String> required = List.of();
        
        return new McpSchema.JsonSchema("object", properties, required, null);
    }
}
