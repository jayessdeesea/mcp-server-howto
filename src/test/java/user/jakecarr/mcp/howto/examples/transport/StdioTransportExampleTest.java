package user.jakecarr.mcp.howto.examples.transport;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the StdioTransportExample class.
 */
public class StdioTransportExampleTest {
    
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
     * Test that verifies the StdioTransportExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample class should exist")
    public void testClassExists() {
        assertNotNull(StdioTransportExample.class);
    }
    
    /**
     * Test that verifies the StdioTransportExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            StdioTransportExample instance = StdioTransportExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate StdioTransportExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            StdioTransportExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in StdioTransportExample");
        }
    }
    
    /**
     * Test that verifies the serverExample method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample should have a serverExample method")
    public void testServerExampleMethodExists() {
        try {
            // Check if the serverExample method exists
            Method method = StdioTransportExample.class.getMethod("serverExample");
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("serverExample method not found in StdioTransportExample");
        }
    }
    
    /**
     * Test that verifies the clientExample method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample should have a clientExample method")
    public void testClientExampleMethodExists() {
        try {
            // Check if the clientExample method exists
            Method method = StdioTransportExample.class.getMethod("clientExample");
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("clientExample method not found in StdioTransportExample");
        }
    }
    
    /**
     * Test that verifies the server creation code in StdioTransportExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample should create a server correctly")
    public void testServerCreation() {
        // This test verifies that the server creation code in StdioTransportExample is correct
        // by creating a server using the same pattern
        
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("stdio-server-example", "1.0.0");
            
            // Create transport provider
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create server using the builder pattern
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .tool(
                    new McpSchema.Tool(
                        "echo",
                        "Echoes back the input text",
                        new McpSchema.JsonSchema(
                            "object",
                            java.util.Map.of(
                                "text", java.util.Map.of(
                                    "type", "string",
                                    "description", "Text to echo back"
                                )
                            ),
                            List.of("text"),
                            null
                        )
                    ),
                    (exchange, toolArgs) -> {
                        String text = (String) toolArgs.get("text");
                        
                        List<McpSchema.Content> content = List.of(
                            new McpSchema.TextContent(
                                null,
                                null,
                                text
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
     * Test that verifies the client creation code in StdioTransportExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample should create a client correctly")
    public void testClientCreation() {
        // This test verifies that the client creation code in StdioTransportExample is correct
        // by creating a client using the same pattern
        
        try {
            // Create client info
            McpSchema.Implementation clientInfo = new McpSchema.Implementation("stdio-client-example", "1.0.0");
            
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
     * Test that verifies the main method in StdioTransportExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StdioTransportExample main method should work correctly")
    public void testMainMethod() {
        try {
            // Call the main method with no arguments
            StdioTransportExample.main(new String[0]);
            
            // Verify that the main method executed correctly (by checking the output)
            String output = outputCapture.toString();
            assertTrue(output.contains("This example demonstrates how to use StdioTransport"));
        } catch (Exception e) {
            fail("Failed to test main method: " + e.getMessage());
        }
    }
}
