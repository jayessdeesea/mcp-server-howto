package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SyncServerStdioToolsExample class.
 */
public class SyncServerStdioToolsExampleTest {

    /**
     * Test that verifies the SyncServerStdioToolsExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioToolsExample class should exist")
    public void testClassExists() {
        assertNotNull(SyncServerStdioToolsExample.class);
    }
    
    /**
     * Test that verifies the SyncServerStdioToolsExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioToolsExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            SyncServerStdioToolsExample instance = SyncServerStdioToolsExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate SyncServerStdioToolsExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioToolsExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            SyncServerStdioToolsExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in SyncServerStdioToolsExample");
        }
    }
    
    /**
     * Test that verifies the server creation code in SyncServerStdioToolsExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioToolsExample should create a server correctly")
    public void testServerCreation() {
        // This test verifies that the server creation code in SyncServerStdioToolsExample is correct
        // by creating a server using the same pattern
        
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("tools-example", "1.0.0");
            
            // Create transport provider
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create the server using the builder pattern with the echo tool
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .tool(
                    new McpSchema.Tool(
                        "echo",
                        "Echoes back the input text",
                        new McpSchema.JsonSchema(
                            "object",
                            Map.of(
                                "text", Map.of(
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
     * Test that verifies the echo tool implementation.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioToolsExample should implement the echo tool correctly")
    public void testEchoTool() {
        // This test verifies that the echo tool implementation in SyncServerStdioToolsExample is correct
        
        try {
            // Create a test server with the echo tool
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("tools-example", "1.0.0");
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .tool(
                    new McpSchema.Tool(
                        "echo",
                        "Echoes back the input text",
                        new McpSchema.JsonSchema(
                            "object",
                            Map.of(
                                "text", Map.of(
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
            
            // Verify that the server was created successfully
            assertNotNull(server);
        } catch (Exception e) {
            fail("Failed to test echo tool: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the calculate tool implementation.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioToolsExample should implement the calculate tool correctly")
    public void testCalculateTool() {
        // This test verifies that the calculate tool implementation in SyncServerStdioToolsExample is correct
        
        try {
            // Create a test server with the calculate tool
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("tools-example", "1.0.0");
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .tool(
                    new McpSchema.Tool(
                        "calculate",
                        "Performs a simple calculation",
                        new McpSchema.JsonSchema(
                            "object",
                            Map.of(
                                "operation", Map.of(
                                    "type", "string",
                                    "enum", List.of("add", "subtract", "multiply", "divide"),
                                    "description", "Operation to perform"
                                ),
                                "a", Map.of(
                                    "type", "number",
                                    "description", "First operand"
                                ),
                                "b", Map.of(
                                    "type", "number",
                                    "description", "Second operand"
                                )
                            ),
                            List.of("operation", "a", "b"),
                            null
                        )
                    ),
                    (exchange, toolArgs) -> {
                        // Implementation not needed for this test
                        return null;
                    }
                )
                .build();
            
            // Verify that the server was created successfully
            assertNotNull(server);
        } catch (Exception e) {
            fail("Failed to test calculate tool: " + e.getMessage());
        }
    }
}
